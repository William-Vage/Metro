package com.jiemaibj.metro

import com.jiemaibj.metro.utilities.SM2Util
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class SM2UnitTest {
    private val sm2Util = SM2Util()

    @Test
    fun generateKey() {
        val keyPair = sm2Util.generateSm2KeyPair()
        val prvKeyStr = keyPair.private.toString()
        val pubKeyStr = keyPair.public.toString()
        assertNotNull(prvKeyStr)
        assertNotNull(pubKeyStr)
    }

    @Test
    fun keySerialization() {
        val keyPair = sm2Util.generateSm2KeyPair()
        val prvKeyStr = sm2Util.sm2PrivateKeyToString(keyPair.private)
        val pubKeyStr = sm2Util.sm2PublicKeyToString(keyPair.public)
        assertEquals(keyPair.private, sm2Util.stringToSM2PrivateKey(prvKeyStr))
        assertEquals(keyPair.public, sm2Util.stringToSM2PublicKey(pubKeyStr))
    }

    @Test
    fun encryptDecrypt() {
        val keyPair = sm2Util.generateSm2KeyPair()
        for (length in 1024..4096 step 1024) {
            val bytes = Random.nextBytes(length)
            val encrypted = sm2Util.encrypt(bytes, keyPair.public as BCECPublicKey)
            val decrypted = sm2Util.decrypt(encrypted, keyPair.private as BCECPrivateKey)
            assertArrayEquals(bytes, decrypted)
        }
    }
}
