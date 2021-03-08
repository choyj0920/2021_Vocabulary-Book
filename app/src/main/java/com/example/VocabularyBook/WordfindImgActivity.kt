package com.example.VocabularyBook

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wordfind_img.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class WordfindImgActivity : AppCompatActivity(){
    lateinit var api:ServiceKakaoApi
    lateinit var filePartImage:  MultipartBody.Part
    lateinit var boxarray:ArrayList<box>
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
           // Log.d("TAG", "결과 : $text ")

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

        }


    }

    private fun imageToBitmap(image: ImageView): Bitmap {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream)

        return bitmap
    }

    private fun imgtoText(bitmap: Bitmap): String{
        var resulttext=""
        Log.d("TAG", "img to text 실행")

        var filepath= getExternalFilesDir(null).toString() +"/wordbook"
        var dir=File(filepath)
        if(!dir.exists())
            dir.mkdirs()

        var fileName="temp.png"
        var file =File(dir,fileName)
        filepath=file.absolutePath

        file.writeBitmap(bitmap,Bitmap.CompressFormat.PNG,80)
        //var file = File(filepath+"/"+fileName)
        Log.d("DEBUG","$filepath")
        file=File(filepath)
        Log.d("TAG", "파일 저장 완료")

        //file = File(filepath+"/"+fileName)

        CoroutineScope(IO).launch {
            boxarray = getTextFromImage(filepath,api)

            withContext(Main) {
                boxarray.sort()
                var text=""
                for ( i in boxarray){
                    text +="${i.text}\n"
                    Log.d("TAG","텍스트 출력--------"+i.text)
                }
                tv_wordfind_result.text=text
            }
        }


        return resulttext
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
            out.close()
        }
    }

    suspend fun getTextFromImage(filepath:String, api:ServiceKakaoApi):ArrayList<box> {
        var boxarray:ArrayList<box> = arrayListOf()

        var file=File(filepath)

        val requestBody: RequestBody
        val body: MultipartBody.Part
        val mapRequestBody =
            LinkedHashMap<String, RequestBody>()
        val arrBody: ArrayList<MultipartBody.Part> = ArrayList()

        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        mapRequestBody["file\"; filename=\"" + file.name] = requestBody
        mapRequestBody["test"] = RequestBody.create(MediaType.parse("text/plain"), "gogogogogogogog")

        body = MultipartBody.Part.createFormData("image", file.name, requestBody)
        arrBody.add(body)

        boxarray= arrayListOf()
        var isfinish=false

        api.ImagetoText(mapRequestBody,arrBody)
            .enqueue(object : Callback<responseimgtotxt?> {
                override fun onResponse(call: Call<responseimgtotxt?>, response: Response<responseimgtotxt?>) {
                    val result = response.body()
                    Log.d("TAG", "code : ${response.code().toString()} ,message : ${response.message()}, ${response.errorBody()} ")
                    if (result != null) {
                        var resultarr= result.result
                        for (i in resultarr){
                            var temp=""
                            for (j in i.recognition_words!!){
                                temp +="$j "
                            }
                            boxarray.add(box(i.boxes.get(0).get(1),i.boxes.get(0).get(0),temp))
                            Log.d("TAG","box 생성 $temp")

                        }
                        //tv_wordfind_result.text=resulttext
                        Log.d("TAG", "-----------------------성공! ")
                        isfinish=true
                    }
                }
                override fun onFailure(call: Call<responseimgtotxt?>, t: Throwable) {

                    Log.d("TAG", "-----------------------실패 : 이미지 -> text $t")
                    isfinish=true
                }
            })

        return boxarray


    }

}