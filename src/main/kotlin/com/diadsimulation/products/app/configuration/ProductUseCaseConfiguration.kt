package com.diadsimulation.products.app.configuration

import com.diadsimulation.products.core.port.input.CreateProductInputPort
import com.diadsimulation.products.core.port.input.GetProductByIdInputPort
import com.diadsimulation.products.core.port.input.GetProductsInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.products.core.usecase.CreateProductUseCaseImplementation
import com.diadsimulation.products.core.usecase.GetProductByIdUseCaseImplementation
import com.diadsimulation.products.core.usecase.GetProductsUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductUseCaseConfiguration {
    @Bean
    fun createProductUseCase(productRepositoryOutputPort: ProductRepositoryOutputPort): CreateProductInputPort =
        CreateProductUseCaseImplementation(productRepositoryOutputPort)

    @Bean
    fun getProductsUseCase(productRepositoryOutputPort: ProductRepositoryOutputPort): GetProductsInputPort =
        GetProductsUseCaseImplementation(productRepositoryOutputPort)

    @Bean
    fun getProductByIdUseCase(productRepositoryOutputPort: ProductRepositoryOutputPort): GetProductByIdInputPort =
        GetProductByIdUseCaseImplementation(productRepositoryOutputPort)
}
