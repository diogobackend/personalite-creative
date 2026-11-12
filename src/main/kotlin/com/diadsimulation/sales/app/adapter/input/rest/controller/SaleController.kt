package com.diadsimulation.sales.app.adapter.input.rest.controller

import com.diadsimulation.sales.app.adapter.input.rest.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.rest.dtos.requests.SearchSalesRequest
import com.diadsimulation.sales.app.adapter.input.rest.dtos.requests.UpdateSaleRequest
import com.diadsimulation.sales.app.adapter.input.rest.dtos.responses.CreateSaleResponse
import com.diadsimulation.sales.app.adapter.input.rest.dtos.responses.SaleSummaryResponse
import com.diadsimulation.sales.app.adapter.input.rest.dtos.responses.SearchSalesResponse
import com.diadsimulation.sales.app.adapter.input.rest.mappers.SaleWebMapper
import com.diadsimulation.sales.app.adapter.input.rest.mappers.SearchSalesWebMapper
import com.diadsimulation.sales.app.adapter.input.rest.swagger.SaleApi
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import com.diadsimulation.sales.core.ports.input.DeleteSaleInputPort
import com.diadsimulation.sales.core.ports.input.FindSaleByIdInputPort
import com.diadsimulation.sales.core.ports.input.UpdateSaleInputPort
import com.diadsimulation.sales.core.usecase.SearchSalesUseCaseImplementation
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/sales")
class SaleController(
    private val createSaleInputPort: CreateSaleInputPort,
    private val saleWebMapper: SaleWebMapper,
    private val searchSalesUseCaseImplementation: SearchSalesUseCaseImplementation,
    private val searchSalesWebMapper: SearchSalesWebMapper,
    private val findSaleByIdInputPort: FindSaleByIdInputPort,
    private val updateSaleInputPort: UpdateSaleInputPort,
    private val deleteSaleInputPort: DeleteSaleInputPort,
) : SaleApi {
    @PostMapping
    override fun create(request: CreateSaleRequest): ResponseEntity<CreateSaleResponse> {
        val sale = saleWebMapper.toDomain(request)
        val createdSale = createSaleInputPort.execute(sale)

        return ResponseEntity
            .status(CREATED)
            .body(saleWebMapper.toResponse(createdSale))
    }

    @GetMapping
    override fun searchSales(
        request: SearchSalesRequest,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): ResponseEntity<SearchSalesResponse> {
        val result =
            searchSalesUseCaseImplementation.execute(
                filter = request.toFilter(),
                page = page,
                size = size,
                sortBy = sortBy,
                sortDirection = sortDirection,
            )

        return ResponseEntity.ok(
            searchSalesWebMapper.toResponse(result),
        )
    }

    @GetMapping("/{id}")
    override fun findById(id: UUID): ResponseEntity<SaleSummaryResponse> {
        val sale = findSaleByIdInputPort.execute(id)

        return ResponseEntity.ok(
            searchSalesWebMapper.toSaleResponse(sale, LocalDate.now()),
        )
    }

    @PatchMapping("/{id}")
    override fun update(
        id: UUID,
        request: UpdateSaleRequest,
    ): ResponseEntity<CreateSaleResponse> {
        val update = saleWebMapper.toUpdateDomain(request)
        val updatedSale = updateSaleInputPort.execute(id, update)

        return ResponseEntity.ok(
            saleWebMapper.toResponse(updatedSale),
        )
    }

    @DeleteMapping("/{id}")
    override fun delete(id: UUID): ResponseEntity<Void> {
        deleteSaleInputPort.execute(id)

        return ResponseEntity.noContent().build()
    }
}
