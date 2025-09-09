package com.example.codechallenge.features.user.domain.usecase

import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // here we can add more business logic, to reinforce things like valid emails,
    // password length and characters, since this is a small project, we already
    // have validations in presentation layer, but it would be wise to have that logic in the use case
    // to avoid registering invalid credentials
    suspend operator fun invoke(user: User): Result<Unit> = userRepository.register(user)
}