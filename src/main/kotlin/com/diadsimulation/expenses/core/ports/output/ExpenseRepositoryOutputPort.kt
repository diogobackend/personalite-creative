package com.diadsimulation.expenses.core.ports.output

import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpensesPage
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter
import java.util.UUID

interface ExpenseRepositoryOutputPort {
    fun save(expense: Expense): Expense

    fun findById(id: UUID): Expense?

    fun findByProductId(productId: UUID): List<Expense>

    fun findAll(filter: GetExpensesFilter): ExpensesPage
}
