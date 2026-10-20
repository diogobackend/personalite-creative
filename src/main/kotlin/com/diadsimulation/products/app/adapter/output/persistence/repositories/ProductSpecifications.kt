package com.diadsimulation.products.app.adapter.output.persistence.repositories

import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity
import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductFeeEntity
import com.diadsimulation.products.core.domain.model.GetProductsFilter
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object ProductSpecifications {
    fun withFilters(filter: GetProductsFilter): Specification<ProductEntity> =
        Specification { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            if (filter.code != null) {
                return@Specification criteriaBuilder.equal(
                    root.get<String>("code"),
                    filter.code,
                )
            }

            filter.name?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%${it.trim().lowercase()}%",
                    )
            }

            filter.description?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%${it.trim().lowercase()}%",
                    )
            }

            filter.category?.let {
                predicates += criteriaBuilder.equal(root.get<Any>("category"), it)
            }

            filter.active?.let {
                predicates += criteriaBuilder.equal(root.get<Boolean>("active"), it)
            }

            filter.pricingType?.let {
                predicates += criteriaBuilder.equal(root.get<Any>("pricingType"), it)
            }

            filter.minQuantity?.let {
                predicates +=
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("minimumQuantity"),
                        it,
                    )
            }

            filter.maxQuantity?.let {
                predicates +=
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("minimumQuantity"),
                        it,
                    )
            }

            filter.minShelfLifeDays?.let {
                predicates +=
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("shelfLifeDays"),
                        it,
                    )
            }

            filter.maxShelfLifeDays?.let {
                predicates +=
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("shelfLifeDays"),
                        it,
                    )
            }

            filter.createdFrom?.let {
                predicates +=
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        it,
                    )
            }

            filter.createdTo?.let {
                predicates +=
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        it,
                    )
            }

            val hasPriceFilters = filter.minPrice != null || filter.maxPrice != null

            if (hasPriceFilters) {
                val priceSubquery = query.subquery(Int::class.java)
                val priceRoot = priceSubquery.from(ProductEntity::class.java)
                val prices = priceRoot.join<Any, Any>("prices", JoinType.INNER)

                val pricePredicates =
                    mutableListOf<Predicate>(
                        criteriaBuilder.equal(priceRoot.get<Any>("id"), root.get<Any>("id")),
                    )

                filter.minPrice?.let {
                    pricePredicates +=
                        criteriaBuilder.greaterThanOrEqualTo(
                            prices.get("unitPrice"),
                            it,
                        )
                }

                filter.maxPrice?.let {
                    pricePredicates +=
                        criteriaBuilder.lessThanOrEqualTo(
                            prices.get("unitPrice"),
                            it,
                        )
                }

                priceSubquery
                    .select(criteriaBuilder.literal(1))
                    .where(*pricePredicates.toTypedArray())

                predicates += criteriaBuilder.exists(priceSubquery)
            }

            val hasFeeFilters =
                filter.feeName?.isNotBlank() == true ||
                    filter.chargeType != null ||
                    filter.minFeeAmount != null ||
                    filter.maxFeeAmount != null

            if (hasFeeFilters) {
                val feeSubquery = query.subquery(Int::class.java)
                val feeRoot = feeSubquery.from(ProductEntity::class.java)
                val fees = feeRoot.join<Any, Any>("fees", JoinType.INNER)

                val feePredicates =
                    mutableListOf<Predicate>(
                        criteriaBuilder.equal(feeRoot.get<Any>("id"), root.get<Any>("id")),
                    )

                filter.feeName?.takeIf { it.isNotBlank() }?.let {
                    feePredicates +=
                        criteriaBuilder.like(
                            criteriaBuilder.lower(fees.get("name")),
                            "%${it.trim().lowercase()}%",
                        )
                }

                filter.chargeType?.let {
                    feePredicates += criteriaBuilder.equal(fees.get<Any>("chargeType"), it)
                }

                filter.minFeeAmount?.let {
                    feePredicates +=
                        criteriaBuilder.greaterThanOrEqualTo(
                            fees.get("amount"),
                            it,
                        )
                }

                filter.maxFeeAmount?.let {
                    feePredicates +=
                        criteriaBuilder.lessThanOrEqualTo(
                            fees.get("amount"),
                            it,
                        )
                }

                feeSubquery
                    .select(criteriaBuilder.literal(1))
                    .where(*feePredicates.toTypedArray())

                predicates += criteriaBuilder.exists(feeSubquery)
            }

            filter.hasFees?.let { hasFees ->
                val feeSubquery = query.subquery(Int::class.java)
                val feeRoot = feeSubquery.from(ProductEntity::class.java)
                feeSubquery
                    .select(criteriaBuilder.literal(1))
                    .where(
                        criteriaBuilder.equal(
                            feeRoot.get<Any>("id"),
                            root.get<Any>("id"),
                        ),
                        criteriaBuilder.isNotEmpty(
                            feeRoot.get<Collection<ProductFeeEntity>>("fees"),
                        ),
                    )

                predicates +=
                    if (hasFees) {
                        criteriaBuilder.exists(feeSubquery)
                    } else {
                        criteriaBuilder.not(criteriaBuilder.exists(feeSubquery))
                    }
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
}
