package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.models.SalesReport
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
import com.diadsimulation.reports.core.ports.output.GetSalesReportOutputPort
import java.time.LocalDate

class GetSalesReportUseCaseImplementation(
    private val getSalesReportOutputPort: GetSalesReportOutputPort,
) : GetSalesReportInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): SalesReport {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        return getSalesReportOutputPort.findReport(
            startDate = startDate,
            endDate = endDate,
        )
    }
}
