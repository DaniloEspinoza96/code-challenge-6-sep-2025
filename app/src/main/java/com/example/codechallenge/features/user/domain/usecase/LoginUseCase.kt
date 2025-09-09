package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // here we can add more business logic, to reinforce things like valid emails, password length
    suspend operator fun invoke(email: String, password: String): Result<Unit> = userRepository.login(email, password)
}