package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseEntity
import com.diadsimulation.expenses.core.ports.output.GetReportTransactionsOutputPort
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.ENTRY
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.EXIT
import com.diadsimulation.reports.core.domain.models.ReportTransaction
import com.diadsimulation.reports.core.domain.models.ReportTransactions
import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.ZoneId

class ReportTransactionsPersistenceAdapter(
    private val entityManager: EntityManager,
) : GetReportTransactionsOutputPort {
    override fun findTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportTransactions {
        val zone = ZoneId.of("America/Sao_Paulo")
        val start = startDate.atStartOfDay(zone).toOffsetDateTime()
        val endExclusive = endDate.plusDays(1).atStartOfDay(zone).toOffsetDateTime()

        val sales =
            entityManager
                .createQuery(
                    """
                    SELECT s
                    FROM SaleEntity s
                    WHERE s.createdAt >= :start
                      AND s.createdAt < :end
                    """.trimIndent(),
                    SaleEntity::class.java,
                ).setParameter("start", start)
                .setParameter("end", endExclusive)
                .resultList

        val expenses =
            entityManager
                .createQuery(
                    """
                    SELECT e
                    FROM ExpenseEntity e
                    WHERE e.expenseDate BETWEEN :startDate AND :endDate
                      AND e.paidAmount > 0
                    """.trimIndent(),
                    ExpenseEntity::class.java,
                ).setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .resultList

        val entries =
            sales
                .map { sale ->
                    val amount =
                        sale.totalAmount
                            .multiply(sale.paidPercentage)
                            .divide(HUNDRED, 2, HALF_UP)

                    ReportTransaction(
                        id = sale.id,
                        type = ENTRY,
                        description = "Recebimento de venda",
                        referenceDate = sale.createdAt.atZoneSameInstant(zone).toLocalDate(),
                        amount = amount,
                    )
                }.filter { it.amount > BigDecimal.ZERO }

        val exits =
            expenses.map { expense ->
                ReportTransaction(
                    id = expense.id,
                    type = EXIT,
                    description = expense.name,
                    referenceDate = expense.expenseDate,
                    amount = expense.paidAmount,
                )
            }

        val transactions =
            (entries + exits).sortedWith(
                compareByDescending<ReportTransaction> { it.referenceDate }
                    .thenBy { it.id },
            )

        val totalEntries =
            entries
                .fold(BigDecimal.ZERO) { total, transaction ->
                    total.add(transaction.amount)
                }.setScale(2, HALF_UP)

        val totalExits =
            exits
                .fold(BigDecimal.ZERO) { total, transaction ->
                    total.add(transaction.amount)
                }.setScale(2, HALF_UP)

        return ReportTransactions(
            totalEntries = totalEntries,
            totalExits = totalExits,
            balance = totalEntries.subtract(totalExits),
            transactions = transactions,
        )
    }

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
