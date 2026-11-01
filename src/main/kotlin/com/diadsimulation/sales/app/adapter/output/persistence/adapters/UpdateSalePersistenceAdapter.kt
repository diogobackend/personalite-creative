package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.mappers.SalePersistenceMapper
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleItemJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleJpaRepository
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.ports.output.UpdateSaleOutputPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateSalePersistenceAdapter(
    private val saleJpaRepository: SaleJpaRepository,
    private val saleItemJpaRepository: SaleItemJpaRepository,
    private val salePersistenceMapper: SalePersistenceMapper,
) : UpdateSaleOutputPort {
    @Transactional
    override fun update(sale: Sale): Sale {
        require(saleJpaRepository.existsById(sale.id)) {
            "Venda não encontrada para o ID: ${sale.id}"
        }

        saleJpaRepository.save(
            salePersistenceMapper.toEntity(sale),
        )

        saleItemJpaRepository.deleteAllBySaleId(sale.id)
        saleItemJpaRepository.flush()

        saleItemJpaRepository.saveAll(
            salePersistenceMapper.toItemEntities(sale),
        )

        return sale
    }
}
