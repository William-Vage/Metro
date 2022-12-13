package com.jiemaibj.metro.utils
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.crypto.symmetric.SymmetricCrypto
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream


public class SM4Util {
    private var sm4: SymmetricCrypto
    private lateinit var sm4Key:String
    init {
        // 密钥长16字节，
        // 不够则会随机补足16字节
        sm4Key = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER, 16)

        sm4 = SymmetricCrypto("SM4/ECB/PKCS5Padding", sm4Key.toByteArray())
    }

    /**
     * 获得密钥
     */
    fun getKey() :String{
        return sm4Key
    }

    /**
     * 加密字符串
     */
    fun encrypt(plaintext:String):String {
        val encryptHex: String = sm4.encryptHex(plaintext)
        return encryptHex
    }

    /**
     * 解密字符串
     */
    fun decrypt(ciphertext: String): String {
        val decryptStr: String = sm4.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8)
        return decryptStr
    }

    fun encryptFile(sourcePath: String, targetPath: String) {
        try {

            sm4.encrypt(FileInputStream(sourcePath), FileOutputStream(targetPath), true)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decryptFile(sourcePath: String, targetPath: String) {

        try {
            sm4.decrypt(FileInputStream(sourcePath),FileOutputStream(targetPath),true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}

