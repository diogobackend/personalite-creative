package com.diadsimulation.sales.core.usecase

import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.movements.core.domain.enums.MovementDirection.IN
import com.diadsimulation.movements.core.domain.enums.MovementOperation.CREATED
import com.diadsimulation.movements.core.domain.enums.MovementOperation.PAYMENT
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.SALE
import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.RECEIPT
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SalePayment
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import com.diadsimulation.sales.core.ports.output.CreateSaleOutputPort
import com.diadsimulation.sales.core.ports.output.SalePaymentRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.NoSuchElementException
import java.util.UUID

open class CreateSaleUseCaseImplementation(
    private val createSaleOutputPort: CreateSaleOutputPort,
    private val userRepositoryOutputPort: UserRepositoryOutputPort,
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    private val salePaymentRepositoryOutputPort: SalePaymentRepositoryOutputPort,
    private val movementRepositoryOutputPort: MovementRepositoryOutputPort,
) : CreateSaleInputPort {
    @Transactional
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
            require(item.unitPrice > ZERO) {
                "O preço unitário do produto deve ser maior que zero."
            }
            require(item.totalPrice > ZERO) {
                "O produto precisa ser vendido com um valor positivo."
            }
        }

        sale.items
            .map { it.productId }
            .distinct()
            .forEach { productId ->
                productRepositoryOutputPort.findById(productId)
                    ?: throw NoSuchElementException(
                        "Produto não encontrado: $productId",
                    )
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

        val calculatedTotal =
            sale.items.fold(ZERO) { total, item ->
                total + item.totalPrice
            }

        require(calculatedTotal.compareTo(sale.totalAmount) == 0) {
            "O valor total da venda deve corresponder à soma dos produtos, incluindo acréscimos e descontos."
        }

        require(sale.totalAmount > ZERO) {
            "O valor total deve ser maior que zero."
        }

        require(sale.downPaymentPercentage in ZERO..HUNDRED) {
            "A porcentagem de entrada deve estar entre 0 e 100."
        }

        val savedSale = createSaleOutputPort.save(sale)
        val now = OffsetDateTime.now()

        movementRepositoryOutputPort.save(
            Movement(
                id = UUID.randomUUID(),
                originId = savedSale.id,
                origin = SALE,
                operation = CREATED,
                direction = null,
                status = null,
                previousValue = null,
                currentValue = savedSale.totalAmount,
                amount = savedSale.totalAmount,
                description = "Venda registrada.",
                createdAt = now,
            ),
        )

        if (savedSale.receivedAmount > ZERO) {
            salePaymentRepositoryOutputPort.save(
                SalePayment(
                    saleId = savedSale.id,
                    amount = savedSale.receivedAmount,
                    type = RECEIPT,
                    paidAt = now,
                ),
            )

            movementRepositoryOutputPort.save(
                Movement(
                    id = UUID.randomUUID(),
                    originId = savedSale.id,
                    origin = SALE,
                    operation = PAYMENT,
                    direction = IN,
                    status = null,
                    previousValue = ZERO,
                    currentValue = savedSale.receivedAmount,
                    amount = savedSale.receivedAmount,
                    description = "Recebimento inicial da venda.",
                    createdAt = now,
                ),
            )
        }

        return savedSale
    }

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
