package com.example.VocabularyBook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_wordfind_img.*
import java.io.File
import java.lang.Exception

class WordfindImgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfind_img)

        iv_wordfindimg.setOnClickListener {
            openGallery()
        }




    }
    private  val OPEN_GALLERY=1
    private fun openGallery(){
        val intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("imgage/*")
        startActivityForResult(intent,OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK){
            if(requestCode==OPEN_GALLERY){
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap =MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                    iv_wordfindimg.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.PrintStack()
                }
            }
        }
    }




}



