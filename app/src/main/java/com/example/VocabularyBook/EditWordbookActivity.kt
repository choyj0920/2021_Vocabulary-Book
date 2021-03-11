package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_wordbook.*


class EditWordbookActivity : AppCompatActivity() {
    var UserUid=-1
    var bookid=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wordbook)

        UserUid=intent.getIntExtra("useruid",-1)
        if (UserUid==-1){
            finish()
        }
        bookid=intent.getIntExtra("bookid",-1)
        if (bookid==-1){
            finish()
        }

        btn_editwordbook_add.setOnClickListener {
            val intent= Intent(this, AddwordActivity::class.java)
            intent.putExtra("useruid",UserUid)
            intent.putExtra("bookid", bookid)
            
            startActivity(intent)
        }
    }










}