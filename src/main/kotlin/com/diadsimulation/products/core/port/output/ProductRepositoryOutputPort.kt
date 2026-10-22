package com.diadsimulation.products.core.port.output

import com.diadsimulation.products.core.domain.model.GetProductsFilter
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductsPage
import java.util.UUID

interface ProductRepositoryOutputPort {
    fun save(product: Product): Product

    fun update(product: Product): Product

    fun findAll(filter: GetProductsFilter): ProductsPage

    fun existsByCode(code: String): Boolean

    fun findByCode(code: String): Product?

    fun findById(id: UUID): Product?
}
