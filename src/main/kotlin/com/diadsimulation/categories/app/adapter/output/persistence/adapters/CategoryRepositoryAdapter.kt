package com.diadsimulation.categories.app.adapter.output.persistence.adapters

import com.diadsimulation.categories.app.adapter.output.persistence.entity.CategoryEntity
import com.diadsimulation.categories.app.adapter.output.persistence.mappers.CategoryPersistenceMapper
import com.diadsimulation.categories.app.adapter.output.persistence.repository.CategoryJpaRepository
import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import java.util.UUID

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

    override fun findAll(
        name: String?,
        type: CategoryType?,
    ): List<Category> =
        categoryJpaRepository
            .findAll(
                Specification<CategoryEntity> { root, _, criteriaBuilder ->
                    val predicates = mutableListOf<Predicate>()

                    name?.let {
                        predicates.add(
                            criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%${it.lowercase()}%",
                            ),
                        )
                    }

                    type?.let {
                        predicates.add(
                            criteriaBuilder.equal(root.get<CategoryType>("type"), it),
                        )
                    }

                    criteriaBuilder.and(*predicates.toTypedArray())
                },
            ).map(categoryPersistenceMapper::toDomain)

    override fun findById(id: UUID): Category? =
        categoryJpaRepository
            .findById(id)
            .orElse(null)
            ?.let(categoryPersistenceMapper::toDomain)

    override fun deleteById(id: UUID) {
        categoryJpaRepository.deleteById(id)
    }
}
