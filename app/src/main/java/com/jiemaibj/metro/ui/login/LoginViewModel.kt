package com.jiemaibj.metro.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.LoginRepository
import com.jiemaibj.metro.data.Result
import com.jiemaibj.metro.utilities.AsymmetricCipherUtil
import com.jiemaibj.metro.utilities.GenericKeyPair
import com.jiemaibj.metro.utilities.RSAUtil
import com.jiemaibj.metro.utilities.SM2Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.system.measureNanoTime

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sm2Util: SM2Util,
    private val rsaUtil: RSAUtil,
) :
    ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    data class TimingResult(
        val strength: Int,
        val keygenNanos: Long,
        val encryptionNanos: Long,
        val decryptionNanos: Long
    )

    private fun <PubKey, PrvKey> testCipher(
        util: AsymmetricCipherUtil<PubKey, PrvKey>,
        strengthSequence: Iterable<Int> = (2048..2048),
    ): Iterable<TimingResult> {
        val bytes = Random.nextBytes(1024)
        return strengthSequence.map {
            val keyPair: GenericKeyPair<PubKey, PrvKey>
            val encrypted: ByteArray
            val keygenNanos = measureNanoTime {
                keyPair = util.generateKeyPair(it)
            }
            val encryptionNanos = measureNanoTime {
                encrypted = util.encrypt(bytes, keyPair.public)
            }
            val decryptionNanos = measureNanoTime {
                util.decrypt(encrypted, keyPair.private)
            }
            TimingResult(it, keygenNanos, encryptionNanos, decryptionNanos)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                val sm2Message = "SM2 密钥生成/加密/解密用时\n" +
                        testCipher(sm2Util).first().let {
                            "%.2fms / %.2fms / %.2fms".format(
                                it.keygenNanos / 1e6,
                                it.encryptionNanos / 1e6,
                                it.decryptionNanos / 1e6
                            )
                        }
                val rsaMessage = "RSA 密钥生成/加密/解密用时\n" +
                        testCipher(rsaUtil, listOf(1024, 2048, 3072, 4096)).joinToString("\n") {
                            "%d: %.2fms / %.2fms / %.2fms".format(
                                it.strength,
                                it.keygenNanos / 1e6,
                                it.encryptionNanos / 1e6,
                                it.decryptionNanos / 1e6
                            )
                        }
                _loginResult.value =
                    LoginResult(success = result.data, message = "$sm2Message\n$rsaMessage")
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }
}