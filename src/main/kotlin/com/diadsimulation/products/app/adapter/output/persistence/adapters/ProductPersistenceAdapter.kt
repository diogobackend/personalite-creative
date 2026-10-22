package com.diadsimulation.products.app.adapter.output.persistence.adapters

import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductFeeEntity
import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductPriceEntity
import com.diadsimulation.products.app.adapter.output.persistence.mappers.ProductPersistenceMapper
import com.diadsimulation.products.app.adapter.output.persistence.repositories.ProductJpaRepository
import com.diadsimulation.products.app.adapter.output.persistence.repositories.ProductSpecifications
import com.diadsimulation.products.core.domain.exceptions.ProductNotFoundException
import com.diadsimulation.products.core.domain.model.GetProductsFilter
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductsPage
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import jakarta.persistence.criteria.JoinType
import org.hibernate.internal.util.collections.CollectionHelper.setOf
import org.springframework.aot.hint.TypeReference.listOf
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Component
class ProductPersistenceAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productPersistenceMapper: ProductPersistenceMapper,
) : ProductRepositoryOutputPort {
    @Transactional
    override fun save(product: Product): Product {
        val entity = productPersistenceMapper.toEntity(product)
        val savedEntity = productJpaRepository.save(entity)

        return productPersistenceMapper.toDomain(savedEntity)
    }

    @Transactional
    override fun update(product: Product): Product {
        val entity =
            productJpaRepository
                .findById(product.id)
                .orElseThrow { ProductNotFoundException(product.id) }

        entity.apply {
            name = product.name
            category = product.category
            description = product.description
            minimumQuantity = product.minimumQuantity
            pricingType = product.pricingType
            shelfLifeDays = product.shelfLifeDays
            productionLeadTimeMinDays = product.productionLeadTimeMinDays
            productionLeadTimeMaxDays = product.productionLeadTimeMaxDays
            active = product.active
            updatedAt = product.updatedAt
        }

        val priceIds = product.prices.map { it.id }.toSet()

        entity.prices.removeIf { existing ->
            existing.id !in priceIds
        }

        product.prices.forEach { price ->
            val existing = entity.prices.find { it.id == price.id }

            if (existing == null) {
                entity.prices.add(
                    ProductPriceEntity(
                        id = price.id,
                        product = entity,
                        minimumQuantity = price.minimumQuantity,
                        unitPrice = price.unitPrice,
                        createdAt = price.createdAt,
                        updatedAt = price.updatedAt,
                    ),
                )
            } else if (
                existing.minimumQuantity != price.minimumQuantity ||
                existing.unitPrice.compareTo(price.unitPrice) != 0
            ) {
                existing.minimumQuantity = price.minimumQuantity
                existing.unitPrice = price.unitPrice
                existing.updatedAt = price.updatedAt
            }
        }

        val feeIds = product.fees.map { it.id }.toSet()

        entity.fees.removeIf { existing ->
            existing.id !in feeIds
        }

        product.fees.forEach { fee ->
            val existing = entity.fees.find { it.id == fee.id }

            if (existing == null) {
                entity.fees.add(
                    ProductFeeEntity(
                        id = fee.id,
                        product = entity,
                        name = fee.name,
                        amount = fee.amount,
                        chargeType = fee.chargeType,
                        required = fee.required,
                        active = fee.active,
                        createdAt = fee.createdAt,
                        updatedAt = fee.updatedAt,
                    ),
                )
            } else if (
                existing.name != fee.name ||
                existing.amount.compareTo(fee.amount) != 0 ||
                existing.chargeType != fee.chargeType ||
                existing.required != fee.required ||
                existing.active != fee.active
            ) {
                existing.name = fee.name
                existing.amount = fee.amount
                existing.chargeType = fee.chargeType
                existing.required = fee.required
                existing.active = fee.active
                existing.updatedAt = fee.updatedAt
            }
        }

        return productPersistenceMapper.toDomain(
            productJpaRepository.saveAndFlush(entity),
        )
    }

    override fun existsByCode(code: String): Boolean = productJpaRepository.existsByCode(code)

    @Transactional(readOnly = true)
    override fun findByCode(code: String): Product? =
        productJpaRepository
            .findByCode(code)
            ?.let(productPersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Product? =
        productJpaRepository
            .findById(id)
            .orElse(null)
            ?.let(productPersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findAll(filter: GetProductsFilter): ProductsPage {
        filter.code?.let { code ->
            val products =
                productJpaRepository
                    .findByCode(code)
                    ?.let(productPersistenceMapper::toDomain)
                    ?.let(::listOf)
                    .orEmpty()

            return ProductsPage(
                content = products,
                page = 0,
                size = 1,
                totalElementsThisPage = products.size,
                totalElements = products.size.toLong(),
                totalPages = if (products.isEmpty()) 0 else 1,
            )
        }

        val allowedSortFields =
            setOf(
                "name",
                "code",
                "createdAt",
                "updatedAt",
                "minimumQuantity",
                "shelfLifeDays",
            )

        val sortBy =
            filter.sortBy
                .takeIf { it == "price" || it in allowedSortFields }
                ?: "name"

        val direction =
            if (filter.sortDirection.equals("DESC", ignoreCase = true)) {
                Sort.Direction.DESC
            } else {
                Sort.Direction.ASC
            }

        val specification = ProductSpecifications.withFilters(filter)

        val pageable =
            if (sortBy == "price") {
                PageRequest.of(
                    filter.page.coerceAtLeast(0),
                    filter.size.coerceIn(1, 100),
                )
            } else {
                PageRequest.of(
                    filter.page.coerceAtLeast(0),
                    filter.size.coerceIn(1, 100),
                    Sort.by(direction, sortBy).and(Sort.by("id")),
                )
            }

        val effectiveSpecification =
            if (sortBy == "price") {
                specification.and(sortByMinimumPrice(direction))
            } else {
                specification
            }

        val result =
            productJpaRepository.findAll(
                effectiveSpecification,
                pageable,
            )

        return ProductsPage(
            content = result.content.map(productPersistenceMapper::toDomain),
            page = result.number,
            size = result.size,
            totalElementsThisPage = result.numberOfElements,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
        )
    }

    private fun sortByMinimumPrice(
        direction: Sort.Direction,
    ): Specification<com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity> =
        Specification { root, query, criteriaBuilder ->
            if (query.resultType != Long::class.java && query.resultType != java.lang.Long.TYPE) {
                val priceSubquery = query.subquery(BigDecimal::class.java)
                val priceRoot =
                    priceSubquery.from(
                        com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity::class.java,
                    )
                val prices = priceRoot.join<Any, Any>("prices", JoinType.INNER)

                priceSubquery
                    .select(criteriaBuilder.min(prices.get<BigDecimal>("unitPrice")))
                    .where(
                        criteriaBuilder.equal(
                            priceRoot.get<UUID>("id"),
                            root.get<UUID>("id"),
                        ),
                    )

                val minimumPrice = priceSubquery.selection

                val withoutPrice =
                    criteriaBuilder
                        .selectCase<Int>()
                        .`when`(criteriaBuilder.isNull(minimumPrice), 1)
                        .otherwise(0)

                val priceOrder =
                    if (direction == Sort.Direction.ASC) {
                        criteriaBuilder.asc(minimumPrice)
                    } else {
                        criteriaBuilder.desc(minimumPrice)
                    }

                query.orderBy(
                    criteriaBuilder.asc(withoutPrice),
                    priceOrder,
                    criteriaBuilder.asc(root.get<String>("code")),
                )
            }

            criteriaBuilder.conjunction()
        }
}
