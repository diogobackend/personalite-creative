package com.diadsimulation.products.core.port.input

import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.UpdateProductCommand
import java.util.UUID

interface UpdateProductInputPort {
    fun execute(
        productId: UUID,
        request: UpdateProductCommand,
    ): Product
}
