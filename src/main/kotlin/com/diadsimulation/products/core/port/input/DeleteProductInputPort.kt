package com.diadsimulation.products.core.port.input

import java.util.UUID

interface DeleteProductInputPort {
    fun execute(id: UUID)
}
