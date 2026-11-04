package com.diadsimulation.categories.app.adapter.output.persistence.adapters

import com.diadsimulation.categories.app.adapter.output.persistence.mappers.CategoryPersistenceMapper
import com.diadsimulation.categories.app.adapter.output.persistence.repository.CategoryJpaRepository
import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryAdapter(
    private val categoryJpaRepository: CategoryJpaRepository,
    private val categoryPersistenceMapper: CategoryPersistenceMapper,
) : CategoryRepositoryOutputPort {
    override fun save(category: Category): Category =
        categoryPersistenceMapper.toDomain(
            categoryJpaRepository.save(
                categoryPersistenceMapper.toEntity(category),
            ),
        )

    override fun existsByNameAndType(
        name: String,
        type: CategoryType,
    ): Boolean = categoryJpaRepository.existsByNameAndType(name, type)
}
