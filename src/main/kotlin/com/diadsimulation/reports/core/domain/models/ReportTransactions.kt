package com.diadsimulation.reports.core.domain.models

import java.math.BigDecimal

data class ReportTransactions(
    val totalEntries: BigDecimal,
    val totalExits: BigDecimal,
    val balance: BigDecimal,
    val transactions: List<ReportTransaction>,
)
