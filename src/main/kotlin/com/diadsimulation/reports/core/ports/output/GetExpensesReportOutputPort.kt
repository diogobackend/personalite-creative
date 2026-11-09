package com.diadsimulation.reports.core.ports.output

import com.diadsimulation.reports.core.domain.models.ExpensesReport
import java.time.LocalDate

interface GetExpensesReportOutputPort {
    fun findReport(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ExpensesReport
}
