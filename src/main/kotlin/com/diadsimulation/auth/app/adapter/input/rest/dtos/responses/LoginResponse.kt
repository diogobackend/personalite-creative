package com.diadsimulation.auth.app.adapter.input.rest.dtos.responses

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
)
