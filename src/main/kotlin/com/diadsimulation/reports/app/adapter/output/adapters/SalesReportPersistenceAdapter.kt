package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementDirection.IN
import com.diadsimulation.movements.core.domain.enums.MovementDirection.OUT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.PAYMENT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.REFUND
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.SALE
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
        val zone = ZoneId.of("America/Sao_Paulo")
        val start = startDate.atStartOfDay(zone).toOffsetDateTime()
        val endExclusive = endDate.plusDays(1).atStartOfDay(zone).toOffsetDateTime()

        val sales =
            entityManager
                .createQuery(
                    """
                    SELECT
                        COUNT(s.id),
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

        val movements =
            entityManager
                .createQuery(
                    """
                    SELECT m.direction, COALESCE(SUM(m.amount), 0)
                    FROM MovementEntity m
                    WHERE m.origin = :origin
                      AND m.operation IN :operations
                      AND m.createdAt >= :start
                      AND m.createdAt < :end
                      AND m.direction IS NOT NULL
                    GROUP BY m.direction
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("origin", SALE)
                .setParameter(
                    "operations",
                    listOf(PAYMENT, REFUND),
                ).setParameter("start", start)
                .setParameter("end", endExclusive)
                .resultList

        fun total(direction: MovementDirection): BigDecimal =
            movements
                .filter { it[0] == direction }
                .fold(ZERO) { sum, row ->
                    sum.add(row[1] as BigDecimal)
                }

        val totalSales = (sales[0] as Number).toLong()
        val totalRevenue = (sales[1] as BigDecimal).setScale(2, HALF_UP)
        val outstandingAmount = (sales[2] as BigDecimal).setScale(2, HALF_UP)

        val receivedAmount =
            total(IN)
                .subtract(total(OUT))
                .setScale(2, HALF_UP)

        val averageTicket =
            if (totalSales > 0) {
                totalRevenue.divide(
                    BigDecimal.valueOf(totalSales),
                    2,
                    HALF_UP,
                )
            } else {
                ZERO.setScale(2, HALF_UP)
            }

        val receivedPercentage =
            if (totalRevenue > ZERO) {
                receivedAmount
                    .multiply(BigDecimal("100"))
                    .divide(totalRevenue, 2, HALF_UP)
            } else {
                ZERO.setScale(2, HALF_UP)
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
