package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.models.ReportOverview
import java.time.LocalDate

interface GetReportOverviewInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportOverview
}
