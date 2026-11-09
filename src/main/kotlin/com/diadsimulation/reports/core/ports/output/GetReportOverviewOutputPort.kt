package com.diadsimulation.reports.core.ports.output

import com.diadsimulation.reports.core.domain.models.ReportOverview
import java.time.LocalDate

interface GetReportOverviewOutputPort {
    fun findOverview(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportOverview
}
