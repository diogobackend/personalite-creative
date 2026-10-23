package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.expenses.app.adapter.output.persistence.mappers.ExpensePersistenceMapper
import com.diadsimulation.expenses.app.adapter.output.persistence.repositories.ExpenseJpaRepository
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class ExpensePersistenceAdapter(
    private val expenseJpaRepository: ExpenseJpaRepository,
    private val expensePersistenceMapper: ExpensePersistenceMapper,
) : ExpenseRepositoryOutputPort {
    @Transactional
    override fun save(expense: Expense): Expense {
        val entity = expensePersistenceMapper.toEntity(expense)
        val savedEntity = expenseJpaRepository.save(entity)

        return expensePersistenceMapper.toDomain(savedEntity)
    }

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Expense? =
        expenseJpaRepository
            .findById(id)
            .orElse(null)
            ?.let(expensePersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findByProductId(productId: UUID): List<Expense> =
        expenseJpaRepository
            .findDistinctByProductsProductId(productId)
            .map(expensePersistenceMapper::toDomain)
}
