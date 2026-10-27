package com.diadsimulation.inventory.core.usecase

import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.inventory.core.domain.model.InventoryPage
import com.diadsimulation.inventory.core.ports.input.GetInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort

class GetInventoryUseCaseImplementation(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
) : GetInventoryInputPort {
    override fun execute(filter: GetInventoryFilter): InventoryPage {
        val effectiveFilter =
            if (filter.sku != null) {
                GetInventoryFilter(
                    sku = filter.sku,
                    page = 0,
                    pageSize = 1,
                )
            } else {
                filter.copy(
                    page = filter.page.coerceAtLeast(0),
                    pageSize = filter.pageSize.coerceIn(1, 100),
                )
            }

        return inventoryRepositoryOutputPort.findAll(effectiveFilter)
    }
}
