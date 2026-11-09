package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class ReportOverview(
    val expectedRevenue: BigDecimal,
    val receivedRevenue: BigDecimal,
    val outstandingRevenue: BigDecimal,
    val expectedExpenses: BigDecimal,
    val paidExpenses: BigDecimal,
    val outstandingExpenses: BigDecimal,
    val expectedProfit: BigDecimal,
    val realizedProfit: BigDecimal,
)
