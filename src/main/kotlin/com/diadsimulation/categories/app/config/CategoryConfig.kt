package com.diadsimulation.categories.app.config

import com.diadsimulation.categories.core.ports.input.CreateCategoryInputPort
import com.diadsimulation.categories.core.ports.input.DeleteCategoryInputPort
import com.diadsimulation.categories.core.ports.input.GetCategoriesInputPort
import com.diadsimulation.categories.core.ports.input.UpdateCategoryInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import com.diadsimulation.categories.core.usecase.CreateCategoryUseCaseImplementation
import com.diadsimulation.categories.core.usecase.DeleteCategoryUseCaseImplementation
import com.diadsimulation.categories.core.usecase.GetCategoriesUseCaseImplementation
import com.diadsimulation.categories.core.usecase.UpdateCategoryUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CategoryConfig {
    @Bean
    fun createCategory(categoryRepositoryOutputPort: CategoryRepositoryOutputPort): CreateCategoryInputPort =
        CreateCategoryUseCaseImplementation(categoryRepositoryOutputPort)

    @Bean
    fun getCategoriesInputPort(categoryRepositoryOutputPort: CategoryRepositoryOutputPort): GetCategoriesInputPort =
        GetCategoriesUseCaseImplementation(categoryRepositoryOutputPort)

    @Bean
    fun updateCategoryInputPort(categoryRepositoryOutputPort: CategoryRepositoryOutputPort): UpdateCategoryInputPort =
        UpdateCategoryUseCaseImplementation(categoryRepositoryOutputPort)

    @Bean
    fun deleteCategoryInputPort(categoryRepositoryOutputPort: CategoryRepositoryOutputPort): DeleteCategoryInputPort =
        DeleteCategoryUseCaseImplementation(categoryRepositoryOutputPort)
}
