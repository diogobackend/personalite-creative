package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class ReportComparisonVariations(
    val revenuePercentage: BigDecimal?,
    val expensesPercentage: BigDecimal?,
    val profitPercentage: BigDecimal?,
)
