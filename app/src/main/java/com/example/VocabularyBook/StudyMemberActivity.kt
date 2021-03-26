package com.example.VocabularyBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VocabularyBook.Adapter.InvitedStudyMemberAdapter
import com.example.VocabularyBook.Adapter.StudyMemberAdapter
import kotlinx.android.synthetic.main.activity_add_study.*
import kotlinx.android.synthetic.main.activity_study_member.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyMemberActivity : AppCompatActivity() {
    private var service: ServiceApi? = null

    var UserUid=-1
    var host=-1
    var Rid=-1
    var room_name=""

    var memberlist :ArrayList<studymsg> = arrayListOf()
    var invitedmemberlist :ArrayList<studymsg> = arrayListOf()
    lateinit var studymemberact:StudyMemberActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_member)
        studymemberact=this

        UserUid=intent.getIntExtra("useruid",-1)
        Rid=intent.getIntExtra("Rid",-1)
        host = intent.getIntExtra("host",-1)
        room_name= intent.getStringExtra("room_name").toString()
        if(host==-1 || UserUid==-1 || Rid==-1){
            Log.d("TAG","getExtra error UserUid=$UserUid,  host=$host,  Rid=$Rid")
            finish()
        }

        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java)

        btn_studymember_invite.setOnClickListener {
            inviteMember(tv_studymember_invite.text.toString())
        }
        loaddata()
    }

    fun loaddata(){

        var isFinish=false
        var isSuccess=false
        service!!.getstudymsg(getstudymsginputdata(Rid,true))!!.enqueue(object :
            Callback<getStudyMsgResponse?> {
            override fun onResponse(
                call: Call<getStudyMsgResponse?>,
                response: Response<getStudyMsgResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug", "${result.message}")

                    if (result.code == 200) {
                        memberlist= arrayListOf()
                        invitedmemberlist= arrayListOf()
                        isSuccess=true

                        for ( i in result.msglist!!){
                            if(i.msg == null){
                                invitedmemberlist.add(i)
                            }
                            else{
                                memberlist.add(i)
                            }
                        }
                        rv_studymember_invite.layoutManager =
                            LinearLayoutManager(studymemberact, LinearLayoutManager.VERTICAL, false)
                        rv_studymember_invite.adapter = InvitedStudyMemberAdapter(studymemberact, invitedmemberlist, UserUid)

                        rv_studymember.layoutManager =
                            LinearLayoutManager(studymemberact, LinearLayoutManager.VERTICAL, false)
                        rv_studymember.adapter = StudyMemberAdapter(studymemberact, memberlist,UserUid)
                    }
                }
                isFinish=true
            }
            override fun onFailure(
                call: Call<getStudyMsgResponse?>,
                t: Throwable
            ) {
                Log.e("TAG", t.message!!)
                isFinish=true
            }
        })
//         GlobalScope.launch(Dispatchers.Main) {
//            while(!isFinish){
//                delay(100)
//            }
//            if(isSuccess){
//                //loadData()
//            }
//        }
    }

    fun inviteMember(email : String){
        var isFinish=false
        var isSuccess=false
        var resultuid:Int=-1
        showProgress(true)
        service!!.getUid(getuidinputdata(email))!!.enqueue(object : Callback<getuidResponse?> {
            override fun onResponse(
                call: Call<getuidResponse?>,
                response: Response<getuidResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    Log.d("debug", "${result.message}")
                    if (result.code == 200) {
                        isSuccess=true
                        resultuid= result.Uid!!
                    }
                    else{
                        Toast.makeText(this@StudyMemberActivity, "${result.message}", Toast.LENGTH_SHORT).show()
                        showProgress(false)

                    }
                }
                isFinish=true
            }
            override fun onFailure(
                call: Call<getuidResponse?>,
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
                for(i in memberlist){
                    if(i.Uid==resultuid){
                        showProgress(false)
                        Toastmsg("현재 스터디 멤버 입니다.")
                        return@launch
                    }
                }
                for(i in invitedmemberlist){
                    if(i.Uid==resultuid){
                        Toastmsg("이미 초대 되어 있는 회원 입니다.")
                        showProgress(false)
                        return@launch
                    }
                }

                service!!.particiapateStudy( participatestudy(Rid, resultuid, ishost = false))!!.enqueue(object : Callback<NormalResponse?> {
                    override fun onResponse(
                        call: Call<NormalResponse?>,
                        response: Response<NormalResponse?>
                    ) {
                        val result = response.body()
                        if (result != null) {
                            if (result.code == 200) {
                                Toastmsg(" 초대 완료")
                                loaddata()
                                Log.d("TAG","스터디 추가 완료  ")
                            } else {
                                Log.d("TAG","오류 . 스터디 추가 실패, ${result.message}")
                            }
                        }
                        showProgress(false)
                    }
                    override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                        Toastmsg("초대 실패...")
                        Log.d("TAG","user스터디 초대 실패,  $t")
                        showProgress(false)


                    }
                })
            }
        }
    }


    fun OutStudy(uid:Int){
        if(host == uid && memberlist.size > 1){
            Toast.makeText(this, "스터디 개설자는 멤버가 모두 탈퇴 할 떄까지 탈퇴하실수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        var isFinish=false
        var isSuccess=false
        service!!.rejectstudy(Rejectstudyinputdata(Rid,uid))!!.enqueue(object : Callback<NormalResponse?> {
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
                //loadData()
            }
        }

    }

    private fun Toastmsg(text:String){
        Toast.makeText( this,text , Toast.LENGTH_SHORT).show()
    }
    private fun showProgress(show: Boolean) {
        pb_studyMember!!.visibility = if (show) View.VISIBLE else View.GONE
    }


}