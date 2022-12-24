package com.jiemaibj.metro

import com.jiemaibj.metro.utilities.SM2Util
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class SM2UnitTest {
    private val sm2Util = SM2Util()

    @Test
    fun generateKey() {
        val keyPair = sm2Util.generateKeyPair()
        val prvKeyStr = keyPair.private.toString()
        val pubKeyStr = keyPair.public.toString()
        assertNotNull(prvKeyStr)
        assertNotNull(pubKeyStr)
    }

    @Test
    fun keySerialization() {
        val keyPair = sm2Util.generateKeyPair()
        val prvKeyStr = sm2Util.privateKeyToString(keyPair.private)
        val pubKeyStr = sm2Util.publicKeyToString(keyPair.public)
        assertEquals(keyPair.private, sm2Util.stringToPrivateKey(prvKeyStr))
        assertEquals(keyPair.public, sm2Util.stringToPublicKey(pubKeyStr))
    }

    @Test
    fun encryptDecrypt() {
        val keyPair = sm2Util.generateKeyPair()
        for (length in 1024..4096 step 1024) {
            val bytes = Random.nextBytes(length)
            val encrypted = sm2Util.encrypt(bytes, keyPair.public)
            val decrypted = sm2Util.decrypt(encrypted, keyPair.private)
            assertArrayEquals(bytes, decrypted)
        }
    }
}
