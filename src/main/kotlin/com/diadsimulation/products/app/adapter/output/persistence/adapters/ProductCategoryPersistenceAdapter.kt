package com.diadsimulation.products.app.adapter.output.persistence.adapters

import com.diadsimulation.categories.app.adapter.output.persistence.repository.CategoryJpaRepository
import com.diadsimulation.categories.core.domain.enums.CategoryType.PRODUCT
import com.diadsimulation.products.core.port.output.ProductCategoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductCategoryPersistenceAdapter(
    private val categoryJpaRepository: CategoryJpaRepository,
) : ProductCategoryOutputPort {
    override fun existsByIdAndTypeProduct(categoryId: UUID): Boolean =
        categoryJpaRepository.existsByIdAndType(
            categoryId,
            PRODUCT,
        )
}
