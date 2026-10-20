package com.diadsimulation.products.core.port.input

import com.diadsimulation.products.core.domain.model.GetProductsFilter
import com.diadsimulation.products.core.domain.model.ProductsPage

interface GetProductsInputPort {
    fun execute(filter: GetProductsFilter): ProductsPage
}
