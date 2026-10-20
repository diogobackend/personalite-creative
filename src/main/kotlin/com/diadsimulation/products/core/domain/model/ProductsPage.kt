package com.diadsimulation.products.core.domain.model

data class ProductsPage(
    val content: List<Product>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalElementsThisPage: Int,
    val totalPages: Int,
)
