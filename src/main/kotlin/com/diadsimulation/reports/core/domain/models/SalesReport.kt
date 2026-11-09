package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class SalesReport(
    val totalRevenue: BigDecimal,
    val totalSales: Long,
    val averageTicket: BigDecimal,
    val receivedAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val receivedPercentage: BigDecimal,
)
