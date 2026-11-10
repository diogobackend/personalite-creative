package com.diadsimulation.expenses.core.domain.model

import java.math.BigDecimal

data class ReportCashFlow(
    val totalEntries: BigDecimal,
    val totalExits: BigDecimal,
    val balance: BigDecimal,
    val days: List<ReportCashFlowDay>,
)
