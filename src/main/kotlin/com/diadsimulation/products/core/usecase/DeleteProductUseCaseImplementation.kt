package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.port.input.DeleteProductInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import java.util.UUID

class DeleteProductUseCaseImplementation(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : DeleteProductInputPort {
    override fun execute(id: UUID) {
        productRepositoryOutputPort.deleteById(id)
    }
}
