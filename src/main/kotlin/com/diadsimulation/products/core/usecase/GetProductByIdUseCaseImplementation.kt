package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.domain.exceptions.ProductNotFoundException
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.port.input.GetProductByIdInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import java.util.UUID

class GetProductByIdUseCaseImplementation(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : GetProductByIdInputPort {
    override fun execute(productId: UUID): Product =
        productRepositoryOutputPort.findById(productId)
            ?: throw ProductNotFoundException(productId)
}
