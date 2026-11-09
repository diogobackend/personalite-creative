package com.diadsimulation.reports.core.ports.output

import com.diadsimulation.reports.core.domain.models.SalesReport
import java.time.LocalDate

interface GetSalesReportOutputPort {
    fun findReport(
        startDate: LocalDate,
        endDate: LocalDate,
    ): SalesReport
}
