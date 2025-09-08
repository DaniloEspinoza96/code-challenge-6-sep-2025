package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.logout()
}