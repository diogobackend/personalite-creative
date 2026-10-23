package com.diadsimulation.expenses.app.adapter.output.persistence.repositories

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExpenseJpaRepository : JpaRepository<ExpenseEntity, UUID> {
    fun findDistinctByProductsProductId(productId: UUID): List<ExpenseEntity>
}
