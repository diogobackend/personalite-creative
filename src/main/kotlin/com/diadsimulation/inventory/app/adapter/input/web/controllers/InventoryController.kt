package com.diadsimulation.inventory.app.adapter.input.web.controllers
import com.diadsimulation.inventory.app.adapter.input.web.mapper.RegisterInventoryMapper
import com.diadsimulation.inventory.app.adapter.input.web.request.RegisterInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.response.RegisterInventoryResponse
import com.diadsimulation.inventory.app.adapter.input.web.swagger.InventoryApi
import com.diadsimulation.inventory.core.ports.input.RegisterInventoryInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inventory")
class InventoryController(
    private val registerInventoryInputPort: RegisterInventoryInputPort,
) : InventoryApi {
    override fun register(request: RegisterInventoryRequest): ResponseEntity<RegisterInventoryResponse> {
        val inventory = RegisterInventoryMapper.toDomain(request)
        val registeredInventory = registerInventoryInputPort.execute(inventory)

        return ResponseEntity
            .status(CREATED)
            .body(RegisterInventoryMapper.toResponse(registeredInventory))
    }
}
