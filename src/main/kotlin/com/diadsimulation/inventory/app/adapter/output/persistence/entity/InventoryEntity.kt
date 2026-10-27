package com.diadsimulation.inventory.app.adapter.output.persistence.entity

import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
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
    @Enumerated(STRING)
    @Column(name = "category", nullable = false)
    var category: ProductCategory,
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,
    @Column(name = "product_id")
    var productId: UUID?,
    @Column(name = "quantity", nullable = false)
    var quantity: Int,
    @Column(name = "minimum_quantity")
    var minimumQuantity: Int?,
    @Enumerated(STRING)
    @Column(name = "unit")
    var unit: InventoryUnit?,
    @Column(name = "color")
    var color: String?,
    @Enumerated(STRING)
    @Column(name = "size")
    var size: InventorySize?,
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
