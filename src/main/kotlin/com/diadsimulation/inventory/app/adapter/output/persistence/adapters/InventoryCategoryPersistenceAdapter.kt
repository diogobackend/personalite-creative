package com.diadsimulation.inventory.app.adapter.output.persistence.adapters

import com.diadsimulation.categories.app.adapter.output.persistence.repository.CategoryJpaRepository
import com.diadsimulation.categories.core.domain.enums.CategoryType.INVENTORY
import com.diadsimulation.inventory.core.ports.output.InventoryCategoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InventoryCategoryPersistenceAdapter(
    private val categoryJpaRepository: CategoryJpaRepository,
) : InventoryCategoryOutputPort {
    override fun existsByIdAndTypeInventory(categoryId: UUID): Boolean =
        categoryJpaRepository.existsByIdAndType(
            categoryId,
            INVENTORY,
        )
}
