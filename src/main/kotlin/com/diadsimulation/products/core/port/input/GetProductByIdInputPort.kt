package com.diadsimulation.products.core.port.input

import com.diadsimulation.products.core.domain.model.Product
import java.util.UUID

interface GetProductByIdInputPort {
    fun execute(productId: UUID): Product
}
