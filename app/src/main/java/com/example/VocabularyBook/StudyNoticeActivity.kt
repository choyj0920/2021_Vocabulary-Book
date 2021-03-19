package com.example.VocabularyBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VocabularyBook.Adapter.Study
import com.example.VocabularyBook.Adapter.StudyAdapter
import com.example.VocabularyBook.Adapter.StudyMsgAdapter
import kotlinx.android.synthetic.main.activity_study.*
import kotlinx.android.synthetic.main.activity_study_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyNoticeActivity : AppCompatActivity() {
    var UserUid=-1
    var host=-1
    var Rid=-1
    var notice=""
    var room_name=""
    var bookid:Int?=null
    lateinit var msglist:ArrayList<studymsg>
    lateinit var service:ServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_notice)

        UserUid=intent.getIntExtra("useruid",-1)
        Rid=intent.getIntExtra("Rid",-1)
        host = intent.getIntExtra("host",-1)
        notice= intent.getStringExtra("notice").toString()
        room_name= intent.getStringExtra("room_name").toString()

        if(host==-1 || UserUid==-1 || Rid==-1){
            Log.d("TAG","getExtra error UserUid=$UserUid,  host=$host,  Rid=$Rid")
            finish()
        }
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)


        tv_studynotice_title.setText(room_name)
        if(host != UserUid){
            tv_Studynotice_notice.isEnabled=false
        }
        tv_Studynotice_notice.setText(notice)
        rv_studynotice.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        loaddata()




    }
    fun loaddata(){
        msglist= arrayListOf()

        service!!.getstudymsg(getstudymsginputdata(Rid))!!.enqueue(object : Callback<getStudyMsgResponse?> {
            override fun onResponse(
                call: Call<getStudyMsgResponse?>,
                response: Response<getStudyMsgResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {
                        var tempmsglist = result.msglist as Array<studymsg>
                        for (i in tempmsglist) {
                            msglist.add(i)
                        }
                        rv_studynotice.adapter = StudyMsgAdapter(this@StudyNoticeActivity, msglist, UserUid)
                    }
                }
            }
            override fun onFailure(
                call: Call<getStudyMsgResponse?>,
                t: Throwable
            ) {

                Log.e("TAG", t.message!!)

            }
        })


    }

    fun updateMsg(userUid: Int, msg: String) {

        service!!.updatestudymsg(Updatestudymsginputdata(Rid,userUid,msg))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                call: Call<NormalResponse?>,
                response: Response<NormalResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {

                    }
                }
            }
            override fun onFailure(
                call: Call<NormalResponse?>,
                t: Throwable
            ) {

                Log.e("TAG", t.message!!)

            }
        })

    }
}