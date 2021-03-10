package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addword.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddwordActivity : AppCompatActivity() {
    var UserUid=-1
    var isFinish=true
    lateinit var api:ServiceApi
    companion object{
        var bookid=-1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addword)

        UserUid= intent.getIntExtra("useruid",-1)
        if(UserUid==-1) finish()
        tv_addword_wordeng.setText(intent.getStringExtra("wordeng"))
        tv_addword_wordmean.setText(intent.getStringExtra("wordmean"))

        val retrofit = RetrofitClientPapago.client
        api = retrofit.create(ServiceApi::class.java)


        btn_addword_selectwordbook.setOnClickListener {
            bookid=-1
            val intent= Intent(this, SelectWordbookActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)


        }

        btn_addword_finish.setOnClickListener {
            if(!isFinish){
                return@setOnClickListener
            }else{
                isFinish=false
            }

            if(tv_addword_wordeng.text.toString().length>45 || tv_addword_wordmean.text.toString().length > 45){
                Toast.makeText( WordmemoryActivity.wordmemoryact,"단어와 뜻은 45자를 넘을 수 없습니다." ,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(bookid==-1){
                Toast.makeText( WordmemoryActivity.wordmemoryact,"단어장을 선택해주세요" ,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                showProgress(true)

                api.AddWord(
                    addwordinputdata(
                        tv_addword_wordeng.text.toString(),
                        tv_addword_wordmean.text.toString(),
                        bookid
                    )
                )
                    ?.enqueue(object : Callback<NormalResponse?> {
                        override fun onResponse(
                            call: Call<NormalResponse?>,
                            response: Response<NormalResponse?>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 200) {
                                    Log.d(
                                        "TAG",
                                        "성공 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 성공"
                                    )
                                } else {
                                    Log.d(
                                        "TAG",
                                        "오류 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 실패"
                                    )
                                }
                            }
                            isFinish = true
                        }

                        override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                            Log.d(
                                "TAG",
                                "오류 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 실패"
                            )
                            isFinish = true

                        }
                    })
                GlobalScope.launch(Dispatchers.Main) {
                    while(!isFinish){
                        delay(100)
                    }
                    showProgress(false)

                }

            }

        }

    }
    private fun showProgress(show: Boolean) {
        pb_addword!!.visibility = if (show) View.VISIBLE else View.GONE
    }

}