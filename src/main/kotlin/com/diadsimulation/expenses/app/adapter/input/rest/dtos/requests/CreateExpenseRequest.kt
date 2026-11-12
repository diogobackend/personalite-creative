package com.diadsimulation.expenses.app.adapter.input.rest.dtos.requests

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Collections.emptyList
import java.util.UUID

data class CreateExpenseRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val name: String,
    val inventoryId: UUID? = null,
    val description: String? = null,
    @field:NotNull
    @field:DecimalMin("0.01")
    val value: BigDecimal,
    @field:DecimalMin("0.00")
    val paidAmount: BigDecimal = ZERO,
    val category: String,
    @field:NotNull
    val categoryId: UUID,
    val allocationType: ExpenseAllocationType = GENERAL,
    val quantity: Int? = null,
    @field:Valid
    val products: List<ExpenseProductRequest> = emptyList(),
    val expenseDate: LocalDate,
    @field:DateTimeFormat(iso = DATE_TIME)
    val createdAt: OffsetDateTime? = null,
)
