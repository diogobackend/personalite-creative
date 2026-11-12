package com.diadsimulation.movements.app.adapter.input.rest.dtos.responses

data class MovementsPageResponse(
    val content: List<MovementResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
)
