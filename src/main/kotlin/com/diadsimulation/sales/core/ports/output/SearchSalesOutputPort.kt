package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SaleFinancialSummary
import com.diadsimulation.sales.core.domain.model.SaleSearchFilter

interface SearchSalesOutputPort {
    fun search(
        filter: SaleSearchFilter,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): SaleSearchResult
}

data class SaleSearchResult(
    val sales: List<Sale>,
    val totalElements: Long,
    val totalPages: Int,
    val page: Int,
    val size: Int,
    val summary: SaleFinancialSummary,
)
