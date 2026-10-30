package com.diadsimulation.sales.app.adapter.input.web.controller

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.SearchSalesRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SearchSalesResponse
import com.diadsimulation.sales.app.adapter.input.web.mappers.SaleWebMapper
import com.diadsimulation.sales.app.adapter.input.web.mappers.SearchSalesWebMapper
import com.diadsimulation.sales.app.adapter.input.web.swagger.SaleApi
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import com.diadsimulation.sales.core.usecase.SearchSalesUseCaseImplementation
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sales")
class SaleController(
    private val createSaleInputPort: CreateSaleInputPort,
    private val saleWebMapper: SaleWebMapper,
    private val searchSalesUseCaseImplementation: SearchSalesUseCaseImplementation,
    private val searchSalesWebMapper: SearchSalesWebMapper,
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
}
