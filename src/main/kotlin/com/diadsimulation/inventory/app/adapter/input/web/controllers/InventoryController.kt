package com.diadsimulation.inventory.app.adapter.input.web.controllers
import com.diadsimulation.inventory.app.adapter.input.web.dtos.request.RegisterInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.dtos.request.UpdateInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.dtos.response.InventoryPageResponse
import com.diadsimulation.inventory.app.adapter.input.web.dtos.response.RegisterInventoryResponse
import com.diadsimulation.inventory.app.adapter.input.web.mapper.RegisterInventoryMapper
import com.diadsimulation.inventory.app.adapter.input.web.swagger.InventoryApi
import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.inventory.core.domain.model.UpdateInventory
import com.diadsimulation.inventory.core.ports.input.GetInventoryByIdInputPort
import com.diadsimulation.inventory.core.ports.input.GetInventoryInputPort
import com.diadsimulation.inventory.core.ports.input.RegisterInventoryInputPort
import com.diadsimulation.inventory.core.ports.input.UpdateInventoryInputPort
import com.diadsimulation.products.core.domain.enums.ProductCategory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/inventory")
class InventoryController(
    private val registerInventoryInputPort: RegisterInventoryInputPort,
    private val getInventoryInputPort: GetInventoryInputPort,
    private val getInventoryByIdInputPort: GetInventoryByIdInputPort,
    private val updateInventoryInputPort: UpdateInventoryInputPort,
) : InventoryApi {
    override fun register(request: RegisterInventoryRequest): ResponseEntity<RegisterInventoryResponse> {
        val inventory = RegisterInventoryMapper.toDomain(request)
        val registeredInventory = registerInventoryInputPort.execute(inventory)

        return ResponseEntity
            .status(CREATED)
            .body(RegisterInventoryMapper.toResponse(registeredInventory))
    }

    override fun getInventory(
        sku: String?,
        name: String?,
        category: ProductCategory?,
        description: String?,
        quantity: Int?,
        minQuantity: Int?,
        maxQuantity: Int?,
        unit: InventoryUnit?,
        color: String?,
        size: InventorySize?,
        material: String?,
        brand: String?,
        model: String?,
        createdAt: LocalDate?,
        createdFrom: LocalDate?,
        createdTo: LocalDate?,
        page: Int,
        pageSize: Int,
        sortBy: String,
        sortDirection: String,
    ): ResponseEntity<InventoryPageResponse> {
        val filter =
            GetInventoryFilter(
                sku = sku,
                name = name,
                category = category,
                description = description,
                quantity = quantity,
                minQuantity = minQuantity,
                maxQuantity = maxQuantity,
                unit = unit,
                color = color,
                size = size,
                material = material,
                brand = brand,
                model = model,
                createdAt = createdAt,
                createdFrom = createdFrom,
                createdTo = createdTo,
                page = page,
                pageSize = pageSize,
                sortBy = sortBy,
                sortDirection = sortDirection,
            )

        val result = getInventoryInputPort.execute(filter)

        return ResponseEntity.ok(
            InventoryPageResponse(
                content = result.content.map(RegisterInventoryMapper::toResponse),
                page = result.page,
                size = result.size,
                totalElements = result.totalElements,
                totalElementsThisPage = result.totalElementsThisPage,
                totalPages = result.totalPages,
            ),
        )
    }

    override fun getInventoryById(id: UUID): ResponseEntity<RegisterInventoryResponse> {
        val inventory = getInventoryByIdInputPort.execute(id)

        return ResponseEntity.ok(
            RegisterInventoryMapper.toResponse(inventory),
        )
    }

    override fun updateInventory(
        id: UUID,
        request: UpdateInventoryRequest,
    ): ResponseEntity<RegisterInventoryResponse> {
        val update =
            UpdateInventory(
                name = request.name,
                category = request.category,
                description = request.description,
                quantity = request.quantity,
                minimumQuantity = request.minimumQuantity,
                unit = request.unit,
                color = request.color,
                size = request.size,
                material = request.material,
                brand = request.brand,
                model = request.model,
            )

        val updatedInventory = updateInventoryInputPort.execute(id, update)

        return ResponseEntity.ok(
            RegisterInventoryMapper.toResponse(updatedInventory),
        )
    }
}
