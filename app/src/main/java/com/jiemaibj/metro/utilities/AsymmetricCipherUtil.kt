package com.jiemaibj.metro.utilities

data class GenericKeyPair<PubKey, PrvKey>(
    val public: PubKey,
    val private: PrvKey,
)

interface AsymmetricCipherUtil<PubKey, PrvKey> {
    fun generateKeyPair(): GenericKeyPair<PubKey, PrvKey>

    fun generateKeyPair(strength: Int): GenericKeyPair<PubKey, PrvKey> = generateKeyPair()

    fun stringToPublicKey(key: String): PubKey

    fun stringToPrivateKey(key: String): PrvKey

    fun publicKeyToString(key: PubKey): String

    fun privateKeyToString(key: PrvKey): String

    /**
     * Encrypts an array of bytes.
     *
     * @param input bytes to encrypt
     * @param pubKey public key used for encryption
     * @return ciphertext
     */
    fun encrypt(input: ByteArray, pubKey: PubKey): ByteArray

    /**
     * Encrypts an array of bytes.
     *
     * @param pubKey public key encoded as a hex string
     * @see encrypt(ByteArray, BCECPublicKey)
     */
    fun encrypt(input: ByteArray, pubKey: String): ByteArray {
        return encrypt(input, stringToPublicKey(pubKey))
    }

    /**
     * Encrypts a string.
     *
     * @param input the string, which is encoded into UTF-8 bytes
     * @param pubKey public key encoded as a hex string
     * @see encrypt(ByteArray, BCECPublicKey)
     */
    fun encrypt(input: String, pubKey: String): ByteArray {
        return encrypt(input.toByteArray(), stringToPublicKey(pubKey))
    }

    /**
     * Encrypts an array of bytes, and encodes the ciphertext in base64.
     *
     * @param pubKey public key encoded as a hex string
     * @see SM2Util.encrypt(ByteArray, BCECPublicKey)
     */
    fun encryptAsBase64String(input: ByteArray, pubKey: String): String {
        return encrypt(input, stringToPublicKey(pubKey)).toBase64String()
    }

    /**
     * Decrypts ciphertext.
     *
     * @param input bytes to decrypt
     * @param prvKey private key used for encryption
     * @return the encrypted bytes
     */
    fun decrypt(input: ByteArray, prvKey: PrvKey): ByteArray

    /**
     * Decrypts ciphertext.
     *
     * @param prvKey private key encoded as a hex string
     * @see SM2Util.decrypt(ByteArray, BCECPrivateKey)
     */
    fun decrypt(input: ByteArray, prvKey: String): ByteArray {
        return decrypt(input, stringToPrivateKey(prvKey))
    }

    /**
     * Decrypts ciphertext in base64 format.
     *
     * @param prvKey private key encoded as a hex string
     * @return the encrypted bytes
     */
    fun decryptBase64String(input: String, prvKey: String): ByteArray {
        return decrypt(input.decodeBase64(), stringToPrivateKey(prvKey))
    }

}