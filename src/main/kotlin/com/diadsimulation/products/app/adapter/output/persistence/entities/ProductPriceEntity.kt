package com.diadsimulation.products.app.adapter.output.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(
    name = "product_prices",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_product_prices_quantity",
            columnNames = ["product_id", "minimum_quantity"],
        ),
    ],
)
class ProductPriceEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductEntity,
    @Column(name = "minimum_quantity", nullable = false)
    var minimumQuantity: Int,
    @Column(
        name = "unit_price",
        nullable = false,
        precision = 12,
        scale = 2,
    )
    var unitPrice: BigDecimal,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
