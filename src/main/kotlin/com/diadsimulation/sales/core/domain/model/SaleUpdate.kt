package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.utils.UpdateField
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class SaleUpdate(
    val customerId: UUID? = null,
    val items: List<SaleItem>? = null,
    val deliveryDeadline: UpdateField<LocalDate?> = UpdateField.Unchanged,
    val totalAmount: BigDecimal? = null,
    val downPaymentPercentage: BigDecimal? = null,
    val paidPercentage: BigDecimal? = null,
    val paymentMethod: PaymentMethod? = null,
    val observation: UpdateField<String?> = UpdateField.Unchanged,
    val delivered: Boolean? = null,
)
