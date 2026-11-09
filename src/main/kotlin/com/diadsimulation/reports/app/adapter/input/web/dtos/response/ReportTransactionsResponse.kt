package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import java.math.BigDecimal

data class ReportTransactionsResponse(
    val totalEntries: BigDecimal,
    val totalExits: BigDecimal,
    val balance: BigDecimal,
    val transactions: List<ReportTransactionResponse>,
)
