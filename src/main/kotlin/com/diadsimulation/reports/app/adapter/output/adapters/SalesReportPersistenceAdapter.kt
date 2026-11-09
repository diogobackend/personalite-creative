package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.reports.core.domain.models.SalesReport
import com.diadsimulation.reports.core.ports.output.GetSalesReportOutputPort
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.ZoneId

class SalesReportPersistenceAdapter(
    private val entityManager: EntityManager,
) : GetSalesReportOutputPort {
    override fun findReport(
        startDate: LocalDate,
        endDate: LocalDate,
    ): SalesReport {
        val zoneId = ZoneId.of("America/Sao_Paulo")

        val start = startDate.atStartOfDay(zoneId).toInstant()
        val end = endDate.plusDays(1).atStartOfDay(zoneId).toInstant()

        val sales =
            entityManager
                .createQuery(
                    """
                    SELECT s.totalAmount, s.paidPercentage
                    FROM SaleEntity s
                    WHERE s.createdAt >= :start
                      AND s.createdAt < :end
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("start", start)
                .setParameter("end", end)
                .resultList

        val totalSales = sales.size.toLong()

        var totalRevenue = ZERO
        var receivedAmount = ZERO

        sales.forEach { sale ->
            val amount = sale[0] as BigDecimal
            val paidPercentage = sale[1] as BigDecimal

            totalRevenue = totalRevenue.add(amount)

            receivedAmount =
                receivedAmount.add(
                    amount
                        .multiply(paidPercentage)
                        .divide(
                            BigDecimal("100"),
                            2,
                            HALF_UP,
                        ),
                )
        }

        val outstandingAmount = totalRevenue.subtract(receivedAmount)

        val averageTicket =
            if (totalSales > 0) {
                totalRevenue.divide(
                    BigDecimal.valueOf(totalSales),
                    2,
                    HALF_UP,
                )
            } else {
                ZERO
            }

        val receivedPercentage =
            if (totalRevenue.compareTo(ZERO) > 0) {
                receivedAmount
                    .multiply(BigDecimal("100"))
                    .divide(
                        totalRevenue,
                        2,
                        HALF_UP,
                    )
            } else {
                ZERO
            }

        return SalesReport(
            totalRevenue = totalRevenue,
            totalSales = totalSales,
            averageTicket = averageTicket,
            receivedAmount = receivedAmount,
            outstandingAmount = outstandingAmount,
            receivedPercentage = receivedPercentage,
        )
    }
}
