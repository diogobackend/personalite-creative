package com.diadsimulation.products.app.configuration

import com.diadsimulation.products.core.port.input.CreateProductInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.products.core.usecase.CreateProductUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductUseCaseConfiguration {
    @Bean
    fun createProductUseCase(productRepositoryOutputPort: ProductRepositoryOutputPort): CreateProductInputPort =
        CreateProductUseCaseImplementation(productRepositoryOutputPort)
}
