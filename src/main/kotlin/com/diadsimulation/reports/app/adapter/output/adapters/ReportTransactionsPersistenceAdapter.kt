package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.core.ports.output.GetReportTransactionsOutputPort
import com.diadsimulation.movements.app.adapter.output.persistence.entities.MovementEntity
import com.diadsimulation.movements.core.domain.enums.MovementDirection.IN
import com.diadsimulation.movements.core.domain.enums.MovementDirection.OUT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.PAYMENT
import com.diadsimulation.movements.core.domain.enums.MovementOperation.REFUND
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.EXPENSE
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.SALE
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.ENTRY
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.EXIT
import com.diadsimulation.reports.core.domain.models.ReportTransaction
import com.diadsimulation.reports.core.domain.models.ReportTransactions
import jakarta.persistence.EntityManager
import java.math.BigDecimal.ZERO
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

        val movements =
            entityManager
                .createQuery(
                    """
                    SELECT m
                    FROM MovementEntity m
                    WHERE m.createdAt >= :start
                      AND m.createdAt < :end
                      AND m.operation IN :operations
                      AND m.direction IS NOT NULL
                    ORDER BY m.createdAt DESC, m.id
                    """.trimIndent(),
                    MovementEntity::class.java,
                ).setParameter("start", start)
                .setParameter("end", endExclusive)
                .setParameter(
                    "operations",
                    listOf(PAYMENT, REFUND),
                ).resultList

        val transactions =
            movements.map { movement ->
                val type =
                    when (movement.direction) {
                        IN -> ENTRY
                        OUT -> EXIT
                        null -> error("Movimentação sem direção financeira.")
                    }

                ReportTransaction(
                    id = movement.id,
                    type = type,
                    description =
                        movement.description
                            ?: when (movement.origin) {
                                SALE -> "Movimentação de venda"
                                EXPENSE -> "Movimentação de despesa"
                            },
                    referenceDate =
                        movement.createdAt
                            .atZoneSameInstant(zone)
                            .toLocalDate(),
                    amount = movement.amount,
                )
            }

        val totalEntries =
            transactions
                .filter { it.type == ENTRY }
                .fold(ZERO) { total, transaction ->
                    total.add(transaction.amount)
                }.setScale(2, HALF_UP)

        val totalExits =
            transactions
                .filter { it.type == EXIT }
                .fold(ZERO) { total, transaction ->
                    total.add(transaction.amount)
                }.setScale(2, HALF_UP)

        return ReportTransactions(
            totalEntries = totalEntries,
            totalExits = totalExits,
            balance = totalEntries.subtract(totalExits),
            transactions = transactions,
        )
    }
}
