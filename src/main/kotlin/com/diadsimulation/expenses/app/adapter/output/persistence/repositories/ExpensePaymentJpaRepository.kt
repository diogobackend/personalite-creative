package com.diadsimulation.expenses.app.adapter.output.persistence.repositories

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpensePaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExpensePaymentJpaRepository : JpaRepository<ExpensePaymentEntity, UUID> {
    fun findAllByExpenseIdOrderByPaidAtAsc(expenseId: UUID): List<ExpensePaymentEntity>
}
