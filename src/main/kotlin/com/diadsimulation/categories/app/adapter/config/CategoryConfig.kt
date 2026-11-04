package com.diadsimulation.categories.app.adapter.config

import com.diadsimulation.categories.core.ports.input.CreateCategoryInputPort
import com.diadsimulation.categories.core.ports.input.GetCategoriesInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import com.diadsimulation.categories.core.usecase.CreateCategoryUseCaseImplementation
import com.diadsimulation.categories.core.usecase.GetCategoriesUseCaseImplementation
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
}
