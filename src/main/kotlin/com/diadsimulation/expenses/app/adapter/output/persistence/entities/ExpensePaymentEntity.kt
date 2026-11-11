package com.diadsimulation.expenses.app.adapter.output.persistence.entities

import com.diadsimulation.expenses.core.domain.enums.ExpensePaymentType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "expense_payments")
class ExpensePaymentEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "expense_id", nullable = false)
    val expenseId: UUID,
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    val amount: BigDecimal,
    @Enumerated(STRING)
    @Column(name = "type", nullable = false, length = 40)
    val type: ExpensePaymentType,
    @Column(name = "paid_at", nullable = false)
    val paidAt: OffsetDateTime,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
)
