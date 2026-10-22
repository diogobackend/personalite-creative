package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.exceptions.ProductNotFoundException
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import com.diadsimulation.products.core.domain.model.UpdateProductCommand
import com.diadsimulation.products.core.domain.model.UpdateProductFeeCommand
import com.diadsimulation.products.core.domain.model.UpdateProductPriceCommand
import com.diadsimulation.products.core.port.input.UpdateProductInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import com.diadsimulation.products.core.utils.PatchField
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

class UpdateProductUseCaseImplementation(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : UpdateProductInputPort {
    override fun execute(
        productId: UUID,
        request: UpdateProductCommand,
    ): Product {
        val product =
            productRepositoryOutputPort.findById(productId)
                ?: throw ProductNotFoundException(productId)

        val now = OffsetDateTime.now()

        val updatedProduct =
            product.copy(
                name = request.name.resolve(product.name),
                category = request.category.resolve(product.category),
                description = request.description.resolve(product.description),
                minimumQuantity = request.minimumQuantity.resolve(product.minimumQuantity),
                pricingType = request.pricingType.resolve(product.pricingType),
                shelfLifeDays = request.shelfLifeDays.resolve(product.shelfLifeDays),
                productionLeadTimeMinDays =
                    request.productionLeadTimeMinDays.resolve(product.productionLeadTimeMinDays),
                productionLeadTimeMaxDays =
                    request.productionLeadTimeMaxDays.resolve(product.productionLeadTimeMaxDays),
                active = request.active.resolve(product.active),
                prices = updatePrices(product.prices, request.prices, now),
                fees = updateFees(product.fees, request.fees, now),
                updatedAt = now,
            )

        validate(updatedProduct)

        return productRepositoryOutputPort.update(updatedProduct)
    }

    private fun updatePrices(
        current: List<ProductPrice>,
        changes: PatchField<List<UpdateProductPriceCommand>>,
        now: OffsetDateTime,
    ): List<ProductPrice> {
        val updates = changes.orAbsent() ?: return current

        require(updates.map { it.id }.distinct().size == updates.size) {
            "Não é permitido informar a mesma faixa de preço duas vezes."
        }

        updates.forEach { update ->
            require(current.any { it.id == update.id }) {
                "Faixa de preço não encontrada: ${update.id}"
            }
        }

        return current.map { price ->
            val update =
                updates.find { it.id == price.id }
                    ?: return@map price

            price.copy(
                minimumQuantity = update.minimumQuantity.resolve(price.minimumQuantity),
                unitPrice = update.unitPrice.resolve(price.unitPrice),
                updatedAt = now,
            )
        }
    }

    private fun updateFees(
        current: List<ProductFee>,
        changes: PatchField<List<UpdateProductFeeCommand>>,
        now: OffsetDateTime,
    ): List<ProductFee> {
        val updates = changes.orAbsent() ?: return current

        require(updates.map { it.id }.distinct().size == updates.size) {
            "Não é permitido informar a mesma taxa duas vezes."
        }

        updates.forEach { update ->
            require(current.any { it.id == update.id }) {
                "Taxa não encontrada: ${update.id}"
            }
        }

        return current.map { fee ->
            val update =
                updates.find { it.id == fee.id }
                    ?: return@map fee

            fee.copy(
                name = update.name.resolve(fee.name),
                amount = update.amount.resolve(fee.amount),
                chargeType = update.chargeType.resolve(fee.chargeType),
                required = update.required.resolve(fee.required),
                active = update.active.resolve(fee.active),
                updatedAt = now,
            )
        }
    }

    private fun validate(product: Product) {
        require(product.name.isNotBlank()) {
            "O nome do produto é obrigatório."
        }

        require(product.minimumQuantity == null || product.minimumQuantity > 0) {
            "A quantidade mínima deve ser maior que zero."
        }

        product.prices.forEach {
            require(it.minimumQuantity > 0) {
                "A quantidade mínima da faixa deve ser maior que zero."
            }
            require(it.unitPrice > BigDecimal.ZERO) {
                "O preço unitário deve ser maior que zero."
            }
        }

        require(
            product.prices
                .map { it.minimumQuantity }
                .distinct()
                .size == product.prices.size,
        ) {
            "Não é permitido cadastrar faixas de preço duplicadas."
        }

        when (product.pricingType) {
            PricingType.FIXED -> {
                require(product.prices.size == 1 && product.minimumQuantity != null) {
                    "Produtos com preço fixo devem possuir uma faixa e quantidade mínima."
                }
            }

            PricingType.TIERED -> {
                require(product.prices.isNotEmpty() && product.minimumQuantity != null) {
                    "Produtos com preço variável devem possuir faixas e quantidade mínima."
                }
            }

            PricingType.QUOTE -> {
                require(product.prices.isEmpty()) {
                    "Produtos sob orçamento não devem possuir preços predefinidos."
                }
            }
        }

        if (product.pricingType != PricingType.QUOTE) {
            require(product.prices.minOf { it.minimumQuantity } == product.minimumQuantity) {
                "A primeira faixa de preço deve corresponder à quantidade mínima do produto."
            }
        }

        product.fees.forEach {
            require(it.name.isNotBlank() && it.amount >= BigDecimal.ZERO) {
                "Taxa inválida."
            }
        }

        product.shelfLifeDays?.let { require(it > 0) }
        product.productionLeadTimeMinDays?.let { require(it >= 0) }
        product.productionLeadTimeMaxDays?.let { require(it >= 0) }

        val min = product.productionLeadTimeMinDays
        val max = product.productionLeadTimeMaxDays

        if (min != null && max != null) {
            require(max >= min) {
                "O prazo máximo não pode ser inferior ao prazo mínimo."
            }
        }
    }

    private fun <T> PatchField<T>.resolve(current: T): T =
        when (this) {
            PatchField.Absent -> current
            is PatchField.Present -> value
        }

    private fun <T> PatchField<T>.orAbsent(): T? =
        when (this) {
            PatchField.Absent -> null
            is PatchField.Present -> value
        }
}
