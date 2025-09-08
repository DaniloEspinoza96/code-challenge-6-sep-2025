package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> = userRepository.register(user)
}