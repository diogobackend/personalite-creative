package com.diadsimulation.products.app.adapter.input.web.controllers

import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import com.diadsimulation.products.app.adapter.input.web.mappers.ProductWebMapper
import com.diadsimulation.products.app.adapter.input.web.swagger.ProductApi
import com.diadsimulation.products.core.port.input.CreateProductInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val createProductInputPort: CreateProductInputPort,
    private val productWebMapper: ProductWebMapper,
) : ProductApi {
    @PostMapping
    override fun create(
        @Valid @RequestBody request: CreateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productWebMapper.toDomain(request)
        val createdProduct = createProductInputPort.execute(product)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productWebMapper.toResponse(createdProduct))
    }
}
