package com.diadsimulation.sales.app.adapter.output.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "sale_items")
class SaleItemEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "sale_id", nullable = false)
    val saleId: UUID,
    @Column(name = "product_id", nullable = false)
    val productId: UUID,
    @Column(name = "inventory_id")
    val inventoryId: UUID?,
)
