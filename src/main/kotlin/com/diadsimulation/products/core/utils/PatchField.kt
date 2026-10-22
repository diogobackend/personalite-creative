package com.diadsimulation.products.core.utils

sealed interface PatchField<out T> {
    data object Absent : PatchField<Nothing>

    data class Present<T>(
        val value: T,
    ) : PatchField<T>
}
