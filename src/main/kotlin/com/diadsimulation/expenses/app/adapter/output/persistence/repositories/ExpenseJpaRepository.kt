package com.diadsimulation.expenses.app.adapter.output.persistence.repositories

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface ExpenseJpaRepository :
    JpaRepository<ExpenseEntity, UUID>,
    JpaSpecificationExecutor<ExpenseEntity> {
    fun findDistinctByProductsProductId(productId: UUID): List<ExpenseEntity>
}
