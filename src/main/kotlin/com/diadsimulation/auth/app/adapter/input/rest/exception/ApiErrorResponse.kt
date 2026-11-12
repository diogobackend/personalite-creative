package com.diadsimulation.auth.app.adapter.input.rest.exception

import java.time.OffsetDateTime

data class ApiErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
)
