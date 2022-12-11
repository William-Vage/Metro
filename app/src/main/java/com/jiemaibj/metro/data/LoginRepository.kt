package com.jiemaibj.metro.data

import com.jiemaibj.metro.data.db.User
import com.jiemaibj.metro.data.db.UserDao
import com.jiemaibj.metro.data.model.LoggedInUser
import com.jiemaibj.metro.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource,
    private val userDao: UserDao,
    @ApplicationScope private val externalScope: CoroutineScope
) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        externalScope.launch {
            userDao.insert(User(loggedInUser.userId, loggedInUser.username, ""))
        }
    }
}