package com.diadsimulation.expenses.core.domain.model

import com.diadsimulation.expenses.core.domain.enums.ExpensePaymentType
import com.diadsimulation.expenses.core.domain.enums.ExpensePaymentType.PAYMENT
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.UUID

data class ExpensePayment(
    val id: UUID = UUID.randomUUID(),
    val expenseId: UUID,
    val amount: BigDecimal,
    val type: ExpensePaymentType = PAYMENT,
    val paidAt: OffsetDateTime = OffsetDateTime.now(),
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    init {
        require(amount > ZERO) {
            "O valor da movimentação deve ser maior que zero."
        }
        require(amount.scale() <= 2) {
            "O valor deve possuir no máximo duas casas decimais."
        }
    }
}
