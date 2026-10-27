package com.diadsimulation.inventory.app.adapter.output.persistence.entity

import com.diadsimulation.inventory.core.domain.enums.InventoryMovementType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "inventory_movements")
class InventoryMovementEntity(
    @Id
    @Column(name = "movement_id", nullable = false)
    val movementId: UUID,
    @Column(name = "inventory_id", nullable = false)
    val inventoryId: UUID,
    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    val type: InventoryMovementType,
    @Column(name = "quantity", nullable = false)
    val quantity: Int,
    @Column(name = "reason", nullable = false)
    val reason: String,
    @Column(name = "expense_id")
    val expenseId: UUID?,
    @Column(name = "sale_id")
    val saleId: UUID?,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
)
