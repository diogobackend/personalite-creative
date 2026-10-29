package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory.STOCK
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.ports.output.ExpenseStockOutputPort
import com.diadsimulation.inventory.core.domain.enums.InventoryMovementType.IN
import com.diadsimulation.inventory.core.domain.enums.InventoryMovementType.OUT
import com.diadsimulation.inventory.core.domain.model.InventoryMovement
import com.diadsimulation.inventory.core.ports.output.InventoryMovementRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.NoSuchElementException
import java.util.UUID

@Component
class ExpenseStockPersistenceAdapter(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    private val inventoryMovementRepositoryOutputPort: InventoryMovementRepositoryOutputPort,
) : ExpenseStockOutputPort {
    override fun registerEntry(expense: Expense) {
        val inventoryId = requireNotNull(expense.inventoryId)
        val quantity = requireNotNull(expense.quantity)

        val inventory =
            inventoryRepositoryOutputPort.findById(inventoryId)
                ?: throw NoSuchElementException("Item do estoque não encontrado.")

        inventoryRepositoryOutputPort.save(
            inventory.copy(
                quantity = inventory.quantity + quantity,
                updatedAt = OffsetDateTime.now(),
            ),
        )

        inventoryMovementRepositoryOutputPort.save(
            InventoryMovement(
                inventoryId = inventoryId,
                type = IN,
                quantity = quantity,
                reason = "EXPENSE",
                expenseId = expense.id,
            ),
        )
    }

    override fun updateEntry(
        previousExpense: Expense,
        updatedExpense: Expense,
    ) {
        val previousInventoryId =
            previousExpense.inventoryId.takeIf {
                previousExpense.category == STOCK
            }
        val updatedInventoryId =
            updatedExpense.inventoryId.takeIf {
                updatedExpense.category == STOCK
            }

        val previousQuantity =
            if (previousInventoryId != null) requireNotNull(previousExpense.quantity) else 0
        val updatedQuantity =
            if (updatedInventoryId != null) requireNotNull(updatedExpense.quantity) else 0

        if (previousInventoryId == updatedInventoryId && previousQuantity == updatedQuantity) {
            return
        }

        fun adjustStock(
            inventoryId: UUID,
            delta: Int,
        ) {
            if (delta == 0) return

            val inventory =
                inventoryRepositoryOutputPort.findById(inventoryId)
                    ?: throw NoSuchElementException("Item do estoque não encontrado.")

            val newQuantity = inventory.quantity + delta
            require(newQuantity >= 0) {
                "Estoque insuficiente para ajustar a despesa."
            }

            inventoryRepositoryOutputPort.save(
                inventory.copy(
                    quantity = newQuantity,
                    updatedAt = OffsetDateTime.now(),
                ),
            )

            inventoryMovementRepositoryOutputPort.save(
                InventoryMovement(
                    inventoryId = inventoryId,
                    type = if (delta > 0) IN else OUT,
                    quantity = kotlin.math.abs(delta),
                    reason = "EXPENSE_ADJUSTMENT",
                    expenseId = updatedExpense.id,
                ),
            )
        }

        if (previousInventoryId == updatedInventoryId) {
            previousInventoryId?.let {
                adjustStock(it, updatedQuantity - previousQuantity)
            }
        } else {
            previousInventoryId?.let {
                adjustStock(it, -previousQuantity)
            }
            updatedInventoryId?.let {
                adjustStock(it, updatedQuantity)
            }
        }
    }
}
