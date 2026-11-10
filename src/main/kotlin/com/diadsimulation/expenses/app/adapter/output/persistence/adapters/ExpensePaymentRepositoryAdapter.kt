package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpensePaymentEntity
import com.diadsimulation.expenses.app.adapter.output.persistence.repositories.ExpensePaymentJpaRepository
import com.diadsimulation.expenses.core.domain.model.ExpensePayment
import com.diadsimulation.expenses.core.ports.output.ExpensePaymentRepositoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ExpensePaymentRepositoryAdapter(
    private val expensePaymentJpaRepository: ExpensePaymentJpaRepository,
) : ExpensePaymentRepositoryOutputPort {
    override fun save(payment: ExpensePayment): ExpensePayment = expensePaymentJpaRepository.save(payment.toEntity()).toDomain()

    override fun findByExpenseId(expenseId: UUID): List<ExpensePayment> =
        expensePaymentJpaRepository
            .findAllByExpenseIdOrderByPaidAtAsc(expenseId)
            .map { it.toDomain() }

    private fun ExpensePayment.toEntity() =
        ExpensePaymentEntity(
            id = id,
            expenseId = expenseId,
            amount = amount,
            type = type,
            paidAt = paidAt,
            createdAt = createdAt,
        )

    private fun ExpensePaymentEntity.toDomain() =
        ExpensePayment(
            id = id,
            expenseId = expenseId,
            amount = amount,
            type = type,
            paidAt = paidAt,
            createdAt = createdAt,
        )
}
