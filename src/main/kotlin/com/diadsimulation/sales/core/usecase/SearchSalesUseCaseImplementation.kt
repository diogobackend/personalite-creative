package com.diadsimulation.sales.core.usecase

import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import com.diadsimulation.sales.core.domain.model.SaleSearchResult
import com.diadsimulation.sales.core.ports.output.SearchSalesOutputPort
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class SearchSalesUseCaseImplementation(
    private val searchSalesOutputPort: SearchSalesOutputPort,
) {
    fun execute(
        filter: SaleSearchFilter,
        page: Int = 0,
        size: Int = 20,
        sortBy: String = "createdAt",
        sortDirection: String = "DESC",
    ): SaleSearchResult {
        require(page >= 0) {
            "A página não pode ser negativa."
        }

        require(size in 1..100) {
            "O tamanho da página deve estar entre 1 e 100."
        }

        require(page.toLong() * size <= Int.MAX_VALUE) {
            "O intervalo da página excede o limite permitido."
        }

        require(sortBy in ALLOWED_SORT_FIELDS) {
            "Campo de ordenação inválido."
        }

        require(sortDirection.uppercase() in ALLOWED_DIRECTIONS) {
            "Direção de ordenação inválida."
        }

        validateFilter(filter)

        return searchSalesOutputPort.search(
            filter = filter,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection.uppercase(),
        )
    }

    private fun validateFilter(filter: SaleSearchFilter) {
        requireMonetaryRange(
            filter.minTotalAmount,
            filter.maxTotalAmount,
            "valor total",
        )

        requireMonetaryRange(
            filter.minRemainingAmount,
            filter.maxRemainingAmount,
            "valor restante",
        )

        requireRange(
            filter.deliveryDeadlineFrom,
            filter.deliveryDeadlineTo,
            "prazo de entrega",
        )

        requireRange(
            filter.createdAtFrom,
            filter.createdAtTo,
            "data de criação",
        )

        requirePercentageRange(
            filter.downPaymentFrom,
            filter.downPaymentTo,
            "percentual de entrada",
        )

        requirePercentageRange(
            filter.paidFrom,
            filter.paidTo,
            "percentual pago",
        )

        requirePercentageRange(
            filter.remainingFrom,
            filter.remainingTo,
            "percentual restante",
        )

        filter.dueWithinDays?.let {
            require(it >= 0) {
                "A quantidade de dias deve ser positiva ou zero."
            }
        }
    }

    private fun requireMonetaryRange(
        from: BigDecimal?,
        to: BigDecimal?,
        field: String,
    ) {
        listOfNotNull(from, to).forEach {
            require(it >= ZERO) {
                "O $field não pode ser negativo."
            }
        }

        requireRange(from, to, field)
    }

    private fun <T : Comparable<T>> requireRange(
        from: T?,
        to: T?,
        field: String,
    ) {
        if (from != null && to != null) {
            require(from <= to) {
                "O intervalo de $field é inválido."
            }
        }
    }

    private fun requirePercentageRange(
        from: BigDecimal?,
        to: BigDecimal?,
        field: String,
    ) {
        listOfNotNull(from, to).forEach {
            require(it >= ZERO && it <= HUNDRED) {
                "O $field deve estar entre 0 e 100."
            }
        }

        requireRange(from, to, field)
    }

    companion object {
        private val HUNDRED = BigDecimal("100")

        private val ALLOWED_SORT_FIELDS =
            setOf(
                "createdAt",
                "totalAmount",
                "remainingAmount",
                "deliveryDeadline",
                "paidPercentage",
            )

        private val ALLOWED_DIRECTIONS =
            setOf(
                "ASC",
                "DESC",
            )
    }
}
