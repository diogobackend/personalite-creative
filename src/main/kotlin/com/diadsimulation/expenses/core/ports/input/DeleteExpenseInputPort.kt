package com.diadsimulation.expenses.core.ports.input

import java.util.UUID

interface DeleteExpenseInputPort {
    fun execute(id: UUID)
}
