package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.categories.app.adapter.output.persistence.repository.CategoryJpaRepository
import com.diadsimulation.categories.core.domain.enums.CategoryType.EXPENSE
import com.diadsimulation.expenses.core.ports.output.ExpenseCategoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ExpenseCategoryPersistenceAdapter(
    private val categoryJpaRepository: CategoryJpaRepository,
) : ExpenseCategoryOutputPort {
    override fun existsByIdAndTypeExpense(categoryId: UUID): Boolean =
        categoryJpaRepository.existsByIdAndType(
            categoryId,
            EXPENSE,
        )
}
