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
import kotlinx.android.synthetic.main.activity_selectwordbook.*

class SelectWordbookActivity:AppCompatActivity() {

    lateinit var rvwordbooklist: RecyclerView
    lateinit var wordbooklist: ArrayList<Wordbook>
    lateinit var mywordbooklistact: SelectWordbookActivity
    private var service: ServiceApi? = null
    var UserUid:Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectwordbook)
        mywordbooklistact=this

        UserUid=intent.getIntExtra("useruid",-1)
        if (UserUid==-1){
            finish()
        }
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)
        loadData()
        rvwordbooklist = rv_selectwordbook as RecyclerView
        rvwordbooklist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //rv_profile.setHasFixedSize((true)) // 성능개선
        rvwordbooklist.adapter = WordbookAdapter(this, wordbooklist,UserUid,true,this)
    }
    private fun loadData() {
        wordbooklist = arrayListOf<Wordbook>()

        service!!.getmyWordbook(UserData(UserUid))!!.enqueue(object : Callback<wordbooklistResponse?> {
            override fun onResponse(
                call: Call<wordbooklistResponse?>,
                response: Response<wordbooklistResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {
                        result.booklist as Array<wordbookdata>
                        for (i in result.booklist) {
                            wordbooklist.add(Wordbook(i.bookid, i.Rid, i.Uid, i.bookname))
                        }
                        rvwordbooklist.adapter = WordbookAdapter(mywordbooklistact, wordbooklist,UserUid,true,mywordbooklistact)
                    }
                }
            }
            override fun onFailure(
                call: Call<wordbooklistResponse?>,
                t: Throwable
            ) {
                Toast.makeText(mywordbooklistact, "로그인 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("TAG", t.message!!)
            }
        })

    }
}