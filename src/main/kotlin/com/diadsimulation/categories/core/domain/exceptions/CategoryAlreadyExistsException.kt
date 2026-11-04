package com.diadsimulation.categories.core.domain.exceptions

class CategoryAlreadyExistsException :
    RuntimeException(
        "Já existe uma categoria com este nome e tipo.",
    )
