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
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import retrofit2.Callback
import retrofit2.Response


class WordfindImgActivity : AppCompatActivity() {
    lateinit var api:ServiceKakaoApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfind_img)

        val retrofit = RetrofitClientkakao.client

        api = retrofit.create(ServiceKakaoApi::class.java)

        iv_wordfindimg.setOnClickListener {
            openGallery()
        }

        btn_wordfindimgselect.setOnClickListener {
            Log.d("TAG",imageToBitmap(iv_wordfindimg).toString())
            var text=imgtoText(imageToBitmap(iv_wordfindimg))
            Log.d("TAG", "결과 : $text ")

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
        }
    }

    private fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    private fun imgtoText(imgbytearr: ByteArray): String{
        var callPostkakaoocr = api.ImagetoText(RetrofitClientkakao.kakaoRestapiKey, RetrofitClientkakao.kakaoContentType, imgbytearr) // 각각
        var resulttext=""
        Log.d("TAG", "imgtotext 실행")


        callPostkakaoocr.enqueue(object : Callback<ImgtoTextResponse?> {
            override fun onResponse(call: Call<ImgtoTextResponse?>, response: Response<ImgtoTextResponse?>) {
                val result = response.body()
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
            override fun onFailure(call: Call<ImgtoTextResponse?>, t: Throwable) {
                Log.d("TAG", "-----------------------실패 : 이미지 -> text $t")
            }
        })


        return resulttext
    }


}



