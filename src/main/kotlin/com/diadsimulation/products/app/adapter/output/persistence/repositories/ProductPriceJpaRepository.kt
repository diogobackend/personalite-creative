package com.diadsimulation.products.app.adapter.output.persistence.repositories

import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductPriceEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductPriceJpaRepository : JpaRepository<ProductPriceEntity, UUID>
