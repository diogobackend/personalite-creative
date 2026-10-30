package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

import java.math.BigDecimal

data class SaleFinancialSummaryResponse(
    val totalSales: Long,
    val totalAmount: BigDecimal,
    val totalDownPayment: BigDecimal,
    val totalReceived: BigDecimal,
    val totalRemaining: BigDecimal,
    val unpaidSales: Long,
    val partiallyPaidSales: Long,
    val paidSales: Long,
    val overdueSales: Long,
    val dueTodaySales: Long,
    val upcomingSales: Long,
)
