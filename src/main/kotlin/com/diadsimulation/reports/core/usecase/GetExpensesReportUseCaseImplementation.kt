package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.models.ExpensesReport
import com.diadsimulation.reports.core.ports.input.GetExpensesReportInputPort
import com.diadsimulation.reports.core.ports.output.GetExpensesReportOutputPort
import java.time.LocalDate

class GetExpensesReportUseCaseImplementation(
    private val getExpensesReportOutputPort: GetExpensesReportOutputPort,
) : GetExpensesReportInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ExpensesReport {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        return getExpensesReportOutputPort.findReport(
            startDate = startDate,
            endDate = endDate,
        )
    }
}
