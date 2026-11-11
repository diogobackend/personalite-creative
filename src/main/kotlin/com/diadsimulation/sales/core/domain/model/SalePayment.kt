package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.SalePaymentType
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.CANCELLED
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.RECEIPT
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.UUID

data class SalePayment(
    val id: UUID = UUID.randomUUID(),
    val saleId: UUID,
    val amount: BigDecimal,
    val type: SalePaymentType = RECEIPT,
    val paidAt: OffsetDateTime = OffsetDateTime.now(),
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    init {
        require(amount >= ZERO) {
            "O valor da movimentação não pode ser negativo."
        }

        require(amount > ZERO || type == CANCELLED) {
            "O valor deve ser maior que zero, exceto para cancelamentos."
        }

        require(amount.scale() <= 2) {
            "O valor deve possuir no máximo duas casas decimais."
        }
    }
}
