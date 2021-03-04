package com.example.VocabularyBook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_wordfind_img.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import retrofit2.Callback
import retrofit2.Response

class WordfindImgActivity : AppCompatActivity() {
    lateinit var api:ServiceKakaoApi
    lateinit var filePartImage:  MultipartBody.Part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfind_img)

        val retrofit = RetrofitClientkakao.client

        api = retrofit.create(ServiceKakaoApi::class.java)

        iv_wordfindimg.setOnClickListener {
            openGallery()
        }

        btn_wordfindimgselect.setOnClickListener {

            var text=imgtoText(imageToBitmap(iv_wordfindimg))
            Log.d("TAG", "결과 : $text ")

           // tv_wordfind_result.text=text

        }


    }
    private  val OPEN_GALLERY=1
    private fun openGallery(){
        val intent:Intent = Intent()
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.action=Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent,"Load Picture"),OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK){
            if(requestCode==OPEN_GALLERY){
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap =MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                    iv_wordfindimg.setImageBitmap(bitmap) // 비트맵으로 표현
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            if (data != null) {

                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                iv_wordfindimg.setImageBitmap(bitmap)

                val imageType = contentResolver.getType(data.data!!)

                val extension = imageType!!.substring(imageType.indexOf("/") + 1)

                data.data!!.let {
                    application.contentResolver.openInputStream(it)?.use { inputStream ->
                        var temparr=inputStream.readBytes()
                        filePartImage = MultipartBody.Part.createFormData(
                            "image",
                            "image.$extension",
                            RequestBody.create(MediaType.parse("imgage/*"),temparr)
                                    //inputStream.readBytes().toRequestBody("*/*".toMediaType())
                        )
                    }
                }

            } else {

            }
        }


    }

    private fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream)

        return stream.toByteArray()
    }

    private fun imgtoText(imgbytearr: ByteArray): String{
        var resulttext=""
        Log.d("TAG", "img to text 실행")


        api.ImagetoText(KakaoRestApiKey= RetrofitClientkakao.kakaoRestapiKey, KakaoRestApiCt= RetrofitClientkakao.kakaoContentType,image =  filePartImage)
            .enqueue(object : Callback<responseimgtotxt?> {
            override fun onResponse(call: Call<responseimgtotxt?>, response: Response<responseimgtotxt?>) {
                val result = response.body()
                Log.d("TAG", "code : ${response.code().toString()} ,message : ${response.message()}, ${response.errorBody()} ")
                if (result != null) {
                    var resultarr= result.result
                    if (resultarr != null) {
                        for (i in resultarr){
                            for (j in i.recognition_words!!){
                                resulttext +="${j}\n"
                            }
                        }
                    }
                    tv_wordfind_result.text=resulttext
                    Log.d("TAG", "-----------------------성공!, 결과 : $resulttext ")
                }
            }
            override fun onFailure(call: Call<responseimgtotxt?>, t: Throwable) {

                Log.d("TAG", "-----------------------실패 : 이미지 -> text $t")
            }
        })


        return resulttext
    }
}