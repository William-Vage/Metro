package com.jiemaibj.metro.utils
import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.crypto.symmetric.SymmetricAlgorithm
import cn.hutool.crypto.symmetric.SymmetricCrypto
import java.io.FileInputStream
import java.io.FileOutputStream


public class DESUtil {
    private var des: SymmetricCrypto
    private lateinit var desKey: String
    init {
        // 密钥长24字节，
        // 不够则会随机补足24字节
        desKey = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER, 24)

        des = SymmetricCrypto(SymmetricAlgorithm.DES, desKey.toByteArray())
    }

    /**
     * 获得密钥
     */
    fun getKey() :String{
        return desKey
    }

    /**
     * 加密
     */
    fun encrypt(plaintext:String):String {
        val encryptHex: String = des.encryptHex(plaintext)
        return encryptHex
    }

    /**
     * 解密
     */
    fun decrypt(ciphertext: String): String {
        val decryptStr: String = des.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8)
        return decryptStr
    }

    fun encryptFile(sourcePath: String, targetPath: String) {
        try {

            des.encrypt(FileInputStream(sourcePath), FileOutputStream(targetPath), true)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decryptFile(sourcePath: String, targetPath: String) {

        try {
            des.decrypt(FileInputStream(sourcePath), FileOutputStream(targetPath),true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}

