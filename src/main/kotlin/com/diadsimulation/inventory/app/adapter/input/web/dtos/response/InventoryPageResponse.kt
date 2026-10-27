package com.diadsimulation.inventory.app.adapter.input.web.dtos.response

data class InventoryPageResponse(
    val content: List<RegisterInventoryResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalElementsThisPage: Int,
    val totalPages: Int,
)
