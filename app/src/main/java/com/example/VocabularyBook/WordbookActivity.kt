package com.example.VocabularyBook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wordbook.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordbookActivity:AppCompatActivity() {
    var bookid:Int?=null
    var Rid:Int?=null
    var Uid:Int?=null
    var wordbookname:String?=null
    var wordbookact:WordbookActivity?=null
    private var service: ServiceApi? = null
    companion object{
        var wordlistarray:ArrayList<worddata>?=null

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordbook)

        wordbookact=this

        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        bookid= intent.getStringExtra("bookid")?.toInt()
        Rid= intent.getStringExtra("Rid")?.toInt()
        Uid= intent.getStringExtra("Uid")?.toInt()
        wordbookname= intent.getStringExtra("wordbookname")?.toString()

        loaddata(bookid)

        tv_wordbookname.text=wordbookname


        tv_wordlist.setOnClickListener {
            val intent= Intent(this, WordlistActivity::class.java)
            startActivity(intent)
        }
        tv_wordmemorize.setOnClickListener {
            val intent= Intent(this, tv_wordmemorize::class.java)
            startActivity(intent)
        }
        tv_wordtest.setOnClickListener {
            val intent= Intent(this, WordtestActivity::class.java)
            startActivity(intent)
        }
        if (Uid==LoginActivity.Useruid){ // 내 단어장일 때만 수정 가능
            tv_wordbookedit.setOnClickListener {
//            val intent= Intent(this, WordlistActivity::class.java)
//            startActivity(intent)
            }
        }


    }

    private fun loaddata(bookid : Int?) { // 채팅방 메시지 값 로드 함수
        var wordlist = arrayListOf<worddata>()
        service!!.getWordlist(wordbookiddata(bookid))!!.enqueue(object : Callback<wordlistResponse?> {
            override fun onResponse(
                    call: Call<wordlistResponse?>,
                    response: Response<wordlistResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        result.wordlist as Array<worddata>
                        for (i in result.wordlist ) {
                            wordlist.add(i)

                        }
                        wordlistarray=wordlist
                    }
                }
            }
            override fun onFailure(
                    call: Call<wordlistResponse?>,
                    t: Throwable
            ) {
                Toast.makeText(wordbookact, "단어를 불러오던중 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("단어리스트를 가져오던 중 에러 발생", t.message!!)

            }
        })
    }


}