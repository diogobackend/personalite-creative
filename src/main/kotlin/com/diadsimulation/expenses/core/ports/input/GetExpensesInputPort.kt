package com.diadsimulation.expenses.core.ports.input

import com.diadsimulation.expenses.core.domain.model.ExpensesPage
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter

interface GetExpensesInputPort {
    fun execute(filter: GetExpensesFilter): ExpensesPage
}
