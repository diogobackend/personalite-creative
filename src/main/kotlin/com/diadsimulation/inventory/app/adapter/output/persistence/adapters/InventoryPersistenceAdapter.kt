package com.diadsimulation.inventory.app.adapter.output.persistence.adapters

import com.diadsimulation.inventory.app.adapter.output.persistence.mapper.InventoryPersistenceMapper
import com.diadsimulation.inventory.app.adapter.output.persistence.repository.InventoryJpaRepository
import com.diadsimulation.inventory.app.adapter.output.persistence.specifications.InventorySpecifications
import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.domain.model.InventoryPage
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.aot.hint.TypeReference.listOf
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class InventoryPersistenceAdapter(
    private val inventoryJpaRepository: InventoryJpaRepository,
) : InventoryRepositoryOutputPort {
    override fun save(inventory: Inventory): Inventory {
        val entity = InventoryPersistenceMapper.toEntity(inventory)
        val savedEntity = inventoryJpaRepository.save(entity)

        return InventoryPersistenceMapper.toDomain(savedEntity)
    }

    @Transactional(readOnly = true)
    override fun findById(inventoryId: UUID): Inventory? =
        inventoryJpaRepository
            .findById(inventoryId)
            .map(InventoryPersistenceMapper::toDomain)
            .orElse(null)

    @Transactional(readOnly = true)
    override fun existsBySku(sku: String): Boolean = inventoryJpaRepository.existsBySku(sku)

    @Transactional(readOnly = true)
    override fun findAll(filter: GetInventoryFilter): InventoryPage {
        if (filter.sku != null) {
            val content =
                inventoryJpaRepository
                    .findBySku(filter.sku)
                    ?.let(InventoryPersistenceMapper::toDomain)
                    ?.let(::listOf)
                    ?: emptyList()

            return InventoryPage(
                content = content,
                page = 0,
                size = 1,
                totalElements = content.size.toLong(),
                totalElementsThisPage = content.size,
                totalPages = if (content.isEmpty()) 0 else 1,
            )
        }

        val allowedSortFields =
            setOf(
                "name",
                "category",
                "quantity",
                "unit",
                "color",
                "size",
                "material",
                "brand",
                "model",
                "sku",
                "createdAt",
                "updatedAt",
            )

        val sortBy =
            filter.sortBy.takeIf { it in allowedSortFields }
                ?: "name"

        val direction =
            if (filter.sortDirection.equals("DESC", ignoreCase = true)) {
                Sort.Direction.DESC
            } else {
                Sort.Direction.ASC
            }

        val sort =
            Sort
                .by(direction, sortBy)
                .and(Sort.by("inventoryId"))

        val pageable =
            PageRequest.of(
                filter.page.coerceAtLeast(0),
                filter.pageSize.coerceIn(1, 100),
                sort,
            )

        val result =
            inventoryJpaRepository.findAll(
                InventorySpecifications.withFilters(filter),
                pageable,
            )

        return InventoryPage(
            content = result.content.map(InventoryPersistenceMapper::toDomain),
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalElementsThisPage = result.numberOfElements,
            totalPages = result.totalPages,
        )
    }

    @Transactional
    override fun deleteById(inventoryId: UUID) {
        inventoryJpaRepository.deleteById(inventoryId)
    }
}
