package com.example.VocabularyBook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        tv_mywordbook.setOnClickListener {
            val intent= Intent(this, MywordbooklistActivity::class.java)
            startActivity(intent)
        }
        tv_wordfind.setOnClickListener {
//            val intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
        }
        tv_study.setOnClickListener {
//            val intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
        }


    }
}