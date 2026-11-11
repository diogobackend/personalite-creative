package com.diadsimulation.movements.core.domain.models

data class MovementPage(
    val content: List<Movement>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
)
