package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.models.SalesReport
import java.time.LocalDate

interface GetSalesReportInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): SalesReport
}
