package com.task.data.repository.login

import com.task.data.Resource
import com.task.data.dto.login.LoginRequest
import com.task.data.dto.login.LoginResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>
}
