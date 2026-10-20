package com.diadsimulation.products.core.port.output

import com.diadsimulation.products.core.domain.model.Product
import java.util.UUID

interface ProductRepositoryOutputPort {
    fun save(product: Product): Product

    fun existsByCode(code: String): Boolean

    fun findByCode(code: String): Product?

    fun findById(id: UUID): Product?
}
