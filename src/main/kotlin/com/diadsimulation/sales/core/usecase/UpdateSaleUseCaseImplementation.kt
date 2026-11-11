package com.diadsimulation.sales.core.usecase

import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.RECEIPT
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.REFUND
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.VALUE_ADJUSTMENT_DECREASE
import com.diadsimulation.sales.core.domain.enums.SalePaymentType.VALUE_ADJUSTMENT_INCREASE
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
import java.math.RoundingMode.HALF_UP
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

        require(downPaymentPercentage in ZERO..HUNDRED) {
            "A porcentagem de entrada deve estar entre 0 e 100."
        }

        val financialOperation = update.financialOperation

        require(
            financialOperation == null ||
                financialOperation == RECEIPT ||
                financialOperation == REFUND,
        ) {
            "A operação financeira deve ser RECEIPT ou REFUND."
        }

        require(update.receivedAmount == null || financialOperation != null) {
            "Informe RECEIPT ou REFUND para alterar o valor recebido."
        }

        val receivedAmount =
            update.receivedAmount ?: currentSale.receivedAmount

        require(receivedAmount >= ZERO) {
            "O valor recebido não pode ser negativo."
        }

        require(receivedAmount <= totalAmount) {
            "O valor recebido não pode ultrapassar o total da venda."
        }

        require(receivedAmount.scale() <= 2) {
            "O valor recebido deve possuir no máximo duas casas decimais."
        }

        val financialDifference =
            receivedAmount.subtract(currentSale.receivedAmount)

        if (financialOperation != null) {
            require(financialDifference.compareTo(ZERO) != 0) {
                "A operação financeira deve alterar o valor recebido."
            }

            require(
                when (financialOperation) {
                    RECEIPT -> financialDifference > ZERO
                    REFUND -> financialDifference < ZERO
                    else -> false
                },
            ) {
                "A operação financeira não corresponde à alteração do valor recebido."
            }
        }

        val updatedAt = OffsetDateTime.now()

        val updatedSale =
            currentSale.copy(
                customerId = customerId,
                items = items,
                totalAmount = totalAmount,
                downPaymentPercentage = downPaymentPercentage,
                receivedAmount = receivedAmount,
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

        val totalDifference =
            updatedSale.totalAmount.subtract(currentSale.totalAmount)

        val savedSale = updateSaleOutputPort.update(updatedSale)

        if (totalDifference.compareTo(ZERO) != 0) {
            salePaymentRepositoryOutputPort.save(
                SalePayment(
                    saleId = savedSale.id,
                    amount = totalDifference.abs(),
                    type =
                        if (totalDifference > ZERO) {
                            VALUE_ADJUSTMENT_INCREASE
                        } else {
                            VALUE_ADJUSTMENT_DECREASE
                        },
                    paidAt = updatedAt,
                ),
            )
        }

        if (financialDifference.compareTo(ZERO) != 0) {
            salePaymentRepositoryOutputPort.save(
                SalePayment(
                    saleId = savedSale.id,
                    amount = financialDifference.abs(),
                    type = requireNotNull(financialOperation),
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
