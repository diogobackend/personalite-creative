package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal
import java.time.YearMonth

data class ReportRevenueEvolutionMonth(
    val month: YearMonth,
    val revenue: BigDecimal,
)
