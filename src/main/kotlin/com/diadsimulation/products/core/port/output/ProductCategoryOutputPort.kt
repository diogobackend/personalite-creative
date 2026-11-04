package com.diadsimulation.products.core.port.output

import java.util.UUID

interface ProductCategoryOutputPort {
    fun existsByIdAndTypeProduct(categoryId: UUID): Boolean
}
