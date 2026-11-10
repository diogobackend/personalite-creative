package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.models.ReportComparison
import java.time.LocalDate

interface ReportComparisonInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
        previousStartDate: LocalDate,
        previousEndDate: LocalDate,
    ): ReportComparison
}
