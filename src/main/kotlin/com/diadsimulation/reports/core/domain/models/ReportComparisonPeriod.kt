package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal
import java.time.LocalDate

data class ReportComparisonPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val revenue: BigDecimal,
    val expenses: BigDecimal,
    val profit: BigDecimal,
)
