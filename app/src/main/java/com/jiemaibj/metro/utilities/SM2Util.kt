package com.jiemaibj.metro.utilities

import org.bouncycastle.asn1.gm.GMNamedCurves
import org.bouncycastle.asn1.gm.GMObjectIdentifiers
import org.bouncycastle.asn1.x9.X9ECParameters
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECParameterSpec
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import org.bouncycastle.util.encoders.Hex
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.*
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import javax.crypto.Cipher
import javax.crypto.CipherInputStream

/**
 * SM2 Utilities.
 *
 * @author dashou
 * @author Rhacoal
 * @date 2022-12-12
 */
class SM2Util {
    private val provider: BouncyCastleProvider = BouncyCastleProvider()

    // SM2 曲线参数
    private val sm2Params: X9ECParameters = GMNamedCurves.getByName("sm2p256v1")
    private val sm2GenSpec = ECGenParameterSpec("sm2p256v1")
    private val ecParameterSpec: ECParameterSpec = ECParameterSpec(
        sm2Params.curve, sm2Params.g, sm2Params.n, sm2Params.h
    )

    // 密钥生成器
    private val keyFactory: KeyFactory = KeyFactory.getInstance("EC", provider)

    // 密钥对生成器
    private val keyPairGenerator by lazy {
        KeyPairGenerator.getInstance("EC", provider).apply {
            initialize(sm2GenSpec, SecureRandom())
        }
    }

    /**
     * Generates SM2 key pair.
     */
    fun generateSm2KeyPair(): KeyPair {
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Decodes SM2 public key in hex format.
     */
    fun stringToSM2PublicKey(key: String): BCECPublicKey {
        val ecPoint = sm2Params.curve.decodePoint(Hex.decode(key))
        return keyFactory.generatePublic(ECPublicKeySpec(ecPoint, ecParameterSpec)) as BCECPublicKey
    }

    /**
     * Decodes SM2 private key in hex format.
     */
    fun stringToSM2PrivateKey(key: String): BCECPrivateKey {
        return keyFactory.generatePrivate(
            ECPrivateKeySpec(BigInteger(key, 16), ecParameterSpec)
        ) as BCECPrivateKey
    }

    /**
     * Encodes SM2 public key into hex string.
     */
    fun sm2PublicKeyToString(key: PublicKey): String {
        return String(Hex.encode((key as BCECPublicKey).q.getEncoded(true)))
    }

    /**
     * Encodes SM2 private key into hex string.
     */
    fun sm2PrivateKeyToString(key: PrivateKey): String {
        return (key as BCECPrivateKey).d.toString(16)
    }

    /**
     * Encrypts an array of bytes.
     *
     * @param input bytes to encrypt
     * @param pubKey public key used for encryption
     * @return ciphertext
     */
    fun encrypt(input: ByteArray, pubKey: BCECPublicKey): ByteArray {
        val cipher = Cipher.getInstance("SM2", provider)
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        return cipher.doFinal(input)
    }

    /**
     * Encrypts a byte stream.
     *
     * @param input bytes to encrypt
     * @param pubKey public key used for encryption
     * @return a stream
     */
    fun encrypt(input: InputStream, pubKey: BCECPublicKey): CipherInputStream {
        val cipher = Cipher.getInstance("SM2", provider)
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        return CipherInputStream(input, cipher)
    }

    /**
     * Encrypts an array of bytes.
     *
     * @param pubKey public key encoded as a hex string
     * @see SM2Util.encrypt(ByteArray, BCECPublicKey)
     */
    fun encrypt(input: ByteArray, pubKey: String): ByteArray {
        return encrypt(input, stringToSM2PublicKey(pubKey))
    }

    /**
     * Encrypts a string.
     *
     * @param input the string, which is encoded into UTF-8 bytes
     * @param pubKey public key encoded as a hex string
     * @see SM2Util.encrypt(ByteArray, BCECPublicKey)
     */
    fun encrypt(input: String, pubKey: String): ByteArray {
        return encrypt(input.toByteArray(), stringToSM2PublicKey(pubKey))
    }

    /**
     * Encrypts an array of bytes, and encodes the ciphertext in base64.
     *
     * @param pubKey public key encoded as a hex string
     * @see SM2Util.encrypt(ByteArray, BCECPublicKey)
     */
    fun encryptAsBase64String(input: ByteArray, pubKey: String): String {
        return encrypt(input, stringToSM2PublicKey(pubKey)).toBase64String()
    }

    /**
     * Decrypts ciphertext.
     *
     * @param input bytes to decrypt
     * @param prvKey private key used for encryption
     * @return the encrypted bytes
     */
    fun decrypt(input: ByteArray, prvKey: BCECPrivateKey): ByteArray {
        val cipher = Cipher.getInstance("SM2", provider)
        cipher.init(Cipher.DECRYPT_MODE, prvKey)
        return cipher.doFinal(input)
    }

    /**
     * Decrypts a byte stream.
     *
     * @param input stream
     * @param prvKey private key used for encryption
     * @return the encrypted bytes
     */
    fun decrypt(input: InputStream, prvKey: BCECPrivateKey): CipherInputStream {
        val cipher = Cipher.getInstance("SM2", provider)
        cipher.init(Cipher.DECRYPT_MODE, prvKey)
        return CipherInputStream(input, cipher)
    }

    /**
     * Decrypts ciphertext.
     *
     * @param prvKey private key encoded as a hex string
     * @see SM2Util.decrypt(ByteArray, BCECPrivateKey)
     */
    fun decrypt(input: ByteArray, prvKey: String): ByteArray {
        return decrypt(input, stringToSM2PrivateKey(prvKey))
    }

    /**
     * Decrypts ciphertext in base64 format.
     *
     * @param prvKey private key encoded as a hex string
     * @return the encrypted bytes
     */
    fun decryptBase64String(input: String, prvKey: String): ByteArray {
        return decrypt(input.decodeBase64(), stringToSM2PrivateKey(prvKey))
    }

    /**
     * Signs an array of bytes.
     *
     * @param bytes bytes to sign
     * @param prvKey private key
     * @return signature in base64 format
     */
    fun sign(bytes: ByteArray, prvKey: String): String {
        // 创建签名对象
        val signature =
            Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider)
        // 将私钥HEX字符串转换为X值
        val privateKey = stringToSM2PrivateKey(prvKey)
        // 初始化为签名状态
        signature.initSign(privateKey)
        // 传入签名字节
        signature.update(bytes)
        // 签名
        return signature.sign().toBase64String()
    }

    /**
     * Signs a string.
     *
     * @param plainText string to sign
     * @param prvKey private key
     * @return signature in base64 format
     */
    fun sign(plainText: String, prvKey: String): String {
        return sign(plainText.toByteArray(), prvKey)
    }

    /**
     * Verifies the signature for an array of bytes.
     *
     * @param bytes bytes
     * @param signatureValue signature to verify
     * @param pubKey public key
     * @return whether the signature is valid
     */
    fun verify(bytes: ByteArray, signatureValue: String, pubKey: String): Boolean {
        // 创建签名对象
        val signature =
            Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider)
        val key = stringToSM2PublicKey(pubKey)
        // 初始化为验签状态
        signature.initVerify(key)
        signature.update(bytes)
        return signature.verify(signatureValue.decodeBase64())
    }

    /**
     * Verifies the signature for an string.
     *
     * @param plainText string
     * @param signatureValue signature to verify
     * @param pubKey public key
     * @return whether the signature is valid
     */
    fun verify(plainText: String, signatureValue: String, pubKey: String): Boolean {
        return verify(plainText.toByteArray(), signatureValue, pubKey)
    }

    /**
     * Verifies a certificate chain
     *
     * @param certStr      证书串
     * @param plaintext    签名原文
     * @param signValueStr 签名产生签名值 此处的签名值实际上就是 R 和 S 的 sequence
     * @return
     */
    fun certVerify(certStr: String, plaintext: ByteArray, signValueStr: String): Boolean {
        val signValue = signValueStr.decodeBase64()
        // 解析证书
        val factory = CertificateFactory()
        val certificate = factory.engineGenerateCertificate(
            ByteArrayInputStream(
                certStr.decodeBase64()
            )
        ) as X509Certificate
        // 验证签名
        val signature = Signature.getInstance(certificate.sigAlgName, provider)
        signature.initVerify(certificate)
        signature.update(plaintext)
        return signature.verify(signValue)
    }
}