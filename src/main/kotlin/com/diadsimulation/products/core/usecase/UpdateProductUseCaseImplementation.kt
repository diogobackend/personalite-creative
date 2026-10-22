package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.exceptions.ProductNotFoundException
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import com.diadsimulation.products.core.domain.model.UpdateProductCommand
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
                prices = updatePrices(product.prices, request, now),
                fees = updateFees(product.fees, request, now),
                updatedAt = now,
            )

        validate(updatedProduct)

        return productRepositoryOutputPort.update(updatedProduct)
    }

    private fun updatePrices(
        current: List<ProductPrice>,
        request: UpdateProductCommand,
        now: OffsetDateTime,
    ): List<ProductPrice> {
        val updates = request.prices.orAbsent().orEmpty()
        val removals = request.removePriceIds

        requireUniqueIds(updates.map { it.id }, "faixa de preço")
        requireUniqueIds(removals, "faixa de preço")

        val currentIds = current.map { it.id }.toSet()

        require(updates.all { it.id in currentIds }) {
            "Uma ou mais faixas de preço não foram encontradas."
        }

        require(removals.all { it in currentIds }) {
            "Uma ou mais faixas de preço não foram encontradas."
        }

        require(updates.none { it.id in removals }) {
            "Não é permitido editar e excluir a mesma faixa de preço."
        }

        val updatesById = updates.associateBy { it.id }
        val removedIds = removals.toSet()

        val retained =
            current
                .filterNot { it.id in removedIds }
                .map { price ->
                    val update =
                        updatesById[price.id]
                            ?: return@map price

                    val minimumQuantity =
                        update.minimumQuantity.resolve(price.minimumQuantity)
                    val unitPrice =
                        update.unitPrice.resolve(price.unitPrice)

                    if (
                        minimumQuantity == price.minimumQuantity &&
                        unitPrice.compareTo(price.unitPrice) == 0
                    ) {
                        price
                    } else {
                        price.copy(
                            minimumQuantity = minimumQuantity,
                            unitPrice = unitPrice,
                            updatedAt = now,
                        )
                    }
                }

        val additions =
            request.addPrices.map { price ->
                ProductPrice(
                    minimumQuantity = price.minimumQuantity,
                    unitPrice = price.unitPrice,
                    createdAt = now,
                    updatedAt = now,
                )
            }

        return retained + additions
    }

    private fun updateFees(
        current: List<ProductFee>,
        request: UpdateProductCommand,
        now: OffsetDateTime,
    ): List<ProductFee> {
        val updates = request.fees.orAbsent().orEmpty()
        val removals = request.removeFeeIds

        requireUniqueIds(updates.map { it.id }, "taxa")
        requireUniqueIds(removals, "taxa")

        val currentIds = current.map { it.id }.toSet()

        require(updates.all { it.id in currentIds }) {
            "Uma ou mais taxas não foram encontradas."
        }

        require(removals.all { it in currentIds }) {
            "Uma ou mais taxas não foram encontradas."
        }

        require(updates.none { it.id in removals }) {
            "Não é permitido editar e excluir a mesma taxa."
        }

        val updatesById = updates.associateBy { it.id }
        val removedIds = removals.toSet()

        val retained =
            current
                .filterNot { it.id in removedIds }
                .map { fee ->
                    val update =
                        updatesById[fee.id]
                            ?: return@map fee

                    val name = update.name.resolve(fee.name)
                    val amount = update.amount.resolve(fee.amount)
                    val chargeType = update.chargeType.resolve(fee.chargeType)
                    val required = update.required.resolve(fee.required)
                    val active = update.active.resolve(fee.active)

                    if (
                        name == fee.name &&
                        amount.compareTo(fee.amount) == 0 &&
                        chargeType == fee.chargeType &&
                        required == fee.required &&
                        active == fee.active
                    ) {
                        fee
                    } else {
                        fee.copy(
                            name = name,
                            amount = amount,
                            chargeType = chargeType,
                            required = required,
                            active = active,
                            updatedAt = now,
                        )
                    }
                }

        val additions =
            request.addFees.map { fee ->
                ProductFee(
                    name = fee.name,
                    amount = fee.amount,
                    chargeType = fee.chargeType,
                    required = fee.required,
                    active = fee.active,
                    createdAt = now,
                    updatedAt = now,
                )
            }

        return retained + additions
    }

    private fun validate(product: Product) {
        require(product.name.isNotBlank()) {
            "O nome do produto é obrigatório."
        }

        require(product.name.length <= 150) {
            "O nome do produto deve conter no máximo 150 caracteres."
        }

        require(product.minimumQuantity == null || product.minimumQuantity > 0) {
            "A quantidade mínima deve ser maior que zero."
        }

        validatePrices(product)
        validateFees(product)
        validateLeadTime(product)
    }

    private fun validatePrices(product: Product) {
        when (product.pricingType) {
            PricingType.FIXED -> {
                require(product.prices.size == 1) {
                    "Produtos com preço fixo devem possuir exatamente uma faixa de preço."
                }
                require(product.minimumQuantity != null) {
                    "Produtos com preço fixo devem possuir quantidade mínima."
                }
            }

            PricingType.TIERED -> {
                require(product.prices.isNotEmpty()) {
                    "Produtos com preço variável devem possuir pelo menos uma faixa."
                }
                require(product.minimumQuantity != null) {
                    "Produtos com preço variável devem possuir quantidade mínima."
                }
            }

            PricingType.QUOTE -> {
                require(product.prices.isEmpty()) {
                    "Produtos sob orçamento não devem possuir preços predefinidos."
                }
            }
        }

        product.prices.forEach { price ->
            require(price.minimumQuantity > 0) {
                "A quantidade mínima da faixa deve ser maior que zero."
            }
            require(price.unitPrice > BigDecimal.ZERO) {
                "O preço unitário deve ser maior que zero."
            }
        }

        require(
            product.prices
                .map { it.minimumQuantity }
                .distinct()
                .size ==
                product.prices.size,
        ) {
            "Não é permitido cadastrar faixas de preço duplicadas."
        }

        if (product.pricingType != PricingType.QUOTE) {
            require(
                product.prices.minOf { it.minimumQuantity } ==
                    product.minimumQuantity,
            ) {
                "A primeira faixa de preço deve corresponder à quantidade mínima do produto."
            }
        }
    }

    private fun validateFees(product: Product) {
        product.fees.forEach { fee ->
            require(fee.name.isNotBlank()) {
                "O nome da taxa é obrigatório."
            }
            require(fee.name.length <= 100) {
                "O nome da taxa deve conter no máximo 100 caracteres."
            }
            require(fee.amount >= BigDecimal.ZERO) {
                "O valor da taxa não pode ser negativo."
            }
        }
    }

    private fun validateLeadTime(product: Product) {
        product.shelfLifeDays?.let {
            require(it > 0) {
                "A validade deve ser maior que zero."
            }
        }

        product.productionLeadTimeMinDays?.let {
            require(it >= 0) {
                "O prazo mínimo não pode ser negativo."
            }
        }

        product.productionLeadTimeMaxDays?.let {
            require(it >= 0) {
                "O prazo máximo não pode ser negativo."
            }
        }

        val minimum = product.productionLeadTimeMinDays
        val maximum = product.productionLeadTimeMaxDays

        if (minimum != null && maximum != null) {
            require(maximum >= minimum) {
                "O prazo máximo não pode ser inferior ao prazo mínimo."
            }
        }
    }

    private fun requireUniqueIds(
        ids: List<UUID>,
        item: String,
    ) {
        require(ids.distinct().size == ids.size) {
            "Não é permitido informar o mesmo ID de $item mais de uma vez."
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
