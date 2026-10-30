package com.diadsimulation.sales.core.domain.model

import java.math.BigDecimal

data class SaleFinancialSummary(
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
