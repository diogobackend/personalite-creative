package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.expenses.core.domain.model.ReportCashFlow
import java.time.LocalDate

interface ReportCashFlowInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportCashFlow
}
