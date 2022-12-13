package com.jiemaibj.metro.utils


import android.content.Context
import cn.hutool.core.io.resource.ResourceUtil.getResources


fun testString() {
    var s = "Anomaly detection techniques aim to detect deviations in normal behavior to detect unknown attacks. Anomaly detectors learn historical benign behavior and classify new behavior based on their deviation from the learned normal behavior[3]. Anomaly detectors can detect unknown attacks such as zero-day attacks but often sufer from a high false alarm rate because it is challenging to distinguish between unknown benign behavior, unknown malicious behavior, and system errors. Also, system evolution causes diiculties in characterizing normal behavior, which may decrease the detection performance"

    var startTime = System.currentTimeMillis()
    for (i in 0..999999) {
        var sm4 = SM4Util()
        val encrypt = sm4.encrypt(s)
    }
    var endTime = System.currentTimeMillis()
    var usedTime = (endTime - startTime)
    println("SM4加密100万次字符串耗时:${usedTime} ms")

    startTime = System.currentTimeMillis()
    for (i in 0..999999) {
        var des = DESUtil()
        val encrypt = des.encrypt(s)
    }
    endTime = System.currentTimeMillis()
    usedTime = (endTime - startTime)
    println("DES加密100万次字符串耗时:${usedTime} ms")
}

fun testImage() {
    var sp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/origin/profile.jpg";//原始文件
    var dp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/encrypt/profile.jpg";//加密后文件
    var dp2:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/decrypt/profile.jpg";//解密后文件
    var sm4 = SM4Util()
    sm4.encryptFile(sp, dp)
    sm4.decryptFile(dp, dp2)

    var des = DESUtil()
    des.encryptFile(sp, dp)
    des.decryptFile(dp, dp2)



}

fun testMusic() {
    var sp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/origin/龍猛寺寬度 - 中宵酒醒.mp3";//原始文件
    var dp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/encrypt/龍猛寺寬度 - 中宵酒醒.mp3";//加密后文件
    var dp2:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/decrypt/龍猛寺寬度 - 中宵酒醒.mp3";//解密后文件

    var sm4 = SM4Util()
    sm4.encryptFile(sp, dp)
    sm4.decryptFile(dp, dp2)

    var des = DESUtil()
    des.encryptFile(sp, dp)
    des.decryptFile(dp, dp2)

}

fun testVideo() {
    var sp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/origin/test.mp4";//原始文件
    var dp:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/encrypt/test.mp4";//加密后文件
    var dp2:String = "/Users/nodiff/Desktop/projects/202212-应用密码学大作业/Metro/app/src/main/res/sm4data/decrypt/test.mp4";//解密后文件

    var sm4 = SM4Util()
    sm4.encryptFile(sp, dp)
    sm4.decryptFile(dp, dp2)

    var des = DESUtil()
    des.encryptFile(sp, dp)
    des.decryptFile(dp, dp2)



}



fun main() {
    testString()

    testImage()

    testMusic()

    testVideo()


}