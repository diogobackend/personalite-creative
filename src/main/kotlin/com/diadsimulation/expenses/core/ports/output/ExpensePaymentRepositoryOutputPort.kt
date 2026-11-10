package com.diadsimulation.expenses.core.ports.output

import com.diadsimulation.expenses.core.domain.model.ExpensePayment
import java.util.UUID

interface ExpensePaymentRepositoryOutputPort {
    fun save(payment: ExpensePayment): ExpensePayment

    fun findByExpenseId(expenseId: UUID): List<ExpensePayment>
}
