package com.jiemaibj.metro.data

import com.jiemaibj.metro.data.model.LoggedInUser
import com.jiemaibj.metro.utilities.toBase64String
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.random.Random


class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(
                userId = java.util.UUID.randomUUID().toString(),
                username = username,
                secret = Random.nextBytes(1024).toBase64String()
            )
            delay(1000)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}