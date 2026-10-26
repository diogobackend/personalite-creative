package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpenseUpdate
import java.util.UUID

interface UpdateExpenseInputPort {
    fun execute(
        id: UUID,
        update: ExpenseUpdate,
    ): Expense
}
