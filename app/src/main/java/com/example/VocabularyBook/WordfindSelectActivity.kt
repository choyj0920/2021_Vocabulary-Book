package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
    import kotlinx.android.synthetic.main.activity_wordfindselect.*

class WordfindSelectActivity : AppCompatActivity() {
    var UserUid=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfindselect)
        UserUid=intent.getIntExtra("useruid",-1)
        if (UserUid==-1){
            finish()
        }

        btn_wordfindsellectimg.setOnClickListener {
            val intent= Intent(this, WordfindImgActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)
        }
        btn_wordfindsellecttxt.setOnClickListener {
            val intent= Intent(this, WordfindTextActivity::class.java)
            intent.putExtra("text","text")
            intent.putExtra("useruid",UserUid)

            startActivity(intent)
        }
        
    }
}