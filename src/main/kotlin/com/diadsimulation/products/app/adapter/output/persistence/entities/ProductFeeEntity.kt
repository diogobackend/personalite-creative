package com.diadsimulation.products.app.adapter.output.persistence.entities

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "product_fees")
class ProductFeeEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductEntity,
    @Column(nullable = false, length = 100)
    var name: String,
    @Column(nullable = false, precision = 12, scale = 2)
    var amount: BigDecimal,
    @Enumerated(STRING)
    @Column(name = "charge_type", nullable = false, length = 30)
    var chargeType: FeeChargeType,
    @Column(nullable = false)
    var required: Boolean = false,
    @Column(nullable = false)
    var active: Boolean = true,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
