package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.reports.core.domain.models.ReportTransactions
import java.time.LocalDate

interface ReportTransactionsInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportTransactions
}
