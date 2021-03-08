package com.example.VocabularyBook

import android.util.Log
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal object MultiUploadTest {
    private const val STORY_UPLOAD_MULTI_URL = "https://kapi.kakao.com/v2/vision/text/ocr"
    private const val ACCESS_TOKEN = "KakaoAK c828e103bd127722af7ccb3ec8859421"
    fun uploadMulti(f:File): String? {
        if (f == null) return null
        val CRLF = "\r\n"
        val TWO_HYPHENS = "--"
        val BOUNDARY = "---------------------------012345678901234567890123456"
        var conn: HttpsURLConnection? = null
        var dos: DataOutputStream? = null
        var fis: FileInputStream? = null
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        var buffer: ByteArray
        val MAX_BUFFER_SIZE = 1 * 1024 * 1024

        // Request
        try {
            val url = URL(STORY_UPLOAD_MULTI_URL)
            conn = url.openConnection() as HttpsURLConnection
            conn.doInput = true
            conn!!.doOutput = true
            conn.useCaches = false
            conn.requestMethod = "POST"
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" +
                    BOUNDARY)
            conn.setRequestProperty("Authorization", ACCESS_TOKEN)
            conn.setRequestProperty("Cache-Control", "no-cache")
            dos = DataOutputStream(conn.outputStream)

            dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF)
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";" +
                    " filename=\"" + f.name + "\"" + CRLF)
            dos.writeBytes(CRLF)
            fis = FileInputStream(f)
            bytesAvailable = fis.available()
            bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE)
            buffer = ByteArray(bufferSize)
            bytesRead = fis.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize)
                bytesAvailable = fis.available()
                bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE)
                bytesRead = fis.read(buffer, 0, bufferSize)
            }
            dos.writeBytes(CRLF)


            // finish delimiter
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF)
            fis!!.close()
            dos.flush()
            dos.close()
        } catch (ex: MalformedURLException) {
            ex.printStackTrace()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            if (fis != null) try {
                fis.close()
            } catch (ignore: IOException) {
            }
            if (dos != null) try {
                dos.close()
            } catch (ignore: IOException) {
            }
        }

        // Response
        var inputStream: InputStream? = null
        var reader: BufferedReader? = null
        try {
            inputStream = BufferedInputStream(conn!!.inputStream)
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            val builder = StringBuilder()
            while (reader.readLine().also { line = it } != null) {
                builder.append(line).append("\n")
            }
            reader.close()
            return builder.toString()
        } catch (e: IOException) {
            Log.d("TAG","에러에러-------")
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (ignore: IOException) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (ignore: IOException) {
                }
            }
            conn!!.disconnect()
        }
        return null
    }
}