package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VocabularyBook.Adapter.AllwordAdpater
import com.example.VocabularyBook.Adapter.EditWordbookAdapter
import kotlinx.android.synthetic.main.activity_edit_wordbook.*
import kotlinx.android.synthetic.main.activity_editword.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditWordbookActivity : AppCompatActivity() {
    var UserUid=-1
    var bookid=-1
    lateinit var  Editwordbookact:EditWordbookActivity
    companion object {
        lateinit var editwordbookact: EditWordbookActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wordbook)
        editwordbookact=this
        Editwordbookact=this

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
        rv_editwordbook.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_editwordbook.adapter =  EditWordbookAdapter( this, WordbookActivity.wordlistarray)

    }

    fun UpdateData(position:Int){
        showProgress(true)
        var pos=position
        WordbookActivity.wordbookact.loaddata(bookid)
        GlobalScope.launch(Dispatchers.Main) {
            while(WordbookActivity.isfinish<2){
                delay(50)
            }
            rv_editwordbook.layoutManager= LinearLayoutManager(Editwordbookact, LinearLayoutManager.VERTICAL,false)

            rv_editwordbook.adapter =  EditWordbookAdapter( Editwordbookact, WordbookActivity.wordlistarray)
            if (WordbookActivity.wordlistarray.size-1 < pos)
                pos=WordbookActivity.wordlistarray.size-1
            (rv_editwordbook.layoutManager as LinearLayoutManager).scrollToPosition(pos)
            showProgress(false)


        }


    }
    fun showProgress(show: Boolean) {
        pb_editwordbook!!.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun Toastmsg(text:String){
        Toast.makeText( this,text , Toast.LENGTH_SHORT).show()

    }





}