package com.diadsimulation.expenses.app.adapter.input.web.responses

data class ExpensesPageResponse(
    val content: List<ExpenseResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalElementsThisPage: Int,
    val totalPages: Int,
)
