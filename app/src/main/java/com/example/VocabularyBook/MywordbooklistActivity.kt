package com.example.VocabularyBook

import android.content.Intent
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
    lateinit var rvwordbooklist: RecyclerView
    lateinit var wordbooklist: ArrayList<Wordbook>
    lateinit var mywordbooklistact: MywordbooklistActivity
    private var service: ServiceApi? = null
    var UserUid:Int =-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywordbooklist)

        UserUid=intent.getIntExtra("useruid",-1)
        if (UserUid==-1){
            finish()
        }
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)

        mywordbooklistact = this

        loadData()
        rvwordbooklist = rv_wordbooklist as RecyclerView
        rvwordbooklist.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //rv_profile.setHasFixedSize((true)) // 성능개선

        rvwordbooklist.adapter = WordbookAdapter(this, wordbooklist,UserUid)

        btn_wordbooklist_addwordbook.setOnClickListener{
            val intent= Intent(this, AddWordbookActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)

        }

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
                        rvwordbooklist.adapter = WordbookAdapter(mywordbooklistact, wordbooklist,UserUid)

                    }
                }
            }
            override fun onFailure(
                    call: Call<wordbooklistResponse?>,
                    t: Throwable
            ) {

                Log.e("TAG", t.message!!)

            }
        })

    }
}