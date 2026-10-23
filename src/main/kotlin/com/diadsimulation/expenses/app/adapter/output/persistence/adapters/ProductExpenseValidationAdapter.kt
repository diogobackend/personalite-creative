package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.expenses.core.ports.output.ProductExpenseValidationOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductExpenseValidationAdapter(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : ProductExpenseValidationOutputPort {
    override fun existsById(productId: UUID): Boolean = productRepositoryOutputPort.findById(productId) != null

    override fun belongToSameCategory(productIds: List<UUID>): Boolean {
        val products = productIds.mapNotNull(productRepositoryOutputPort::findById)

        return products.size == productIds.size &&
            products.map { it.category }.distinct().size == 1
    }
}
