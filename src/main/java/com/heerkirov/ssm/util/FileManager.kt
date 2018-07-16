package com.heerkirov.ssm.util

import org.springframework.web.multipart.MultipartFile
import java.io.*
import com.sun.deploy.trace.Trace.flush
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.IOException



class FileManager(private val basePath: String) {
    fun save(file: MultipartFile, fileName: String) {
        //获取输出流
        val os = FileOutputStream("$basePath/$fileName")
        //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
        val stream = file.inputStream
        var temp: Int
        //一个一个字节的读取并写入
        while(true) {
            temp = stream.read()
            if(temp == -1) break
            os.write(temp)
        }
        os.flush()
        os.close()
        stream.close()
    }
    fun saveSafe(file: MultipartFile, fileName: String): Boolean {
        return try {
            save(file, fileName)
            true
        }catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun saveAndResize(file: MultipartFile, fileName: String, size: Int) {
        val os = FileOutputStream("$basePath/$fileName")
        //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
        val stream = file.inputStream
        resizeImage(stream, os, size, "jpg")
    }
    fun saveAndResizeSafe(file: MultipartFile, fileName: String, size: Int): Boolean {
        return try {
            saveAndResize(file, fileName, size)
            true
        }catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun get(fileName: String): ByteArray? {
        val file = File("$basePath/$fileName")
        if(file.exists() && file.isFile && file.canRead()) {
            val stream = FileInputStream(file)
            val data = ByteArray(file.length().toInt())
            val length = stream.read(data)
            stream.close()
            return data
        }else return null
    }

    fun resizeImage(iStream: InputStream, os: OutputStream, size: Int, format: String) {
        val prevImage = ImageIO.read(iStream)
        val width = prevImage.width.toDouble()
        val height = prevImage.height.toDouble()
        val percent = size / width
        val newWidth = (width * percent).toInt()
        val newHeight = (height * percent).toInt()
        val image = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR)
        val graphics = image.createGraphics()
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null)
        ImageIO.write(image, format, os)
        os.flush()
        iStream.close()
        os.close()
    }
}