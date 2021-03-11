package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_editword.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditwordActivity : AppCompatActivity() {
    var UserUid=-1
    var isFinish=true
    var WordId=-1
    var pos =-1
    lateinit var api:ServiceApi
    companion object{
        var bookid = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editword)
        bookid=intent.getIntExtra("bookid",-1)
        UserUid= intent.getIntExtra("useruid",-1)
        WordId=intent.getIntExtra("wordid",-1)
        pos=intent.getIntExtra("position",-1)
        if(UserUid==-1 || bookid==-1) finish()

        tv_editword_wordeng.setText(intent.getStringExtra("wordeng"))
        tv_editword_wordmean.setText(intent.getStringExtra("wordmean"))

        val retrofit = RetrofitClient.client
        api = retrofit.create(ServiceApi::class.java)
        isFinish=true

        btn_editword_finish.setOnClickListener {
            if(!isFinish){
                Log.d("TAG","이전 작업 중 ")
                return@setOnClickListener
            }else{
                Log.d("TAG","editword 작업 시작")
                isFinish=false
            }
            
            if(tv_editword_wordeng.text.toString().length==0 || tv_editword_wordmean.text.toString().length==0) {
                Log.d("TAG", "단어 길이 제한----------")
                Toast.makeText(this, "단어 혹은 뜻이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                isFinish = true
                return@setOnClickListener
            }
            if(tv_editword_wordeng.text.toString().length>45 || tv_editword_wordmean.text.toString().length > 45){
                Log.d("TAG","단어 길이 제한----------")
                Toast.makeText( this,"단어와 뜻은 45자를 넘을 수 없습니다." ,Toast.LENGTH_SHORT).show()
                isFinish=true
                return@setOnClickListener
            }
            else {
                showProgress(true)
                var isSuccess=false

                api.EditWord(editwordinputdata(tv_editword_wordeng.text.toString(), tv_editword_wordmean.text.toString(), bookid,WordId))!!.enqueue(object : Callback<NormalResponse?> {
                        override fun onResponse(
                            call: Call<NormalResponse?>,
                            response: Response<NormalResponse?>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 200) {
                                    Toastmsg("단어 수정 완료")
                                    isSuccess=true
                                    Log.d("TAG", "성공 : 단어 :${tv_editword_wordeng.text.toString()},뜻 : ${tv_editword_wordmean.text.toString()} 단어장:${bookid}에 수정 성공")
                                } else {
                                    Toastmsg("단어 수정 실패...")
                                    Log.d("TAG", "오류 : 단어 :${tv_editword_wordeng.text.toString()},뜻 : ${tv_editword_wordmean.text.toString()} 단어장:${bookid}에 수정 실패")
                                }
                            }
                            isFinish = true
                        }
                        override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                            Toastmsg("단어 수정 실패...")
                            Log.d("TAG", "오류 : 단어 :${tv_editword_wordeng.text.toString()},뜻 : ${tv_editword_wordmean.text.toString()} 단어장:${bookid}에 수정 실패")
                            isFinish = true

                        }
                    })
                GlobalScope.launch(Dispatchers.Main) {
                    while(!isFinish){
                        delay(100)
                    }
                    showProgress(false)
                    if(isSuccess){
                        EditWordbookActivity.editwordbookact.UpdateData(pos)
                        finish()
                    }
                }
            }
        }
    }
    private fun showProgress(show: Boolean) {
        pb_editword!!.visibility = if (show) View.VISIBLE else View.GONE
    }
    private fun Toastmsg(text:String){
        Toast.makeText( this,text ,Toast.LENGTH_SHORT).show()

    }

}