package com.diadsimulation.sales.app.adapter.output.persistence.entity

import com.diadsimulation.sales.core.domain.enums.SalePaymentType
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
@Table(name = "sale_payments")
class SalePaymentEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "sale_id", nullable = false)
    val saleId: UUID,
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    val amount: BigDecimal,
    @Enumerated(STRING)
    @Column(name = "type", nullable = false, length = 30)
    val type: SalePaymentType,
    @Column(name = "paid_at", nullable = false)
    val paidAt: OffsetDateTime,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
)
