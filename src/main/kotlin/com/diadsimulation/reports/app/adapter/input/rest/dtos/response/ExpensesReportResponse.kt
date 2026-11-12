package com.diadsimulation.reports.app.adapter.input.rest.dtos.response

import java.math.BigDecimal

data class ExpensesReportResponse(
    val totalExpenses: Long,
    val expectedAmount: BigDecimal,
    val paidAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val paidPercentage: BigDecimal,
    val pendingExpenses: Long,
)
