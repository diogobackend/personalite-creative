package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.enums.SalePaymentType
import com.diadsimulation.sales.core.domain.utils.UpdateField
import com.diadsimulation.sales.core.domain.utils.UpdateField.Unchanged
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class SaleUpdate(
    val customerId: UUID? = null,
    val items: List<SaleItem>? = null,
    val deliveryDeadline: UpdateField<LocalDate?> = Unchanged,
    val totalAmount: BigDecimal? = null,
    val downPaymentPercentage: BigDecimal? = null,
    val receivedAmount: BigDecimal? = null,
    val paymentMethod: PaymentMethod? = null,
    val observation: UpdateField<String?> = Unchanged,
    val delivered: Boolean? = null,
    val financialOperation: SalePaymentType? = null,
)
