package com.example.VocabularyBook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity() {
    var UserUid:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserUid=intent.getIntExtra("useruid",-1)

        setContentView(R.layout.activity_main)

        tv_mywordbook.setOnClickListener {
            val intent= Intent(this, MywordbooklistActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)
        }
        tv_wordfind.setOnClickListener {
            val intent= Intent(this,WordfindSelectActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)
        }
        tv_study.setOnClickListener {
            val intent= Intent(this,MystudylistActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)
        }


    }
}