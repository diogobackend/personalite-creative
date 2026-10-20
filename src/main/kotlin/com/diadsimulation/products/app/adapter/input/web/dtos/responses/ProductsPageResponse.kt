package com.diadsimulation.products.app.adapter.input.web.dtos.responses

data class ProductsPageResponse(
    val content: List<ProductResponse>,
    val page: Int,
    val size: Int,
    val totalElementsThisPage: Long,
    val totalElements: Long,
    val totalPages: Int,
)
