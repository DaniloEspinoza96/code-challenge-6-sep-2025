package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.utils.validateEmail
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (!validateEmail(user.email)) return Result.failure(Exception("Email inválido"))
        if (user.password.length < 6) return Result.failure(Exception("Contraseña inválida"))
        if (user.name.isBlank()) return Result.failure(Exception("El nombre no puede estar vacío"))
        if (user.lastname.isBlank()) return Result.failure(Exception("El apellido no puede estar vacío"))

        return userRepository.register(user)
    }
}