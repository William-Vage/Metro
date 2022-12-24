package com.jiemaibj.metro

import com.jiemaibj.metro.utilities.RSAUtil
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class RSAUnitTest {
    private val rsaUtil = RSAUtil()

    @Test
    fun generateKey() {
        val keyPair = rsaUtil.generateKeyPair()
        val prvKeyStr = keyPair.private.toString()
        val pubKeyStr = keyPair.public.toString()
        Assert.assertNotNull(prvKeyStr)
        Assert.assertNotNull(pubKeyStr)
    }

    @Test
    fun encryptDecrypt() {
        val keyPair = rsaUtil.generateKeyPair()
        for (length in 1024..4096 step 1024) {
            val bytes = Random.nextBytes(length)
            val encrypted = rsaUtil.encrypt(bytes, keyPair.public)
            val decrypted = rsaUtil.decrypt(encrypted, keyPair.private)
            Assert.assertArrayEquals(bytes, decrypted)
        }
    }
}