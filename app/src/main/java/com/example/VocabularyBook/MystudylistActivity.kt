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
import com.example.VocabularyBook.Adapter.Study
import com.example.VocabularyBook.Adapter.StudyAdapter
import com.example.VocabularyBook.Adapter.Wordbook
import com.example.VocabularyBook.Adapter.WordbookAdapter
import kotlinx.android.synthetic.main.activity_mystudylist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MystudylistActivity:AppCompatActivity() {
    lateinit var rvstudylist: RecyclerView
    lateinit var studylist: ArrayList<Study>
    lateinit var mystudylistact: MystudylistActivity
    private var service: ServiceApi? = null
    var UserUid:Int =-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mystudylist)

        UserUid=intent.getIntExtra("useruid",-1)
        if (UserUid==-1){
            finish()
        }
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)

        mystudylistact = this

        loadData()
        rvstudylist = rv_studylist as RecyclerView
        rvstudylist.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvstudylist.adapter = StudyAdapter(this, studylist,UserUid)

        btn_studylist_addstudy.setOnClickListener{
            val intent= Intent(this, AddStudyActivity::class.java)
            intent.putExtra("useruid",UserUid)
            startActivity(intent)

        }


    }
    private fun loadData() {
        studylist = arrayListOf()

        service!!.getmyStudy(UserData(UserUid))!!.enqueue(object : Callback<studylistResponse?> {
            override fun onResponse(
                    call: Call<studylistResponse?>,
                    response: Response<studylistResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")

                    if (result.code == 200) {
                        result.studylist as Array<studydata>
                        for (i in result.studylist) {

                            studylist.add(Study(i.Rid, i.room_name, i.host, i.notice,i.msg != null))

                        }
                        rvstudylist.adapter = StudyAdapter(mystudylistact, studylist,UserUid)

                    }
                }
            }
            override fun onFailure(
                    call: Call<studylistResponse?>,
                    t: Throwable
            ) {

                Log.e("TAG", t.message!!)

            }
        })
    }

    fun rejectStudy(Rid:Int){
        var isFinish=false
        var isSuccess=false
        service!!.rejectstudy(Rejectstudyinputdata(Rid,UserUid))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                    call: Call<NormalResponse?>,
                    response: Response<NormalResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {
                        isSuccess=true
                    }
                }
                isFinish=true
            }
            override fun onFailure(
                    call: Call<NormalResponse?>,
                    t: Throwable
            ) {

                Log.e("TAG", t.message!!)
                isFinish=true
            }
        })
        GlobalScope.launch(Dispatchers.Main) {
            while(!isFinish){
                delay(100)
            }
            if(isSuccess){
                loadData()
            }
        }

    }


    fun aceptStudy(Rid:Int){
        var isFinish=false
        var isSuccess=false
        service!!.updatestudymsg(Updatestudymsginputdata(Rid,UserUid,""))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                    call: Call<NormalResponse?>,
                    response: Response<NormalResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {
                        isSuccess=true
                    }
                }
                isFinish=true
            }
            override fun onFailure(
                    call: Call<NormalResponse?>,
                    t: Throwable
            ) {

                Log.e("TAG", t.message!!)
                isFinish=true
            }
        })
        GlobalScope.launch(Dispatchers.Main) {
            while(!isFinish){
                delay(100)
            }
            if(isSuccess){
                loadData()
            }
        }

    }





}