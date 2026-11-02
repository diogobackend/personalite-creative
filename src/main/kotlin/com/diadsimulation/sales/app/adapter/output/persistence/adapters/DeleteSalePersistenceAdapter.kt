package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleItemJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleJpaRepository
import com.diadsimulation.sales.core.ports.output.DeleteSaleOutputPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class DeleteSalePersistenceAdapter(
    private val saleJpaRepository: SaleJpaRepository,
    private val saleItemJpaRepository: SaleItemJpaRepository,
) : DeleteSaleOutputPort {
    @Transactional
    override fun deleteById(id: UUID) {
        saleItemJpaRepository.deleteAllBySaleId(id)
        saleJpaRepository.deleteById(id)
    }
}
