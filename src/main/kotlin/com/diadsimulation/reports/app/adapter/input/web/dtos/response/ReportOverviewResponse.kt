package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import java.math.BigDecimal

data class ReportOverviewResponse(
    val expectedRevenue: BigDecimal,
    val receivedRevenue: BigDecimal,
    val outstandingRevenue: BigDecimal,
    val expectedExpenses: BigDecimal,
    val paidExpenses: BigDecimal,
    val outstandingExpenses: BigDecimal,
    val expectedProfit: BigDecimal,
    val realizedProfit: BigDecimal,
)
