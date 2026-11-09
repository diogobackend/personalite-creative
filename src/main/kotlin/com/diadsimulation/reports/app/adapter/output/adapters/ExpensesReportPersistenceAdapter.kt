package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.reports.core.domain.models.ExpensesReport
import com.diadsimulation.reports.core.ports.output.GetExpensesReportOutputPort
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate

class ExpensesReportPersistenceAdapter(
    private val entityManager: EntityManager,
) : GetExpensesReportOutputPort {
    override fun findReport(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ExpensesReport {
        val expenses =
            entityManager
                .createQuery(
                    """
                    SELECT e.value, e.paidAmount
                    FROM ExpenseEntity e
                    WHERE e.expenseDate >= :startDate
                      AND e.expenseDate <= :endDate
                    """.trimIndent(),
                    Array<Any>::class.java,
                ).setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .resultList

        val totalExpenses = expenses.size.toLong()

        var expectedAmount = BigDecimal.ZERO
        var paidAmount = BigDecimal.ZERO
        var pendingExpenses = 0L

        expenses.forEach { expense ->
            val value = expense[0] as BigDecimal
            val paid = expense[1] as BigDecimal

            expectedAmount = expectedAmount.add(value)
            paidAmount = paidAmount.add(paid)

            if (paid < value) {
                pendingExpenses++
            }
        }

        val outstandingAmount = expectedAmount.subtract(paidAmount)

        val paidPercentage =
            if (expectedAmount > BigDecimal.ZERO) {
                paidAmount
                    .multiply(BigDecimal("100"))
                    .divide(expectedAmount, 2, HALF_UP)
            } else {
                BigDecimal.ZERO
            }

        return ExpensesReport(
            totalExpenses = totalExpenses,
            expectedAmount = expectedAmount,
            paidAmount = paidAmount,
            outstandingAmount = outstandingAmount,
            paidPercentage = paidPercentage,
            pendingExpenses = pendingExpenses,
        )
    }
}
