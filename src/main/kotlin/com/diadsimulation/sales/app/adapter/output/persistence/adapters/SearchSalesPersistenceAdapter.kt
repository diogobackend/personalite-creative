package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import com.diadsimulation.sales.app.adapter.output.persistence.mappers.SalePersistenceMapper
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleFinancialSummaryRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleItemJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.specifications.SaleSpecification
import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import com.diadsimulation.sales.core.ports.output.SaleSearchResult
import com.diadsimulation.sales.core.ports.output.SearchSalesOutputPort
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Component
class SearchSalesPersistenceAdapter(
    private val saleJpaRepository: SaleJpaRepository,
    private val saleItemJpaRepository: SaleItemJpaRepository,
    private val salePersistenceMapper: SalePersistenceMapper,
    private val saleFinancialSummaryRepository: SaleFinancialSummaryRepository,
    private val entityManager: EntityManager,
) : SearchSalesOutputPort {
    @Transactional(readOnly = true)
    override fun search(
        filter: SaleSearchFilter,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): SaleSearchResult {
        val descending = sortDirection.equals("DESC", ignoreCase = true)
        val today = LocalDate.now()

        val result =
            if (sortBy == "remainingAmount") {
                searchByRemainingAmount(filter, page, size, descending, today)
            } else {
                val direction = if (descending) DESC else ASC

                val pageable =
                    PageRequest.of(
                        page,
                        size,
                        Sort.by(
                            Sort.Order(direction, sortBy),
                            Sort.Order.asc("id"),
                        ),
                    )

                val salesPage =
                    saleJpaRepository.findAll(
                        SaleSpecification.byFilter(filter, today),
                        pageable,
                    )

                salesPage.content to salesPage.totalElements
            }

        val sales =
            result.first.map { entity ->
                val items = saleItemJpaRepository.findAllBySaleId(entity.id)
                salePersistenceMapper.toDomain(entity, items)
            }

        return SaleSearchResult(
            sales = sales,
            totalElements = result.second,
            totalPages = calculateTotalPages(result.second, size),
            page = page,
            size = size,
            summary = saleFinancialSummaryRepository.summarize(filter, today),
        )
    }

    private fun searchByRemainingAmount(
        filter: SaleSearchFilter,
        page: Int,
        size: Int,
        descending: Boolean,
        today: LocalDate,
    ): Pair<List<SaleEntity>, Long> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(SaleEntity::class.java)
        val root = query.from(SaleEntity::class.java)

        val remainingAmount = remainingAmount(root)

        query.select(root)

        SaleSpecification
            .byFilter(filter, today)
            .toPredicate(root, query, cb)
            ?.let(query::where)

        query.orderBy(
            if (descending) {
                cb.desc(remainingAmount)
            } else {
                cb.asc(remainingAmount)
            },
            cb.asc(root.get<UUID>("id")),
        )

        val sales =
            entityManager
                .createQuery(query)
                .setFirstResult(Math.multiplyExact(page, size))
                .setMaxResults(size)
                .resultList

        val countQuery = cb.createQuery(Long::class.javaObjectType)
        val countRoot = countQuery.from(SaleEntity::class.java)

        countQuery.select(cb.count(countRoot))

        SaleSpecification
            .byFilter(filter, today)
            .toPredicate(countRoot, countQuery, cb)
            ?.let(countQuery::where)

        val total = entityManager.createQuery(countQuery).singleResult

        return sales to total
    }

    private fun remainingAmount(root: Root<SaleEntity>): Expression<BigDecimal> {
        val cb = entityManager.criteriaBuilder

        val paidAmount =
            cb.function(
                "round",
                BigDecimal::class.java,
                cb.quot(
                    cb.prod(
                        root.get<BigDecimal>("totalAmount"),
                        root.get<BigDecimal>("paidPercentage"),
                    ),
                    BigDecimal("100"),
                ),
                cb.literal(2),
            )

        return cb.diff(
            root.get<BigDecimal>("totalAmount"),
            paidAmount,
        )
    }

    private fun calculateTotalPages(
        totalElements: Long,
        size: Int,
    ): Int = ((totalElements / size) + if (totalElements % size > 0) 1 else 0).toInt()
}
