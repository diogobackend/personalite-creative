package com.diadsimulation.categories.app.adapter.output.persistence.repository

import com.diadsimulation.categories.app.adapter.output.persistence.entity.CategoryEntity
import com.diadsimulation.categories.core.domain.enums.CategoryType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface CategoryJpaRepository :
    JpaRepository<CategoryEntity, UUID>,
    JpaSpecificationExecutor<CategoryEntity> {
    fun existsByNameAndType(
        name: String,
        type: CategoryType,
    ): Boolean

    fun existsByIdAndType(
        id: UUID,
        type: CategoryType,
    ): Boolean
}
