package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.CANCELLED
import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementDirection.IN
import com.diadsimulation.movements.core.domain.enums.MovementDirection.OUT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.PAYMENT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.REFUND
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.EXPENSE
import com.diadsimulation.reports.core.domain.models.ExpensesReport
import com.diadsimulation.reports.core.ports.output.GetExpensesReportOutputPort
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.ZoneId

class ExpensesReportPersistenceAdapter(
    private val entityManager: EntityManager,
) : GetExpensesReportOutputPort {
    override fun findReport(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ExpensesReport {
        val zone = ZoneId.of("America/Sao_Paulo")
        val start = startDate.atStartOfDay(zone).toOffsetDateTime()
        val endExclusive = endDate.plusDays(1).atStartOfDay(zone).toOffsetDateTime()

        val expenses =
            entityManager
                .createQuery(
                    """
                    SELECT e.value, e.paidAmount, e.status
                    FROM ExpenseEntity e
                    WHERE e.expenseDate >= :startDate
                      AND e.expenseDate <= :endDate
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .resultList

        val totalExpenses = expenses.size.toLong()

        var expectedAmount = ZERO
        var outstandingAmount = ZERO
        var pendingExpenses = 0L

        expenses.forEach { expense ->
            val value = expense[0] as BigDecimal
            val paid = expense[1] as BigDecimal
            val status = expense[2] as ExpenseStatus

            if (status != CANCELLED) {
                expectedAmount = expectedAmount.add(value)

                val pending = value.subtract(paid)

                if (pending > ZERO) {
                    outstandingAmount = outstandingAmount.add(pending)
                    pendingExpenses++
                }
            } else {
                expectedAmount = expectedAmount.add(paid)
            }
        }

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
                ).setParameter("origin", EXPENSE)
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

        val paidAmount =
            total(OUT)
                .subtract(total(IN))
                .setScale(2, HALF_UP)

        val paidPercentage =
            if (expectedAmount > ZERO) {
                paidAmount
                    .multiply(BigDecimal("100"))
                    .divide(expectedAmount, 2, HALF_UP)
            } else {
                ZERO.setScale(2, HALF_UP)
            }

        return ExpensesReport(
            totalExpenses = totalExpenses,
            expectedAmount = expectedAmount.setScale(2, HALF_UP),
            paidAmount = paidAmount,
            outstandingAmount = outstandingAmount.setScale(2, HALF_UP),
            paidPercentage = paidPercentage,
            pendingExpenses = pendingExpenses,
        )
    }
}
