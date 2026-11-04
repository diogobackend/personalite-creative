package com.diadsimulation.categories.app.adapter.input.web.controllers

import com.diadsimulation.categories.app.adapter.input.web.dtos.requests.CreateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.web.dtos.responses.CategoryResponse
import com.diadsimulation.categories.app.adapter.input.web.mappers.CategoryWebMapper
import com.diadsimulation.categories.app.adapter.input.web.swagger.CategoryApi
import com.diadsimulation.categories.core.ports.input.CreateCategoryInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val createCategoryInputPort: CreateCategoryInputPort,
    private val categoryWebMapper: CategoryWebMapper,
) : CategoryApi {
    @PostMapping
    override fun create(request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val category =
            createCategoryInputPort.execute(
                name = request.name,
                type = request.type,
            )

        return ResponseEntity
            .status(CREATED)
            .body(categoryWebMapper.toResponse(category))
    }
}
