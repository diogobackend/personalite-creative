package com.diadsimulation.inventory.core.domain.model

data class InventoryPage(
    val content: List<Inventory>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalElementsThisPage: Int,
    val totalPages: Int,
)
