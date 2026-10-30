package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

data class SearchSalesResponse(
    val sales: List<SaleSummaryResponse>,
    val pagination: SalePaginationResponse,
    val summary: SaleFinancialSummaryResponse,
)
