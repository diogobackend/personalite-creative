package com.diadsimulation.auth.core.usecase

import com.diadsimulation.auth.app.adapter.input.web.security.UserAccessAuthorization
import com.diadsimulation.auth.core.common.Messages.USER_NOT_FOUND
import com.diadsimulation.auth.core.domain.model.User
import com.diadsimulation.auth.core.port.input.FindUserInputPort
import com.diadsimulation.auth.core.port.output.UserRepositoryOutputPort
import java.util.NoSuchElementException
import java.util.UUID

class FindUserUseCaseImplementation(
    private val userRepositoryOutputPort: UserRepositoryOutputPort,
    private val userAccessAuthorization: UserAccessAuthorization,
) : FindUserInputPort {
    override fun execute(
        userId: UUID?,
        email: String?,
        login: String?,
        cpf: String?,
        rg: String?,
    ): User {
        val user =
            when {
                userId != null -> userRepositoryOutputPort.findById(userId)
                email != null -> userRepositoryOutputPort.findByEmail(email)
                login != null -> userRepositoryOutputPort.findByLogin(login)
                cpf != null -> userRepositoryOutputPort.findByCpf(cpf)
                rg != null -> userRepositoryOutputPort.findByRg(rg)
                else -> null
            } ?: throw NoSuchElementException(USER_NOT_FOUND)

        userAccessAuthorization.authorize(user.userId)

        return user
    }
}
