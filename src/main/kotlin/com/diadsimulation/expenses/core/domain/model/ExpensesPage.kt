package com.diadsimulation.expenses.core.domain.model

data class ExpensesPage(
    val content: List<Expense>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalElementsThisPage: Int,
    val totalPages: Int,
)
