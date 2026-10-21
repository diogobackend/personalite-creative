
package com.diadsimulation.products.app.adapter.input.web.controllers

import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductsPageResponse
import com.diadsimulation.products.app.adapter.input.web.mappers.ProductWebMapper
import com.diadsimulation.products.app.adapter.input.web.swagger.ProductApi
import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import com.diadsimulation.products.core.domain.model.GetProductsFilter
import com.diadsimulation.products.core.port.input.CreateProductInputPort
import com.diadsimulation.products.core.port.input.GetProductByIdInputPort
import com.diadsimulation.products.core.port.input.GetProductsInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@RestController
@RequestMapping("/products")
class ProductController(
    private val createProductInputPort: CreateProductInputPort,
    private val getProductsInputPort: GetProductsInputPort,
    private val productWebMapper: ProductWebMapper,
    private val getProductByIdInputPort: GetProductByIdInputPort,
) : ProductApi {
    @PostMapping
    override fun create(request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val product = productWebMapper.toDomain(request)
        val createdProduct = createProductInputPort.execute(product)

        return ResponseEntity
            .status(CREATED)
            .body(productWebMapper.toResponse(createdProduct))
    }

    @GetMapping
    override fun getProducts(
        code: String?,
        name: String?,
        description: String?,
        category: ProductCategory?,
        active: Boolean?,
        pricingType: PricingType?,
        feeName: String?,
        chargeType: FeeChargeType?,
        minQuantity: Int?,
        maxQuantity: Int?,
        minShelfLifeDays: Int?,
        maxShelfLifeDays: Int?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        minFeeAmount: BigDecimal?,
        maxFeeAmount: BigDecimal?,
        hasFees: Boolean?,
        createdFrom: OffsetDateTime?,
        createdTo: OffsetDateTime?,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): ResponseEntity<ProductsPageResponse> {
        val filter =
            GetProductsFilter(
                code = code,
                name = name,
                description = description,
                category = category,
                active = active,
                pricingType = pricingType,
                feeName = feeName,
                chargeType = chargeType,
                minQuantity = minQuantity,
                maxQuantity = maxQuantity,
                minShelfLifeDays = minShelfLifeDays,
                maxShelfLifeDays = maxShelfLifeDays,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minFeeAmount = minFeeAmount,
                maxFeeAmount = maxFeeAmount,
                hasFees = hasFees,
                createdFrom = createdFrom,
                createdTo = createdTo,
                page = page,
                size = size,
                sortBy = sortBy,
                sortDirection = sortDirection,
            )

        val productsPage = getProductsInputPort.execute(filter)

        return ResponseEntity.ok(
            productWebMapper.toPageResponse(productsPage),
        )
    }

    @GetMapping("/{productId}")
    override fun getProductById(productId: UUID): ResponseEntity<ProductResponse> {
        val product = getProductByIdInputPort.execute(productId)

        return ResponseEntity.ok(
            productWebMapper.toResponse(product),
        )
    }
}
