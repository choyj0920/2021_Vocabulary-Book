package com.example.VocabularyBook

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.Adapter.Wordbook
import com.example.VocabularyBook.Adapter.WordbookAdapter
import kotlinx.android.synthetic.main.activity_mywordbooklist.*

class MywordbooklistActivity:AppCompatActivity() {
    lateinit var rvwordbooklist :RecyclerView
    lateinit var wordbooklist : ArrayList<Wordbook>
    lateinit var mywordbooklistact : MywordbooklistActivity
    private var service: ServiceApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywordbooklist)

        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)

        mywordbooklistact=this

        loadData()
        rvwordbooklist = rv_wordbooklist as RecyclerView
        rvwordbooklist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //rv_profile.setHasFixedSize((true)) // 성능개선

        rvwordbooklist.adapter = WordbookAdapter(this, wordbooklist)

    }

    private fun loadData(){ // 채팅방 메시지 값 로드 함수
        wordbooklist=arrayListOf<Wordbook>()

        service!!.findFriend(UserData(LoginActivity.Useruid))!!.enqueue(object : Callback<wordbooklistResponse?> {
            override fun onResponse(
                call: Call<wordbooklistResponse?>,
                response: Response<wordbooklistResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        result.booklist as Array<wordbookdata>
                        for (i in result.booklist ){

                            wordbooklist.add(Wordbook(i.bookid, i.Rid, i.Uid, i.bookname))

                        }
                        rvwordbooklist.adapter= WordbookAdapter(mywordbooklistact, wordbooklist)

                    }
                }
            }
            override fun onFailure(
                call: Call<wordbooklistResponse?>,
                t: Throwable
            ) {
                Toast.makeText(mywordbooklistact, "로그인 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("로그인 에러 발생", t.message!!)

            }
        })


    }


}