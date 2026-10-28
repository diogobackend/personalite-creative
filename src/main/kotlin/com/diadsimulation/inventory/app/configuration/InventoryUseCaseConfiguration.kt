package com.diadsimulation.inventory.app.configuration

import com.diadsimulation.inventory.core.ports.input.GetInventoryByIdInputPort
import com.diadsimulation.inventory.core.ports.input.GetInventoryInputPort
import com.diadsimulation.inventory.core.ports.input.RegisterInventoryInputPort
import com.diadsimulation.inventory.core.ports.input.UpdateInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryMovementRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.inventory.core.usecase.GetInventoryByIdUseCaseImplementation
import com.diadsimulation.inventory.core.usecase.GetInventoryUseCaseImplementation
import com.diadsimulation.inventory.core.usecase.RegisterInventoryUseCaseImplementation
import com.diadsimulation.inventory.core.usecase.UpdateInventoryUseCaseImplementation
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

    @Bean
    fun getInventoryByIdUseCase(inventoryRepositoryOutputPort: InventoryRepositoryOutputPort): GetInventoryByIdInputPort =
        GetInventoryByIdUseCaseImplementation(
            inventoryRepositoryOutputPort,
        )

    @Bean
    fun updateInventoryUseCase(inventoryRepositoryOutputPort: InventoryRepositoryOutputPort): UpdateInventoryInputPort =
        UpdateInventoryUseCaseImplementation(inventoryRepositoryOutputPort)
}
