package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class ExpensesReport(
    val totalExpenses: Long,
    val expectedAmount: BigDecimal,
    val paidAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val paidPercentage: BigDecimal,
    val pendingExpenses: Long,
)
