package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import com.diadsimulation.reports.core.domain.models.ReportRevenueEvolutionMonth
import java.math.BigDecimal
import java.time.YearMonth

data class ReportRevenueEvolutionMonthResponse(
    val month: YearMonth,
    val revenue: BigDecimal,
) {
    companion object {
        fun from(domain: ReportRevenueEvolutionMonth): ReportRevenueEvolutionMonthResponse =
            ReportRevenueEvolutionMonthResponse(
                month = domain.month,
                revenue = domain.revenue,
            )
    }
}
