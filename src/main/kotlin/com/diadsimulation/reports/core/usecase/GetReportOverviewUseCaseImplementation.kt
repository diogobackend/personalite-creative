package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.models.ReportOverview
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.output.GetReportOverviewOutputPort
import java.time.LocalDate

class GetReportOverviewUseCaseImplementation(
    private val getReportOverviewOutputPort: GetReportOverviewOutputPort,
) : GetReportOverviewInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportOverview {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        return getReportOverviewOutputPort.findOverview(
            startDate = startDate,
            endDate = endDate,
        )
    }
}
