package com.diadsimulation.sales.core.domain.model

data class SaleSearchResult(
    val sales: List<Sale>,
    val totalElements: Long,
    val totalPages: Int,
    val page: Int,
    val size: Int,
    val summary: SaleFinancialSummary,
)
