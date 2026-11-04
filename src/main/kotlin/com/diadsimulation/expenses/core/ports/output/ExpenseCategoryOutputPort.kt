package com.diadsimulation.expenses.core.ports.output

import java.util.UUID

interface ExpenseCategoryOutputPort {
    fun existsByIdAndTypeExpense(categoryId: UUID): Boolean
}
