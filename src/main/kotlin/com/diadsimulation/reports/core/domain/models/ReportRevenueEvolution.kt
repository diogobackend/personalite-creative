package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class ReportRevenueEvolution(
    val totalRevenue: BigDecimal,
    val months: List<ReportRevenueEvolutionMonth>,
)
