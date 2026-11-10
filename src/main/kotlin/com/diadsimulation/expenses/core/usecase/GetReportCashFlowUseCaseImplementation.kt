package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.model.ReportCashFlow
import com.diadsimulation.expenses.core.domain.model.ReportCashFlowDay
import com.diadsimulation.expenses.core.ports.input.ReportCashFlowInputPort
import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.ENTRY
import com.diadsimulation.reports.core.domain.enums.ReportTransactionType.EXIT
import java.math.BigDecimal
import java.time.LocalDate

class GetReportCashFlowUseCaseImplementation(
    private val reportTransactionsInputPort: ReportTransactionsInputPort,
) : ReportCashFlowInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportCashFlow {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        val report =
            reportTransactionsInputPort.execute(
                startDate = startDate,
                endDate = endDate,
            )

        val days =
            report.transactions
                .groupBy { it.referenceDate }
                .toSortedMap()
                .map { (date, transactions) ->
                    val entries =
                        transactions
                            .filter { it.type == ENTRY }
                            .fold(BigDecimal.ZERO) { total, transaction ->
                                total + transaction.amount
                            }

                    val exits =
                        transactions
                            .filter { it.type == EXIT }
                            .fold(BigDecimal.ZERO) { total, transaction ->
                                total + transaction.amount
                            }

                    ReportCashFlowDay(
                        date = date,
                        entries = entries,
                        exits = exits,
                        balance = entries - exits,
                    )
                }

        return ReportCashFlow(
            totalEntries = report.totalEntries,
            totalExits = report.totalExits,
            balance = report.balance,
            days = days,
        )
    }
}
