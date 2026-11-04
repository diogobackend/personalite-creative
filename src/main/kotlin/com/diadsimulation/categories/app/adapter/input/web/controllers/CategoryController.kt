package com.diadsimulation.categories.app.adapter.input.web.controllers

import com.diadsimulation.categories.app.adapter.input.web.dtos.requests.CreateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.web.dtos.requests.UpdateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.web.dtos.responses.CategoryResponse
import com.diadsimulation.categories.app.adapter.input.web.mappers.CategoryWebMapper
import com.diadsimulation.categories.app.adapter.input.web.swagger.CategoryApi
import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.ports.input.CreateCategoryInputPort
import com.diadsimulation.categories.core.ports.input.DeleteCategoryInputPort
import com.diadsimulation.categories.core.ports.input.GetCategoriesInputPort
import com.diadsimulation.categories.core.ports.input.UpdateCategoryInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val createCategoryInputPort: CreateCategoryInputPort,
    private val categoryWebMapper: CategoryWebMapper,
    private val getCategoriesInputPort: GetCategoriesInputPort,
    private val updateCategoryInputPort: UpdateCategoryInputPort,
    private val deleteCategoryInputPort: DeleteCategoryInputPort,
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

    @GetMapping
    override fun findAll(
        name: String?,
        type: CategoryType?,
    ): ResponseEntity<List<CategoryResponse>> =
        ResponseEntity.ok(
            getCategoriesInputPort
                .execute(name, type)
                .map(categoryWebMapper::toResponse),
        )

    @PatchMapping("/{id}")
    override fun update(
        id: UUID,
        request: UpdateCategoryRequest,
    ): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(
            categoryWebMapper.toResponse(
                updateCategoryInputPort.execute(id, request.name),
            ),
        )

    @DeleteMapping("/{id}")
    override fun delete(id: UUID): ResponseEntity<Void> {
        deleteCategoryInputPort.execute(id)
        return ResponseEntity.noContent().build()
    }
}
