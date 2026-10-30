package com.diadsimulation.sales.app.adapter.output.persistence.specifications

import com.diadsimulation.auth.app.adapter.output.persistence.entities.UserEntity
import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity
import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleItemEntity
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.DUE_TODAY
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.NO_DEADLINE
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.ON_TIME
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.OVERDUE
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus.PAID
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus.PARTIALLY_PAID
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus.UNPAID
import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.persistence.criteria.Subquery
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID

object SaleSpecification {
    private val HUNDRED = BigDecimal("100")

    fun byFilter(
        filter: SaleSearchFilter,
        today: LocalDate = LocalDate.now(),
    ): Specification<SaleEntity> =
        Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.saleId?.let {
                predicates += cb.equal(root.get<UUID>("id"), it)
            }

            filter.customer?.takeIf(String::isNotBlank)?.let {
                predicates +=
                    customerExists(
                        root,
                        query!!.subquery(Int::class.javaObjectType),
                        cb,
                        it,
                    )
            }

            filter.minTotalAmount?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("totalAmount"),
                        it,
                    )
            }

            filter.maxTotalAmount?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("totalAmount"),
                        it,
                    )
            }

            filter.minRemainingAmount?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        remainingAmount(root, cb),
                        it,
                    )
            }

            filter.maxRemainingAmount?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        remainingAmount(root, cb),
                        it,
                    )
            }

            filter.observation?.takeIf(String::isNotBlank)?.let {
                predicates += contains(cb, root.get("observation"), it)
            }

            filter.deliveryDeadlineFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("deliveryDeadline"),
                        it,
                    )
            }

            filter.deliveryDeadlineTo?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("deliveryDeadline"),
                        it,
                    )
            }

            filter.dueWithinDays?.let {
                predicates +=
                    cb.between(
                        root.get("deliveryDeadline"),
                        today,
                        today.plusDays(it.toLong()),
                    )
            }

            filter.createdAtFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        it.atStartOfDay().atOffset(ZoneOffset.UTC),
                    )
            }

            filter.createdAtTo?.let {
                predicates +=
                    cb.lessThan(
                        root.get("createdAt"),
                        it.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC),
                    )
            }

            percentageRange(
                predicates,
                root,
                cb,
                "downPaymentPercentage",
                filter.downPaymentFrom,
                filter.downPaymentTo,
            )

            percentageRange(
                predicates,
                root,
                cb,
                "paidPercentage",
                filter.paidFrom,
                filter.paidTo,
            )

            filter.remainingFrom?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("paidPercentage"),
                        HUNDRED.subtract(it),
                    )
            }

            filter.remainingTo?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("paidPercentage"),
                        HUNDRED.subtract(it),
                    )
            }

            filter.paymentMethod?.let {
                predicates +=
                    cb.equal(
                        root.get<String>("paymentMethod"),
                        it.name,
                    )
            }

            filter.paymentStatus?.let { status ->
                val paid = root.get<BigDecimal>("paidPercentage")

                predicates +=
                    when (status) {
                        UNPAID -> {
                            cb.equal(paid, BigDecimal.ZERO)
                        }

                        PARTIALLY_PAID -> {
                            cb.and(
                                cb.greaterThan(paid, BigDecimal.ZERO),
                                cb.lessThan(paid, HUNDRED),
                            )
                        }

                        PAID -> {
                            cb.greaterThanOrEqualTo(paid, HUNDRED)
                        }
                    }
            }

            filter.deadlineStatus?.let { status ->
                val deadline = root.get<LocalDate>("deliveryDeadline")

                predicates +=
                    when (status) {
                        NO_DEADLINE -> cb.isNull(deadline)
                        ON_TIME -> cb.greaterThan(deadline, today)
                        DUE_TODAY -> cb.equal(deadline, today)
                        OVERDUE -> cb.lessThan(deadline, today)
                    }
            }

            val hasProductFilter =
                listOf(
                    filter.productName,
                    filter.productCommercialCode,
                    filter.color,
                    filter.brand,
                    filter.model,
                    filter.size,
                    filter.material,
                ).any { !it.isNullOrBlank() }

            if (hasProductFilter) {
                predicates +=
                    productExists(
                        root,
                        query!!.subquery(Int::class.javaObjectType),
                        cb,
                        filter,
                    )
            }

            filter.search?.takeIf(String::isNotBlank)?.let { search ->
                predicates +=
                    cb.or(
                        contains(cb, root.get("observation"), search),
                        customerExists(
                            root,
                            query!!.subquery(Int::class.javaObjectType),
                            cb,
                            search,
                        ),
                        productExists(
                            root,
                            query!!.subquery(Int::class.javaObjectType),
                            cb,
                            SaleSearchFilter(productName = search),
                            searchAnyProductField = true,
                        ),
                    )
            }

            cb.and(*predicates.toTypedArray())
        }

    private fun customerExists(
        sale: Root<SaleEntity>,
        subquery: Subquery<Int>,
        cb: CriteriaBuilder,
        value: String,
    ): Predicate {
        val customer = subquery.from(UserEntity::class.java)

        val normalizedValue = normalizeDigits(value)

        val matches =
            mutableListOf(
                contains(cb, customer.get("name"), value),
                contains(cb, customer.get("email"), value),
                contains(cb, customer.get("cpf"), value),
                contains(cb, customer.get("phone"), value),
            )

        if (normalizedValue.isNotEmpty()) {
            matches +=
                cb.like(
                    digitsOnly(cb, customer.get("cpf")),
                    "%$normalizedValue%",
                )

            matches +=
                cb.like(
                    digitsOnly(cb, customer.get("phone")),
                    "%$normalizedValue%",
                )
        }

        subquery
            .select(cb.literal(1))
            .where(
                cb.equal(
                    customer.get<UUID>("userId"),
                    sale.get<UUID>("customerId"),
                ),
                cb.or(*matches.toTypedArray()),
            )

        return cb.exists(subquery)
    }

    private fun productExists(
        sale: Root<SaleEntity>,
        subquery: Subquery<Int>,
        cb: CriteriaBuilder,
        filter: SaleSearchFilter,
        searchAnyProductField: Boolean = false,
    ): Predicate {
        val item = subquery.from(SaleItemEntity::class.java)
        val product = subquery.from(ProductEntity::class.java)

        val predicates =
            mutableListOf(
                cb.equal(
                    item.get<UUID>("saleId"),
                    sale.get<UUID>("id"),
                ),
                cb.equal(
                    item.get<UUID>("productId"),
                    product.get<UUID>("id"),
                ),
            )

        if (searchAnyProductField) {
            val search = filter.productName.orEmpty()

            predicates +=
                cb.or(
                    contains(cb, product.get("name"), search),
                    contains(cb, product.get("code"), search),
                )
        } else {
            filter.productName?.takeIf(String::isNotBlank)?.let {
                predicates += contains(cb, product.get("name"), it)
            }

            filter.productCommercialCode?.takeIf(String::isNotBlank)?.let {
                predicates += contains(cb, product.get("code"), it)
            }
        }

        filter.color?.takeIf(String::isNotBlank)?.let {
            predicates += contains(cb, item.get("color"), it)
        }

        filter.brand?.takeIf(String::isNotBlank)?.let {
            predicates += contains(cb, item.get("brand"), it)
        }

        filter.model?.takeIf(String::isNotBlank)?.let {
            predicates += contains(cb, item.get("model"), it)
        }

        filter.size?.takeIf(String::isNotBlank)?.let {
            predicates += contains(cb, item.get("size"), it)
        }

        filter.material?.takeIf(String::isNotBlank)?.let {
            predicates += contains(cb, item.get("material"), it)
        }

        subquery
            .select(cb.literal(1))
            .where(*predicates.toTypedArray())

        return cb.exists(subquery)
    }

    private fun percentageRange(
        predicates: MutableList<Predicate>,
        root: Root<SaleEntity>,
        cb: CriteriaBuilder,
        field: String,
        from: BigDecimal?,
        to: BigDecimal?,
    ) {
        from?.let {
            predicates +=
                cb.greaterThanOrEqualTo(
                    root.get(field),
                    it,
                )
        }

        to?.let {
            predicates +=
                cb.lessThanOrEqualTo(
                    root.get(field),
                    it,
                )
        }
    }

    private fun remainingAmount(
        root: Root<SaleEntity>,
        cb: CriteriaBuilder,
    ): Expression<BigDecimal> {
        val paidAmount =
            cb.function(
                "round",
                BigDecimal::class.java,
                cb.quot(
                    cb.prod(
                        root.get<BigDecimal>("totalAmount"),
                        root.get<BigDecimal>("paidPercentage"),
                    ),
                    HUNDRED,
                ),
                cb.literal(2),
            )

        return cb.diff(
            root.get<BigDecimal>("totalAmount"),
            paidAmount,
        )
    }

    private fun contains(
        cb: CriteriaBuilder,
        path: Expression<String>,
        value: String,
    ): Predicate =
        cb.like(
            cb.lower(path),
            "%${escapeLike(value.trim().lowercase())}%",
            '\\',
        )

    private fun digitsOnly(
        cb: CriteriaBuilder,
        path: Expression<String>,
    ): Expression<String> =
        cb.function(
            "regexp_replace",
            String::class.java,
            cb.coalesce(path, ""),
            cb.literal("[^0-9]"),
            cb.literal(""),
            cb.literal("g"),
        )

    private fun normalizeDigits(value: String): String = value.filter(Char::isDigit)

    private fun escapeLike(value: String): String =
        value
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
}
