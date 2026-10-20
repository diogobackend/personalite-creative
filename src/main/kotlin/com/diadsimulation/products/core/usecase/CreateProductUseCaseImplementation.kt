package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.port.input.CreateProductInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort
import java.math.BigDecimal

class CreateProductUseCaseImplementation(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : CreateProductInputPort {
    override fun execute(product: Product): Product {
        validate(product)

        require(!productRepositoryOutputPort.existsByCode(product.code)) {
            "Já existe um produto cadastrado com o código ${product.code}."
        }

        return productRepositoryOutputPort.save(product)
    }

    private fun validate(product: Product) {
        require(product.code.matches(Regex("\\d{3}"))) {
            "O código comercial deve conter exatamente 3 dígitos."
        }

        require(product.name.isNotBlank()) {
            "O nome do produto é obrigatório."
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
            require(product.prices.minOf { it.minimumQuantity } == product.minimumQuantity) {
                "A primeira faixa de preço deve corresponder à quantidade mínima do produto."
            }
        }
    }

    private fun validateFees(product: Product) {
        product.fees.forEach { fee ->
            require(fee.name.isNotBlank()) {
                "O nome da taxa é obrigatório."
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
}
