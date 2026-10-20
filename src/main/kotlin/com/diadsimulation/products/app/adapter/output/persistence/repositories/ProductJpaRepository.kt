package com.diadsimulation.products.app.adapter.output.persistence.repositories

import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductJpaRepository : JpaRepository<ProductEntity, UUID> {
    fun existsByCode(code: String): Boolean

    fun findByCode(code: String): ProductEntity?
}
