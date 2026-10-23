package com.diadsimulation.expenses.app.adapter.input.web.controller

import com.diadsimulation.expenses.app.adapter.input.web.mappers.ExpenseWebMapper
import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.app.adapter.input.web.swagger.ExpenseApi
import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val createExpenseInputPort: CreateExpenseInputPort,
    private val expenseWebMapper: ExpenseWebMapper,
) : ExpenseApi {
    @PostMapping
    override fun create(request: CreateExpenseRequest): ResponseEntity<ExpenseResponse> {
        val expense = expenseWebMapper.toDomain(request)
        val createdExpense = createExpenseInputPort.execute(expense)

        return ResponseEntity
            .status(CREATED)
            .body(expenseWebMapper.toResponse(createdExpense))
    }
}
