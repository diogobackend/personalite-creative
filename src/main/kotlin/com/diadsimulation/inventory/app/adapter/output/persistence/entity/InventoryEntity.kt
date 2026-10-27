package com.diadsimulation.inventory.app.adapter.output.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(
    name = "inventory",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_inventory_sku",
            columnNames = ["sku"],
        ),
    ],
)
class InventoryEntity(
    @Id
    @Column(name = "inventory_id", nullable = false)
    val inventoryId: UUID,
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "category", nullable = false)
    var category: String,
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,
    @Column(name = "product_id")
    var productId: UUID?,
    @Column(name = "quantity", nullable = false)
    var quantity: Int,
    @Column(name = "minimum_quantity")
    var minimumQuantity: Int?,
    @Column(name = "unit", nullable = false)
    var unit: String,
    @Column(name = "color")
    var color: String?,
    @Column(name = "size")
    var size: String?,
    @Column(name = "material")
    var material: String?,
    @Column(name = "brand")
    var brand: String?,
    @Column(name = "model")
    var model: String?,
    @Column(name = "sku")
    var sku: String?,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime,
)
