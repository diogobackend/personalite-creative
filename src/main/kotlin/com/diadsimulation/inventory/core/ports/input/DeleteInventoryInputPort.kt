package com.diadsimulation.inventory.core.ports.input

import java.util.UUID

fun interface DeleteInventoryInputPort {
    fun execute(id: UUID)
}
