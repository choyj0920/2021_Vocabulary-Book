package com.example.VocabularyBook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.VocabularyBook.Adapter.Study
import com.example.VocabularyBook.Adapter.StudyAdapter
import kotlinx.android.synthetic.main.activity_study.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyActivity : AppCompatActivity() {
    var UserUid=-1
    var host=-1
    var Rid=-1
    var notice=""
    var room_name=""
    var bookid:Int?=null
    lateinit var service:ServiceApi
    lateinit var rankviewarray :ArrayList<TextView>
    val rankstr :ArrayList<String> = arrayListOf("1st","2nd","3rd")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        UserUid=intent.getIntExtra("useruid",-1)
        Rid=intent.getIntExtra("Rid",-1)
        host = intent.getIntExtra("host",-1)
        notice= intent.getStringExtra("notice").toString()
        room_name= intent.getStringExtra("room_name").toString()
        if(host==-1 || UserUid==-1 || Rid==-1){
            Log.d("TAG","getExtra error UserUid=$UserUid,  host=$host,  Rid=$Rid")
            finish()
        }
        rankviewarray= arrayListOf(tv_name1,tv_cnt1,tv_1st,tv_name2,tv_cnt2,tv_2nd,tv_name3,tv_cnt3,tv_3rd)

        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)

        loadWordboookdata()
        updateRank()


        tv_study_notice.setOnClickListener {
            val intent= Intent(this, StudyNoticeActivity::class.java)
            intent.putExtra("Rid",Rid)
            intent.putExtra("host",host)
            intent.putExtra("notice",notice)
            intent.putExtra("room_name",room_name)
            intent.putExtra("useruid", UserUid)
            startActivity(intent)

        }
        tv_study_part.setOnClickListener {
            val intent= Intent(this, StudyMemberActivity::class.java)
            intent.putExtra("Rid",Rid)
            intent.putExtra("host",host)
            intent.putExtra("room_name",room_name)
            intent.putExtra("useruid", UserUid)
            startActivity(intent)
        }



    }
    fun addWordlistlistener(){
        tv_study_wordbook.setOnClickListener {
            val intent= Intent(this, WordbookActivity::class.java)
            intent.putExtra("bookid",bookid)
            intent.putExtra("Rid",Rid)
            intent.putExtra("Uid",host)
            intent.putExtra("wordbookname",room_name+"단어장")
            intent.putExtra("useruid",UserUid)
            this.startActivity(intent)
        }
    }

    private fun loadWordboookdata(){
        service!!.getStudybookid(studybookidinput(Rid))!!.enqueue(object : Callback<studybookidResponse?> {
            override fun onResponse(
                call: Call<studybookidResponse?>,
                response: Response<studybookidResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")

                    if (result.code == 200) {
                        bookid=result.bookid
                        addWordlistlistener()


                    }
                }
            }
            override fun onFailure(
                call: Call<studybookidResponse?>,
                t: Throwable
            ) {
                Log.e("TAG", t.message!!)
            }
        })
    }

    fun updateRank(){

        service!!.getstudyRank(getStudyRankinputdata(bookid))!!.enqueue(object : Callback<getStudyRankResponse?> {
            override fun onResponse(
                    call: Call<getStudyRankResponse?>,
                    response: Response<getStudyRankResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")

                    if (result.code == 200) {
                        if (result.rank != null) {
                            GlobalScope.launch(Dispatchers.Main) {
                                for (i in result.rank.indices) {
                                    rankviewarray[i * 3].setText(result.rank[i].username)
                                    rankviewarray[i * 3 + 1].setText(result.rank[i].count.toString())
                                    rankviewarray[i*3+2].setText(rankstr[i].toString())

                                }
                            }
                        }

                    }
                }
            }
            override fun onFailure(
                    call: Call<getStudyRankResponse?>,
                    t: Throwable
            ) {
                Log.e("TAG", t.message!!)
            }
        })
    }



}