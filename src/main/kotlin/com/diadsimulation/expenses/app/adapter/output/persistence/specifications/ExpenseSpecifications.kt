package com.diadsimulation.expenses.app.adapter.output.persistence.specifications

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseEntity
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.UUID

object ExpenseSpecifications {
    fun withFilters(filter: GetExpensesFilter): Specification<ExpenseEntity> =
        Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.name?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    cb.like(
                        cb.lower(root.get("name")),
                        "%${it.trim().lowercase()}%",
                    )
            }

            filter.description?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    cb.like(
                        cb.lower(root.get("description")),
                        "%${it.trim().lowercase()}%",
                    )
            }

            filter.value?.let {
                predicates += cb.equal(root.get<BigDecimal>("value"), it)
            }

            filter.minValue?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("value"),
                        it,
                    )
            }

            filter.maxValue?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("value"),
                        it,
                    )
            }

            filter.category?.let {
                predicates += cb.equal(root.get<Any>("category"), it)
            }

            filter.allocationType?.let {
                predicates += cb.equal(root.get<Any>("allocationType"), it)
            }

            filter.status?.let {
                predicates += cb.equal(root.get<Any>("status"), it)
            }

            filter.productId?.let {
                val products = root.join<ExpenseEntity, Any>("products", JoinType.INNER)

                predicates +=
                    cb.equal(
                        products.get<UUID>("productId"),
                        it,
                    )
            }

            filter.quantity?.let {
                predicates += cb.equal(root.get<Int>("quantity"), it)
            }

            filter.minQuantity?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("quantity"),
                        it,
                    )
            }

            filter.maxQuantity?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("quantity"),
                        it,
                    )
            }

            filter.expenseDate?.let {
                predicates += cb.equal(root.get<LocalDate>("expenseDate"), it)
            }

            filter.expenseDateFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("expenseDate"),
                        it,
                    )
            }

            filter.createdAt?.let { date ->
                predicates.add(
                    cb.between(
                        root.get("createdAt"),
                        date.atStartOfDay().atOffset(UTC),
                        date
                            .plusDays(1)
                            .atStartOfDay()
                            .atOffset(UTC)
                            .minusNanos(1),
                    ),
                )
            }

            filter.expenseDateTo?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("expenseDate"),
                        it,
                    )
            }

            filter.createdFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get<OffsetDateTime>("createdAt"),
                        it,
                    )
            }

            filter.createdTo?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get<OffsetDateTime>("createdAt"),
                        it,
                    )
            }

            filter.updatedFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get<OffsetDateTime>("updatedAt"),
                        it,
                    )
            }

            filter.updatedTo?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get<OffsetDateTime>("updatedAt"),
                        it,
                    )
            }

            cb.and(*predicates.toTypedArray())
        }
}
