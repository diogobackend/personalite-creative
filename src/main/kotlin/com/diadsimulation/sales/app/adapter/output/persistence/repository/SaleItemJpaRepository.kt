package com.diadsimulation.sales.app.adapter.output.persistence.repository

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SaleItemJpaRepository : JpaRepository<SaleItemEntity, UUID>
