package com.diadsimulation.sales.app.adapter.input.web.dtos.requests

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.enums.SalePaymentType
import org.openapitools.jackson.nullable.JsonNullable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class UpdateSaleRequest(
    val customerId: UUID? = null,
    val items: List<UpdateSaleItemRequest>? = null,
    val deliveryDeadline: JsonNullable<LocalDate?> = JsonNullable.undefined(),
    val downPaymentPercentage: BigDecimal? = null,
    val receivedAmount: BigDecimal? = null,
    val paymentMethod: PaymentMethod? = null,
    val observation: JsonNullable<String?> = JsonNullable.undefined(),
    val delivered: Boolean? = null,
    val financialOperation: SalePaymentType? = null,
)
