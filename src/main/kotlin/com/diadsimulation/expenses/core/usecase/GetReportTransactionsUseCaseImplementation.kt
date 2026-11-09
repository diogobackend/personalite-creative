package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.expenses.core.ports.output.GetReportTransactionsOutputPort
import com.diadsimulation.reports.core.domain.models.ReportTransactions
import java.time.LocalDate

class GetReportTransactionsUseCaseImplementation(
    private val getReportTransactionsOutputPort: GetReportTransactionsOutputPort,
) : ReportTransactionsInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportTransactions {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        return getReportTransactionsOutputPort.findTransactions(
            startDate = startDate,
            endDate = endDate,
        )
    }
}
