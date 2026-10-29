package com.diadsimulation.expenses.core.ports.output

import com.diadsimulation.expenses.core.domain.model.Expense

interface ExpenseStockOutputPort {
    fun registerEntry(expense: Expense)

    fun updateEntry(
        previousExpense: Expense,
        updatedExpense: Expense,
    )
}
