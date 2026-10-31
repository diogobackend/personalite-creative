package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.mappers.SalePersistenceMapper
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleItemJpaRepository
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SaleJpaRepository
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.ports.output.FindSaleByIdOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FindSaleByIdPersistenceAdapter(
    private val saleJpaRepository: SaleJpaRepository,
    private val saleItemJpaRepository: SaleItemJpaRepository,
    private val salePersistenceMapper: SalePersistenceMapper,
) : FindSaleByIdOutputPort {
    override fun findById(id: UUID): Sale? {
        val sale =
            saleJpaRepository.findById(id).orElse(null)
                ?: return null

        val items = saleItemJpaRepository.findAllBySaleId(id)

        return salePersistenceMapper.toDomain(sale, items)
    }
}
