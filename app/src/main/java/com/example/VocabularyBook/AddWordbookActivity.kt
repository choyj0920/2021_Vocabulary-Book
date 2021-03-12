package com.example.VocabularyBook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_wordbook.*
import kotlinx.android.synthetic.main.activity_addword.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWordbookActivity : AppCompatActivity() {
    var UserUid =-1
    var RId :Int?= -1
    var bookid:Int?=null
    lateinit var api:ServiceApi
    lateinit var addwordbookact:AddWordbookActivity
    var isFinish=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wordbook)
        addwordbookact=this

        UserUid = intent.getIntExtra("useruid", -1)
        RId = intent.getIntExtra("rid", -1)
        if (RId == -1) RId = null
        if (UserUid == -1) finish()

        val retrofit = RetrofitClient.client
        api = retrofit.create(ServiceApi::class.java)

        btn_addwordbook_add.setOnClickListener {
            if(tv_addwordbook.text.toString() == ""){
                Toastmsg("단어장의 이름을 입력해 주세요")
                return@setOnClickListener
            }
            isFinish=false
            var isSuccess = false
            api.AddWordbook(AddWordbookInput(UserUid,RId,tv_addwordbook.text.toString()))!!.enqueue(object : Callback<AddwordbookResponse?> {
                override fun onResponse(
                    call: Call<AddwordbookResponse?>,
                    response: Response<AddwordbookResponse?>
                ) {
                    val result = response.body()
                    if (result != null) {
                        if (result.code == 200) {
                            Toastmsg("단어장 추가 완료")
                            bookid=result.bookid
                            isSuccess = true
                            Log.d(
                                "TAG","단어장 추가 완료 단어장 "
                            )
                        } else {
                            Toastmsg("단어장 추가 실패 ...")
                            Log.d("TAG","오류 . 단어장 추가 실패, ${result.message}")
                        }
                    }
                    isFinish = true
                }
                override fun onFailure(call: Call<AddwordbookResponse?>, t: Throwable) {
                    Toastmsg("단어장 추가 실패...")
                    Log.d(
                        "TAG","단어장 추가 실패,  $t")
                    isFinish = true
                }
            })
            GlobalScope.launch(Dispatchers.Main) {
                while (!isFinish) {
                    delay(100)
                }
                
                if (isSuccess && bookid !=null) {
                    val intent= Intent(addwordbookact, WordbookActivity::class.java)
                    intent.putExtra("bookid", bookid!!)
                    intent.putExtra("Rid",RId)
                    intent.putExtra("Uid",UserUid)
                    intent.putExtra("wordbookname",tv_addwordbook.text.toString())
                    intent.putExtra("useruid",UserUid)
                    addwordbookact.startActivity(intent)
                    finish()

                }
            }

        }
    }
    private fun Toastmsg(text:String){
        Toast.makeText( this,text , Toast.LENGTH_SHORT).show()
    }
}