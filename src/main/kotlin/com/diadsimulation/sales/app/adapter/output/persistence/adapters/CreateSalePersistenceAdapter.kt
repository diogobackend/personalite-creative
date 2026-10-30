package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.mappers.SalePersistenceMapper
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleItemJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleJpaRepository
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.ports.output.CreateSaleOutputPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CreateSalePersistenceAdapter(
    private val saleJpaRepository: SaleJpaRepository,
    private val saleItemJpaRepository: SaleItemJpaRepository,
    private val salePersistenceMapper: SalePersistenceMapper,
) : CreateSaleOutputPort {
    @Transactional
    override fun save(sale: Sale): Sale {
        val saleEntity = salePersistenceMapper.toEntity(sale)
        val saleItemEntities = salePersistenceMapper.toItemEntities(sale)

        saleJpaRepository.save(saleEntity)
        saleItemJpaRepository.saveAll(saleItemEntities)

        return sale
    }
}
