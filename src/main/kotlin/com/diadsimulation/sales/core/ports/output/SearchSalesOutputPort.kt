package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import com.diadsimulation.sales.core.domain.model.SaleSearchResult

interface SearchSalesOutputPort {
    fun search(
        filter: SaleSearchFilter,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): SaleSearchResult
}
