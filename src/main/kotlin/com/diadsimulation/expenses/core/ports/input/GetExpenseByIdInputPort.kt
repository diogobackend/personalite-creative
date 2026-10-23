package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.expenses.core.domain.model.Expense
import java.util.UUID

fun interface GetExpenseByIdInputPort {
    fun execute(id: UUID): Expense
}
