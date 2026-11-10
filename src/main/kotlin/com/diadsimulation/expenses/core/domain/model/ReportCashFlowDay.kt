package com.diadsimulation.expenses.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class ReportCashFlowDay(
    val date: LocalDate,
    val entries: BigDecimal,
    val exits: BigDecimal,
    val balance: BigDecimal,
)
