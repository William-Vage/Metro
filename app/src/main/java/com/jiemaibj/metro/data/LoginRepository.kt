package com.jiemaibj.metro.data

import android.util.Log
import com.jiemaibj.metro.data.db.User
import com.jiemaibj.metro.data.db.UserDao
import com.jiemaibj.metro.data.model.LoggedInUser
import com.jiemaibj.metro.di.ApplicationScope
import com.jiemaibj.metro.utilities.SM2Util
import com.jiemaibj.metro.utilities.decodeBase64
import com.jiemaibj.metro.utilities.toBase64String
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource,
    private val userDao: UserDao,
    private val sm2Util: SM2Util,
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
        val keypair = sm2Util.generateKeyPair()
        val encrypted =
            sm2Util.encrypt(loggedInUser.secret.decodeBase64(), keypair.public).toBase64String()
        Log.i("login", "encrypted: $encrypted")
        externalScope.launch {
            userDao.insert(
                User(
                    id = loggedInUser.userId,
                    username = loggedInUser.username,
                    realName = "",
                    key = sm2Util.privateKeyToString(keypair.private),
                    secret = encrypted
                )
            )
        }
    }
}