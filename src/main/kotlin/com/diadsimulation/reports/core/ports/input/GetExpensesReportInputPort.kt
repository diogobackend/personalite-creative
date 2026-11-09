package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.models.ExpensesReport
import java.time.LocalDate

interface GetExpensesReportInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ExpensesReport
}
