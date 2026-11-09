package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.CANCELLED
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
                        COALESCE(SUM(s.totalAmount * s.paidPercentage / 100), 0)
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
                        COALESCE(SUM(e.paidAmount), 0),
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
                ).setParameter(
                    "cancelled",
                    CANCELLED,
                ).setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .singleResult

        val expectedRevenue = (revenue[0] as Number).toBigDecimal()
        val receivedRevenue = (revenue[1] as Number).toBigDecimal()
        val expectedExpenses = (expenses[0] as Number).toBigDecimal()
        val paidExpenses = (expenses[1] as Number).toBigDecimal()
        val outstandingExpenses = (expenses[2] as Number).toBigDecimal()

        return ReportOverview(
            expectedRevenue = expectedRevenue,
            receivedRevenue = receivedRevenue,
            outstandingRevenue = expectedRevenue.subtract(receivedRevenue),
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
