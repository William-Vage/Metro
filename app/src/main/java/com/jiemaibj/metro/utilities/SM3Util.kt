package com.jiemaibj.metro.utilities

import org.bouncycastle.crypto.digests.SM3Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import java.util.*


/**
 * SM3算法工具类
 */
object SM3Util {
    /**
     * 计算SM3摘要值
     *
     * @param srcData 原文
     * @return 摘要值，对于SM3算法来说是32字节
     */
    fun hash(srcData: ByteArray): ByteArray {
        val digest = SM3Digest()
        digest.update(srcData, 0, srcData.size)
        val hash = ByteArray(digest.digestSize)
        digest.doFinal(hash, 0)
        return hash
    }

    /**
     * 验证摘要
     *
     * @param srcData 原文
     * @param sm3Hash 摘要值
     * @return 返回true标识验证成功，false标识验证失败
     */
    fun verify(srcData: ByteArray, sm3Hash: ByteArray?): Boolean {
        val newHash = hash(srcData)
        return if (Arrays.equals(newHash, sm3Hash)) {
            true
        } else {
            false
        }
    }

    /**
     * 计算SM3 Mac值
     *
     * @param key     key值，可以是任意长度的字节数组
     * @param srcData 原文
     * @return Mac值，对于HMac-SM3来说是32字节
     */
    fun hmac(key: ByteArray?, srcData: ByteArray): ByteArray {
        val keyParameter = KeyParameter(key)
        val digest = SM3Digest()
        val mac = HMac(digest)
        mac.init(keyParameter)
        mac.update(srcData, 0, srcData.size)
        val result = ByteArray(mac.macSize)
        mac.doFinal(result, 0)
        return result
    }
}
