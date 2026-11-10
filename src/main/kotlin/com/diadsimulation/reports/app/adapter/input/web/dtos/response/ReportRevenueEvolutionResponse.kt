package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import com.diadsimulation.reports.core.domain.models.ReportRevenueEvolution
import java.math.BigDecimal

data class ReportRevenueEvolutionResponse(
    val totalRevenue: BigDecimal,
    val months: List<ReportRevenueEvolutionMonthResponse>,
) {
    companion object {
        fun from(domain: ReportRevenueEvolution): ReportRevenueEvolutionResponse =
            ReportRevenueEvolutionResponse(
                totalRevenue = domain.totalRevenue,
                months = domain.months.map(ReportRevenueEvolutionMonthResponse::from),
            )
    }
}
