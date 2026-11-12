package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.CANCELLED
import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementDirection.IN
import com.diadsimulation.movements.core.domain.enums.MovementDirection.OUT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.PAYMENT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.REFUND
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.EXPENSE
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.SALE
import com.diadsimulation.reports.core.domain.models.ReportOverview
import com.diadsimulation.reports.core.ports.output.GetReportOverviewOutputPort
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.ZoneId

class ReportOverviewPersistenceAdapter(
    private val entityManager: EntityManager,
) : GetReportOverviewOutputPort {
    override fun findOverview(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportOverview {
        val zone = ZoneId.of("America/Sao_Paulo")
        val start = startDate.atStartOfDay(zone).toOffsetDateTime()
        val endExclusive = endDate.plusDays(1).atStartOfDay(zone).toOffsetDateTime()

        val revenue =
            entityManager
                .createQuery(
                    """
                    SELECT
                        COALESCE(SUM(s.totalAmount), 0),
                        COALESCE(SUM(s.totalAmount - s.receivedAmount), 0)
                    FROM SaleEntity s
                    WHERE s.createdAt >= :start
                      AND s.createdAt < :end
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("start", start)
                .setParameter("end", endExclusive)
                .singleResult

        val expenses =
            entityManager
                .createQuery(
                    """
                    SELECT
                        COALESCE(SUM(
                            CASE
                                WHEN e.status <> :cancelled THEN e.value
                                ELSE e.paidAmount
                            END
                        ), 0),
                        COALESCE(SUM(
                            CASE
                                WHEN e.status <> :cancelled
                                THEN e.value - e.paidAmount
                                ELSE 0
                            END
                        ), 0)
                    FROM ExpenseEntity e
                    WHERE e.expenseDate BETWEEN :startDate AND :endDate
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("cancelled", CANCELLED)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .singleResult

        val movements =
            entityManager
                .createQuery(
                    """
                    SELECT m.origin, m.direction, COALESCE(SUM(m.amount), 0)
                    FROM MovementEntity m
                    WHERE m.createdAt >= :start
                      AND m.createdAt < :end
                      AND m.operation IN :operations
                      AND m.direction IS NOT NULL
                    GROUP BY m.origin, m.direction
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("start", start)
                .setParameter("end", endExclusive)
                .setParameter(
                    "operations",
                    listOf(PAYMENT, REFUND),
                ).resultList

        fun total(
            origin: MovementOrigin,
            direction: MovementDirection,
        ): BigDecimal =
            movements
                .filter { it[0] == origin && it[1] == direction }
                .fold(BigDecimal.ZERO) { sum, row ->
                    sum.add((row[2] as Number).toBigDecimal())
                }

        val expectedRevenue = (revenue[0] as Number).toBigDecimal()
        val outstandingRevenue = (revenue[1] as Number).toBigDecimal()

        val expectedExpenses = (expenses[0] as Number).toBigDecimal()
        val outstandingExpenses = (expenses[1] as Number).toBigDecimal()

        val receivedRevenue =
            total(SALE, IN)
                .subtract(total(SALE, OUT))
                .setScale(2, HALF_UP)

        val paidExpenses =
            total(EXPENSE, OUT)
                .subtract(total(EXPENSE, IN))
                .setScale(2, HALF_UP)

        return ReportOverview(
            expectedRevenue = expectedRevenue,
            receivedRevenue = receivedRevenue,
            outstandingRevenue = outstandingRevenue,
            expectedExpenses = expectedExpenses,
            paidExpenses = paidExpenses,
            outstandingExpenses = outstandingExpenses,
            expectedProfit = expectedRevenue.subtract(expectedExpenses),
            realizedProfit = receivedRevenue.subtract(paidExpenses),
        )
    }

    private fun Number.toBigDecimal(): BigDecimal =
        when (this) {
            is BigDecimal -> this
            else -> BigDecimal(toString())
        }.setScale(2, HALF_UP)
}
