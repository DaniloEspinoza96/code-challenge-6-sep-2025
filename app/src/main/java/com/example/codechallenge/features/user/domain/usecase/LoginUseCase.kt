package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.utils.validateEmail
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (!validateEmail(email)) return Result.failure(Exception("Email inválido"))
        if (password.length < 6) return Result.failure(Exception("Contraseña inválida"))

        return userRepository.login(email, password)
    }

}