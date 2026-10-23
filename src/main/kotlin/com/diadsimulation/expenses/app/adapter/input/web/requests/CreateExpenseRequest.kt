package com.diadsimulation.expenses.app.adapter.input.web.requests

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PENDING
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Collections.emptyList

data class CreateExpenseRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val name: String,
    val description: String? = null,
    @field:NotNull
    @field:DecimalMin("0.01")
    val value: BigDecimal,
    val category: ExpenseCategory,
    val allocationType: ExpenseAllocationType = GENERAL,
    val status: ExpenseStatus = PENDING,
    val quantity: Int? = null,
    @field:Valid
    val products: List<ExpenseProductRequest> = emptyList(),
    val expenseDate: LocalDate,
    @field:DateTimeFormat(iso = DATE_TIME)
    val createdAt: OffsetDateTime? = null,
)
