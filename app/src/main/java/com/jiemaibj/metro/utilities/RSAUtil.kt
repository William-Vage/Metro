package com.jiemaibj.metro.utilities

import org.bouncycastle.crypto.AsymmetricBlockCipher
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.engines.RSAEngine
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator
import org.bouncycastle.crypto.params.AsymmetricKeyParameter
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.math.min

class RSAUtil :
    AsymmetricCipherUtil<AsymmetricKeyParameter, AsymmetricKeyParameter> {

    override fun generateKeyPair() = generateKeyPair(2048)

    /**
     * Generates RSA key pair.
     */
    override fun generateKeyPair(strength: Int): GenericKeyPair<AsymmetricKeyParameter, AsymmetricKeyParameter> {
        // Source: http://stackoverflow.com/questions/3087049/bouncy-castle-rsa-keypair-generation-using-lightweight-api
        val generator = RSAKeyPairGenerator()
        generator.init(
            RSAKeyGenerationParameters(
                BigInteger("10001", 16),
                SecureRandom(),
                strength,
                80
            )
        )
        val keyPair = generator.generateKeyPair()
        return GenericKeyPair(keyPair.public, keyPair.private)
    }

    override fun encrypt(input: ByteArray, pubKey: AsymmetricKeyParameter): ByteArray {
        val engine = RSAEngine()
        // true for encryption
        engine.init(true, pubKey)
        val inputBlockSize = engine.inputBlockSize
        val bytes = ByteArrayOutputStream()
        for (i in input.indices step inputBlockSize) {
            bytes.write(engine.processBlock(input, i, min(input.size - i, inputBlockSize)))
        }
        return bytes.toByteArray()
    }

    override fun decrypt(input: ByteArray, prvKey: AsymmetricKeyParameter): ByteArray {
        val engine: AsymmetricBlockCipher = RSAEngine()
        // false for decryption
        engine.init(false, prvKey)
        val inputBlockSize = engine.inputBlockSize
        val bytes = ByteArrayOutputStream()
        for (i in input.indices step inputBlockSize) {
            bytes.write(engine.processBlock(input, i, min(input.size - i, inputBlockSize)))
        }
        return bytes.toByteArray()
    }

    override fun stringToPublicKey(key: String): AsymmetricKeyParameter {
        TODO("Not yet implemented")
    }

    override fun stringToPrivateKey(key: String): AsymmetricKeyParameter {
        TODO("Not yet implemented")
    }

    override fun publicKeyToString(key: AsymmetricKeyParameter): String {
        TODO("Not yet implemented")
    }

    override fun privateKeyToString(key: AsymmetricKeyParameter): String {
        TODO("Not yet implemented")
    }
}