package com.diadsimulation.sales.app.adapter.output.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "sales")
class SaleEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "customer_id", nullable = false)
    val customerId: UUID,
    @Column(name = "delivery_deadline")
    val deliveryDeadline: LocalDate?,
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    val totalAmount: BigDecimal,
    @Column(name = "down_payment_percentage", nullable = false, precision = 5, scale = 2)
    val downPaymentPercentage: BigDecimal,
    @Column(name = "paid_percentage", nullable = false, precision = 5, scale = 2)
    val paidPercentage: BigDecimal,
    @Column(name = "payment_method", nullable = false, length = 30)
    val paymentMethod: String,
    @Column(name = "observation", columnDefinition = "TEXT")
    val observation: String?,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime,
)
