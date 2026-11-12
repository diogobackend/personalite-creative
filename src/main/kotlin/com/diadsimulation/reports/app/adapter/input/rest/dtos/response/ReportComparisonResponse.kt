package com.diadsimulation.reports.app.adapter.input.rest.dtos.response

import com.diadsimulation.reports.core.domain.models.ReportComparison
import com.diadsimulation.reports.core.domain.models.ReportComparisonPeriod
import com.diadsimulation.reports.core.domain.models.ReportComparisonVariations
import java.math.BigDecimal
import java.time.LocalDate

data class ReportComparisonResponse(
    val currentPeriod: ReportComparisonPeriodResponse,
    val previousPeriod: ReportComparisonPeriodResponse,
    val variations: ReportComparisonVariationsResponse,
) {
    companion object {
        fun from(domain: ReportComparison): ReportComparisonResponse =
            ReportComparisonResponse(
                currentPeriod = ReportComparisonPeriodResponse.from(domain.currentPeriod),
                previousPeriod = ReportComparisonPeriodResponse.from(domain.previousPeriod),
                variations = ReportComparisonVariationsResponse.from(domain.variations),
            )
    }
}

data class ReportComparisonPeriodResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val revenue: BigDecimal,
    val expenses: BigDecimal,
    val profit: BigDecimal,
) {
    companion object {
        fun from(domain: ReportComparisonPeriod): ReportComparisonPeriodResponse =
            ReportComparisonPeriodResponse(
                startDate = domain.startDate,
                endDate = domain.endDate,
                revenue = domain.revenue,
                expenses = domain.expenses,
                profit = domain.profit,
            )
    }
}

data class ReportComparisonVariationsResponse(
    val revenuePercentage: BigDecimal?,
    val expensesPercentage: BigDecimal?,
    val profitPercentage: BigDecimal?,
) {
    companion object {
        fun from(domain: ReportComparisonVariations): ReportComparisonVariationsResponse =
            ReportComparisonVariationsResponse(
                revenuePercentage = domain.revenuePercentage,
                expensesPercentage = domain.expensesPercentage,
                profitPercentage = domain.profitPercentage,
            )
    }
}
