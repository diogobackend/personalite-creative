package com.diadsimulation.products.app.adapter.output.persistence.adapters

import com.diadsimulation.products.app.adapter.output.persistence.mappers.ProductPersistenceMapper
import com.diadsimulation.products.app.adapter.output.persistence.repositories.ProductJpaRepository
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class ProductPersistenceAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productPersistenceMapper: ProductPersistenceMapper,
) : ProductRepositoryOutputPort {
    @Transactional
    override fun save(product: Product): Product {
        val entity = productPersistenceMapper.toEntity(product)
        val savedEntity = productJpaRepository.save(entity)

        return productPersistenceMapper.toDomain(savedEntity)
    }

    override fun existsByCode(code: String): Boolean = productJpaRepository.existsByCode(code)

    @Transactional(readOnly = true)
    override fun findByCode(code: String): Product? =
        productJpaRepository
            .findByCode(code)
            ?.let(productPersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Product? =
        productJpaRepository
            .findById(id)
            .orElse(null)
            ?.let(productPersistenceMapper::toDomain)
}
