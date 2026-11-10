package com.diadsimulation.sales.app.adapter.output.persistence.repository

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SalePaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SalePaymentJpaRepository : JpaRepository<SalePaymentEntity, UUID> {
    fun findAllBySaleIdOrderByPaidAtAsc(saleId: UUID): List<SalePaymentEntity>
}
