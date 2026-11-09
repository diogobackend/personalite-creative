package com.diadsimulation.expenses.core.ports.output

import com.diadsimulation.reports.core.domain.models.ReportTransactions
import java.time.LocalDate

interface GetReportTransactionsOutputPort {
    fun findTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportTransactions
}
