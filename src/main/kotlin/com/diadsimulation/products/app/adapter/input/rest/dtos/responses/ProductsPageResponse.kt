package com.diadsimulation.products.app.adapter.input.rest.dtos.responses

data class ProductsPageResponse(
    val content: List<ProductResponse>,
    val page: Int,
    val size: Int,
    val totalElementsThisPage: Long,
    val totalElements: Long,
    val totalPages: Int,
)
