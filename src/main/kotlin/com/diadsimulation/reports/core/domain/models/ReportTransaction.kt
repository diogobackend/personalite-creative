package com.diadsimulation.reports.core.domain.models

import com.diadsimulation.reports.core.domain.enums.ReportTransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class ReportTransaction(
    val id: UUID,
    val type: ReportTransactionType,
    val description: String,
    val referenceDate: LocalDate,
    val amount: BigDecimal,
)
