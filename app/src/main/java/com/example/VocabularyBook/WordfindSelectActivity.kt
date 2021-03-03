package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wordfindsellect.*

class WordfindSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfindsellect)

        btn_wordfindsellectimg.setOnClickListener {
            val intent= Intent(this, WordfindImgActivity::class.java)
            startActivity(intent)
        }
        btn_wordfindsellecttxt.setOnClickListener {
//            val intent= Intent(this, MywordbooklistActivity::class.java)
//            startActivity(intent)
        }




    }
}