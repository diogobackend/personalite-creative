package com.diadsimulation.sales.app.configuration

import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import com.diadsimulation.sales.core.ports.output.CreateSaleOutputPort
import com.diadsimulation.sales.core.ports.output.SearchSalesOutputPort
import com.diadsimulation.sales.core.usecase.CreateSaleUseCaseImplementation
import com.diadsimulation.sales.core.usecase.SearchSalesUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SaleUseCaseConfig {
    @Bean
    fun createSaleUseCaseImplementation(
        createSaleOutputPort: CreateSaleOutputPort,
        userRepositoryOutputPort: UserRepositoryOutputPort,
        productRepositoryOutputPort: ProductRepositoryOutputPort,
        inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    ): CreateSaleInputPort =
        CreateSaleUseCaseImplementation(
            createSaleOutputPort = createSaleOutputPort,
            userRepositoryOutputPort = userRepositoryOutputPort,
            productRepositoryOutputPort = productRepositoryOutputPort,
            inventoryRepositoryOutputPort = inventoryRepositoryOutputPort,
        )

    @Bean
    fun searchSalesUseCase(searchSalesOutputPort: SearchSalesOutputPort): SearchSalesUseCaseImplementation =
        SearchSalesUseCaseImplementation(searchSalesOutputPort)
}
