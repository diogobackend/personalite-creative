package com.diadsimulation.sales.core.domain.utils

sealed interface UpdateField<out T> {
    data object Unchanged : UpdateField<Nothing>

    data class Set<T>(
        val value: T,
    ) : UpdateField<T>
}
