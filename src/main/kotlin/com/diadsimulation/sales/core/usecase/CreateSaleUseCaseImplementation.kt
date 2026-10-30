package com.diadsimulation.sales.core.usecase

import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import com.diadsimulation.sales.core.ports.output.CreateSaleOutputPort
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.NoSuchElementException

class CreateSaleUseCaseImplementation(
    private val createSaleOutputPort: CreateSaleOutputPort,
    private val userRepositoryOutputPort: UserRepositoryOutputPort,
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
) : CreateSaleInputPort {
    override fun execute(sale: Sale): Sale {
        userRepositoryOutputPort.findById(sale.customerId)
            ?: throw NoSuchElementException("Cliente não encontrado.")

        require(sale.items.isNotEmpty()) {
            "A venda deve possuir pelo menos um produto."
        }

        sale.items.forEach { item ->
            require(item.quantity > 0) {
                "A quantidade do produto deve ser maior que zero."
            }
        }

        sale.items.map { it.productId }.distinct().forEach { productId ->
            productRepositoryOutputPort.findById(productId)
                ?: throw NoSuchElementException("Produto não encontrado: $productId")
        }

        sale.items
            .mapNotNull { item ->
                item.inventoryId?.let { inventoryId ->
                    inventoryId to item.productId
                }
            }.forEach { (inventoryId, productId) ->
                val inventory =
                    inventoryRepositoryOutputPort.findById(inventoryId)
                        ?: throw NoSuchElementException(
                            "Item de estoque não encontrado: $inventoryId",
                        )

                require(inventory.productId == productId) {
                    "O item de estoque não pertence ao produto informado."
                }
            }

        require(sale.totalAmount > ZERO) {
            "O valor total deve ser maior que zero."
        }

        require(sale.downPaymentPercentage in ZERO..HUNDRED) {
            "A porcentagem de entrada deve estar entre 0 e 100."
        }

        require(sale.paidPercentage in ZERO..HUNDRED) {
            "A porcentagem paga deve estar entre 0 e 100."
        }

        return createSaleOutputPort.save(sale)
    }

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
