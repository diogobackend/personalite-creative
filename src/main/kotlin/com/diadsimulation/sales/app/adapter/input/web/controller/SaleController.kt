package com.diadsimulation.sales.app.adapter.input.web.controller

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleResponse
import com.diadsimulation.sales.app.adapter.input.web.mappers.SaleWebMapper
import com.diadsimulation.sales.app.adapter.input.web.swagger.SaleApi
import com.diadsimulation.sales.core.ports.input.CreateSaleInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sales")
class SaleController(
    private val createSaleInputPort: CreateSaleInputPort,
    private val saleWebMapper: SaleWebMapper,
) : SaleApi {
    @PostMapping
    override fun create(request: CreateSaleRequest): ResponseEntity<CreateSaleResponse> {
        val sale = saleWebMapper.toDomain(request)
        val createdSale = createSaleInputPort.execute(sale)

        return ResponseEntity
            .status(CREATED)
            .body(saleWebMapper.toResponse(createdSale))
    }
}
