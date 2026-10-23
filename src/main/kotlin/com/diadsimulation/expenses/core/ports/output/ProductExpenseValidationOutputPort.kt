package com.diadsimulation.expenses.core.ports.output

import java.util.UUID

interface ProductExpenseValidationOutputPort {
    fun existsById(productId: UUID): Boolean

    fun belongToSameCategory(productIds: List<UUID>): Boolean
}
