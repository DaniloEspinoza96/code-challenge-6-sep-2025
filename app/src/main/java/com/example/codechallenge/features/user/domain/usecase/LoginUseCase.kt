package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> = userRepository.login(email, password)
}