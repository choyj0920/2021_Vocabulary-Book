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

        val retrofit = RetrofitClient.client
        api = retrofit.create(ServiceApi::class.java)
        isFinish=true

        btn_addword_selectwordbook.setOnClickListener {
            bookid=-1
            val intent= Intent(this, SelectWordbookActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)


        }

        btn_addword_finish.setOnClickListener {
            if(!isFinish){
                Log.d("TAG","이전 작업 중 ")
                return@setOnClickListener
            }else{
                Log.d("TAG","addword작업")
                isFinish=false
            }

            if(tv_addword_wordeng.text.toString().length>45 || tv_addword_wordmean.text.toString().length > 45){
                Log.d("TAG","단어 길이 제한----------")
                Toast.makeText( this,"단어와 뜻은 45자를 넘을 수 없습니다." ,Toast.LENGTH_SHORT).show()
                isFinish=true
                return@setOnClickListener
            }
            else if(bookid==-1){
                Log.d("TAG","단어장을 골라주세요----------")
                Toast.makeText( this,"단어장을 선택해주세요" ,Toast.LENGTH_SHORT).show()
                isFinish=true
                return@setOnClickListener
            }
            else {
                showProgress(true)

                api.AddWord(addwordinputdata(tv_addword_wordeng.text.toString(), tv_addword_wordmean.text.toString(), bookid))!!.enqueue(object : Callback<NormalResponse?> {
                        override fun onResponse(
                            call: Call<NormalResponse?>,
                            response: Response<NormalResponse?>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 200) {
                                    Toastmsg("단어 추가 완료")
                                    Log.d("TAG", "성공 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 성공")
                                } else {
                                    Toastmsg("단어 추가 실패...")
                                    Log.d("TAG", "오류 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 실패")
                                }
                            }
                            isFinish = true
                        }
                        override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                            Toastmsg("단어 추가 실패...")
                            Log.d("TAG", "오류 : 단어 :${tv_addword_wordeng.text.toString()},뜻 : ${tv_addword_wordmean.text.toString()} 단어장:${bookid}에 추가 실패")
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
    private fun Toastmsg(text:String){
        Toast.makeText( this,text ,Toast.LENGTH_SHORT).show()

    }

}