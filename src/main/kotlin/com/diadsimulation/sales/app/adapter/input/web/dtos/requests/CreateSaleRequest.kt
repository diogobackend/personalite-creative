package com.diadsimulation.sales.app.adapter.input.web.dtos.requests

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreateSaleRequest(
    val customerId: UUID,
    val deliveryDeadline: LocalDate,
    val totalAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val paidPercentage: BigDecimal,
    val paymentMethod: PaymentMethod,
    val items: List<CreateSaleItemRequest>,
)
