package com.diadsimulation.sales.core.usecase

import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.RECEIPT
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.REFUND
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SalePayment
import com.diadsimulation.sales.core.domain.model.SaleUpdate
import com.diadsimulation.sales.core.domain.utils.UpdateField
import com.diadsimulation.sales.core.ports.input.FindSaleByIdInputPort
import com.diadsimulation.sales.core.ports.input.UpdateSaleInputPort
import com.diadsimulation.sales.core.ports.output.SalePaymentRepositoryOutputPort
import com.diadsimulation.sales.core.ports.output.UpdateSaleOutputPort
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.NoSuchElementException
import java.util.UUID

open class UpdateSaleUseCaseImplementation(
    private val findSaleByIdInputPort: FindSaleByIdInputPort,
    private val updateSaleOutputPort: UpdateSaleOutputPort,
    private val userRepositoryOutputPort: UserRepositoryOutputPort,
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    private val salePaymentRepositoryOutputPort: SalePaymentRepositoryOutputPort,
) : UpdateSaleInputPort {
    @Transactional
    override fun execute(
        id: UUID,
        update: SaleUpdate,
    ): Sale {
        val currentSale = findSaleByIdInputPort.execute(id)

        val customerId = update.customerId ?: currentSale.customerId
        val items = update.items ?: currentSale.items

        userRepositoryOutputPort.findById(customerId)
            ?: throw NoSuchElementException("Cliente não encontrado.")

        require(items.isNotEmpty()) {
            "A venda deve possuir pelo menos um produto."
        }

        items.forEach { item ->
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

        items.map { it.productId }.distinct().forEach { productId ->
            productRepositoryOutputPort.findById(productId)
                ?: throw NoSuchElementException("Produto não encontrado: $productId")
        }

        items
            .mapNotNull { item ->
                item.inventoryId?.let { it to item.productId }
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

        val totalAmount =
            items.fold(ZERO) { total, item ->
                total + item.totalPrice
            }

        require(totalAmount > ZERO) {
            "O valor total da venda deve ser maior que zero."
        }

        val downPaymentPercentage =
            update.downPaymentPercentage ?: currentSale.downPaymentPercentage

        val paidPercentage =
            update.paidPercentage ?: currentSale.paidPercentage

        require(downPaymentPercentage in ZERO..HUNDRED) {
            "A porcentagem de entrada deve estar entre 0 e 100."
        }

        require(paidPercentage in ZERO..HUNDRED) {
            "A porcentagem paga deve estar entre 0 e 100."
        }

        val updatedAt = OffsetDateTime.now()

        val updatedSale =
            currentSale.copy(
                customerId = customerId,
                items = items,
                totalAmount = totalAmount,
                downPaymentPercentage = downPaymentPercentage,
                paidPercentage = paidPercentage,
                paymentMethod = update.paymentMethod ?: currentSale.paymentMethod,
                updatedAt = updatedAt,
                deliveryDeadline =
                    when (val field = update.deliveryDeadline) {
                        is UpdateField.Set -> field.value
                        UpdateField.Unchanged -> currentSale.deliveryDeadline
                    },
                observation =
                    when (val field = update.observation) {
                        is UpdateField.Set -> field.value
                        UpdateField.Unchanged -> currentSale.observation
                    },
                deliveredAt =
                    when (update.delivered) {
                        true -> currentSale.deliveredAt ?: updatedAt
                        false -> null
                        null -> currentSale.deliveredAt
                    },
            )

        val previousReceivedAmount = currentSale.receivedAmount
        val updatedReceivedAmount = updatedSale.receivedAmount

        val difference = updatedReceivedAmount.subtract(previousReceivedAmount)

        val savedSale = updateSaleOutputPort.update(updatedSale)

        if (difference.compareTo(ZERO) != 0) {
            val type =
                if (difference > ZERO) {
                    RECEIPT
                } else {
                    REFUND
                }

            salePaymentRepositoryOutputPort.save(
                SalePayment(
                    saleId = savedSale.id,
                    amount = difference.abs(),
                    type = type,
                    paidAt = updatedAt,
                ),
            )
        }

        return savedSale
    }

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
