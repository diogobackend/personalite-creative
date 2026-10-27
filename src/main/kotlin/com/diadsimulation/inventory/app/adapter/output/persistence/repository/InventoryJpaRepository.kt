package com.diadsimulation.inventory.app.adapter.output.persistence.repository

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InventoryJpaRepository : JpaRepository<InventoryEntity, UUID> {
    fun existsBySku(sku: String): Boolean
}
