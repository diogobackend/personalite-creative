package com.diadsimulation.reports.core.domain.models

data class ReportComparison(
    val currentPeriod: ReportComparisonPeriod,
    val previousPeriod: ReportComparisonPeriod,
    val variations: ReportComparisonVariations,
)
