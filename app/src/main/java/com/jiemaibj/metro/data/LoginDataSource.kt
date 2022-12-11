package com.jiemaibj.metro.data

import com.jiemaibj.metro.data.model.LoggedInUser
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Singleton


class LoginDataSource {

   suspend fun login(username: String, password: String): Result<LoggedInUser> {
       return try {
           // TODO: handle loggedInUser authentication
           val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username)
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