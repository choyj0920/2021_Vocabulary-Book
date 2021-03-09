package com.example.VocabularyBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_wordfind_text.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordfindTextActivity : AppCompatActivity() {
    var koToen:Boolean=true
    lateinit var api:ServicePapagoApi
    var Translated_text=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordfind_text)
        var beforetext=intent.getStringExtra("text").toString()
        tv_wordfind_before.setText(beforetext)

        val retrofit = RetrofitClientPapago.client
        api = retrofit.create(ServicePapagoApi::class.java)

        btn_swap_lang.setOnClickListener { // 번역언어 변경
            btn_swap_lang.setText(if(koToen)"영->한" else "한->영")
            koToen = !koToen
        }
        btn_translate.setOnClickListener { // 번역 버튼 리스너
            showProgress(true)
            GlobalScope.launch(Dispatchers.Main) {
                val job1 = async(Dispatchers.IO) {
                    translate(tv_wordfind_before.text.toString())
                }
                job1.await()
                tv_wordfind_after.setText(Translated_text)
                showProgress(false)
            }
        }

    }

    suspend fun translate(targettext:String){
        if(targettext==""){ // 비어있을때는 변역이 되지 않으므로
            Translated_text=""
            return
        }
        var isfinish=false
        var callPostTransferPapago = api.transferPapago(
            if(koToen) "ko" else "en", if(koToen) "en" else "ko", targettext ) // 각각 파파고 api 요청변수

        callPostTransferPapago.enqueue(object : Callback<TranslateResponse?> {
            override fun onResponse(call: Call<TranslateResponse?>, response: Response<TranslateResponse?>) {
                val result = response.body()
                if (result != null) {
                    Translated_text= result.message?.result?.translatedText.toString() // result 안에안에 있는 변역된 문장 저장
                    Log.d("TAG", "성공 :,$targettext -> ${Translated_text}")
                }
                isfinish=true

            }
            override fun onFailure(call: Call<TranslateResponse?>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
                Translated_text=""
                isfinish=true

            }
        })

        while(!isfinish){
            delay(100)
        }


    }

    private fun showProgress(show: Boolean) {
        pb_wordfindtext!!.visibility = if (show) View.VISIBLE else View.GONE
    }
}