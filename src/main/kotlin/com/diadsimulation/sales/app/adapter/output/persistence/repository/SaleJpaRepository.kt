package com.diadsimulation.sales.app.adapter.output.persistence.repository

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface SaleJpaRepository :
    JpaRepository<SaleEntity, UUID>,
    JpaSpecificationExecutor<SaleEntity>
