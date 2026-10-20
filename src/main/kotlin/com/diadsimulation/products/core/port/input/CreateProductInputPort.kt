package com.diadsimulation.products.core.port.input

import com.diadsimulation.products.core.domain.model.Product

interface CreateProductInputPort {
    fun execute(product: Product): Product
}
