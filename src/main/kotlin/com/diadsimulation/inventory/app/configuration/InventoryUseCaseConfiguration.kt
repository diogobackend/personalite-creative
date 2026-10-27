package com.diadsimulation.inventory.app.configuration

import com.diadsimulation.inventory.core.ports.input.GetInventoryInputPort
import com.diadsimulation.inventory.core.ports.input.RegisterInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryMovementRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.inventory.core.usecase.GetInventoryUseCaseImplementation
import com.diadsimulation.inventory.core.usecase.RegisterInventoryUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InventoryUseCaseConfiguration {
    @Bean
    fun registerInventoryUseCase(
        inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
        inventoryMovementRepositoryOutputPort: InventoryMovementRepositoryOutputPort,
    ): RegisterInventoryInputPort =
        RegisterInventoryUseCaseImplementation(
            inventoryRepositoryOutputPort,
            inventoryMovementRepositoryOutputPort,
        )

    @Bean
    fun getInventoryUseCase(inventoryRepositoryOutputPort: InventoryRepositoryOutputPort): GetInventoryInputPort =
        GetInventoryUseCaseImplementation(
            inventoryRepositoryOutputPort,
        )
}
