package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import java.math.BigDecimal

data class SalesReportResponse(
    val totalRevenue: BigDecimal,
    val totalSales: Long,
    val averageTicket: BigDecimal,
    val receivedAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val receivedPercentage: BigDecimal,
)
