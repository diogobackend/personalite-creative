package com.diadsimulation.sales.app.adapter.output.persistence.repository

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import com.diadsimulation.sales.app.adapter.output.persistence.specifications.SaleSpecification
import com.diadsimulation.sales.core.domain.model.SaleFinancialSummary
import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Predicate
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class SaleFinancialSummaryRepository(
    private val entityManager: EntityManager,
) {
    fun summarize(
        filter: SaleSearchFilter,
        today: LocalDate = LocalDate.now(),
    ): SaleFinancialSummary {
        val cb = entityManager.criteriaBuilder
        val query = cb.createTupleQuery()
        val sale = query.from(SaleEntity::class.java)

        val paidPercentage = sale.get<BigDecimal>("paidPercentage")
        val totalAmount = sale.get<BigDecimal>("totalAmount")
        val deadline = sale.get<LocalDate>("deliveryDeadline")

        val downPaymentAmount =
            percentageAmount(
                cb,
                totalAmount,
                sale.get("downPaymentPercentage"),
            )

        val receivedAmount =
            percentageAmount(
                cb,
                totalAmount,
                paidPercentage,
            )

        val remainingAmount =
            cb.diff(
                totalAmount,
                receivedAmount,
            )

        fun countWhen(predicate: Predicate): Expression<Long> =
            cb.sum(
                cb
                    .selectCase<Long>()
                    .`when`(predicate, 1L)
                    .otherwise(0L),
            )

        query.multiselect(
            cb.count(sale).alias("totalSales"),
            cb.sum(totalAmount).alias("totalAmount"),
            cb.sum(downPaymentAmount).alias("totalDownPayment"),
            cb.sum(receivedAmount).alias("totalReceived"),
            cb.sum(remainingAmount).alias("totalRemaining"),
            countWhen(
                cb.equal(paidPercentage, BigDecimal.ZERO),
            ).alias("unpaidSales"),
            countWhen(
                cb.and(
                    cb.greaterThan(paidPercentage, BigDecimal.ZERO),
                    cb.lessThan(paidPercentage, HUNDRED),
                ),
            ).alias("partiallyPaidSales"),
            countWhen(
                cb.greaterThanOrEqualTo(paidPercentage, HUNDRED),
            ).alias("paidSales"),
            countWhen(
                cb.lessThan(deadline, today),
            ).alias("overdueSales"),
            countWhen(
                cb.equal(deadline, today),
            ).alias("dueTodaySales"),
            countWhen(
                cb.greaterThan(deadline, today),
            ).alias("upcomingSales"),
        )

        SaleSpecification
            .byFilter(filter, today)
            .toPredicate(sale, query, cb)
            ?.let(query::where)

        val result = entityManager.createQuery(query).singleResult

        return SaleFinancialSummary(
            totalSales = result.long("totalSales"),
            totalAmount = result.amount("totalAmount"),
            totalDownPayment = result.amount("totalDownPayment"),
            totalReceived = result.amount("totalReceived"),
            totalRemaining = result.amount("totalRemaining"),
            unpaidSales = result.long("unpaidSales"),
            partiallyPaidSales = result.long("partiallyPaidSales"),
            paidSales = result.long("paidSales"),
            overdueSales = result.long("overdueSales"),
            dueTodaySales = result.long("dueTodaySales"),
            upcomingSales = result.long("upcomingSales"),
        )
    }

    private fun percentageAmount(
        cb: CriteriaBuilder,
        totalAmount: Expression<BigDecimal>,
        percentage: Expression<BigDecimal>,
    ): Expression<BigDecimal> {
        val calculatedAmount =
            cb.toBigDecimal(
                cb.quot(
                    cb.prod(totalAmount, percentage),
                    HUNDRED,
                ),
            )

        return cb.function(
            "round",
            BigDecimal::class.java,
            calculatedAmount,
            cb.literal(2),
        )
    }

    private fun jakarta.persistence.Tuple.amount(alias: String): BigDecimal =
        (get(alias) as? BigDecimal ?: BigDecimal.ZERO)
            .setScale(2, java.math.RoundingMode.HALF_UP)

    private fun jakarta.persistence.Tuple.long(alias: String): Long = (get(alias) as? Number)?.toLong() ?: 0L

    companion object {
        private val HUNDRED = BigDecimal("100")
    }
}
