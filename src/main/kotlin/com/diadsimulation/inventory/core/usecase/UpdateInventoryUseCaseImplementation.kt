package com.diadsimulation.inventory.core.usecase

import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.domain.model.UpdateInventory
import com.diadsimulation.inventory.core.ports.input.UpdateInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryCategoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.NoSuchElementException
import java.util.UUID

open class UpdateInventoryUseCaseImplementation(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    private val inventoryCategoryOutputPort: InventoryCategoryOutputPort,
) : UpdateInventoryInputPort {
    @Transactional
    override fun execute(
        id: UUID,
        update: UpdateInventory,
    ): Inventory {
        val inventory =
            inventoryRepositoryOutputPort.findById(id)
                ?: throw NoSuchElementException("Item do estoque não encontrado.")

        val updatedInventory =
            inventory.copy(
                name = update.name ?: inventory.name,
                category = update.category ?: inventory.category,
                categoryId = update.categoryId ?: inventory.categoryId,
                description = update.description ?: inventory.description,
                quantity = update.quantity ?: inventory.quantity,
                minimumQuantity = update.minimumQuantity ?: inventory.minimumQuantity,
                unit = update.unit ?: inventory.unit,
                color = update.color ?: inventory.color,
                size = update.size ?: inventory.size,
                material = update.material ?: inventory.material,
                brand = update.brand ?: inventory.brand,
                model = update.model ?: inventory.model,
                updatedAt = OffsetDateTime.now(),
            )

        update.categoryId?.let { categoryId ->
            require(inventoryCategoryOutputPort.existsByIdAndTypeInventory(categoryId)) {
                "A categoria informada não existe ou não pertence ao tipo INVENTORY."
            }
        }
        return inventoryRepositoryOutputPort.save(updatedInventory)
    }
}
