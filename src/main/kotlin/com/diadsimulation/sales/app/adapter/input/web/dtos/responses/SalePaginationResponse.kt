package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

data class SalePaginationResponse(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
)
