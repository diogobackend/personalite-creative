package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.expenses.core.domain.model.Expense

interface CreateExpenseInputPort {
    fun execute(expense: Expense): Expense
}
