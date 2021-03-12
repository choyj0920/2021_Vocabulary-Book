package com.example.VocabularyBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_study.*
import kotlinx.android.synthetic.main.activity_wordfind_img.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudyActivity : AppCompatActivity() {
    var UserUid =-1
    lateinit var api:ServiceApi
    lateinit var addstudyact:AddStudyActivity
    var isFinish=true
    var newstudyid=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study)
        addstudyact=this

        UserUid = intent.getIntExtra("useruid", -1)

        if (UserUid == -1) finish()

        val retrofit = RetrofitClient.client
        api = retrofit.create(ServiceApi::class.java)

        btn_addwordbook_add.setOnClickListener {
            if(tv_addstudy_studyname.text.toString() == ""){
                Toastmsg("스터디의 이름을 입력해 주세요")
                return@setOnClickListener
            }
            if(tv_addstudy_studyname.text.toString().length > 50){
                Toastmsg("스터디의 이름은 50자를 넘을 수 없습니다.")
                return@setOnClickListener
            }
            var studyname=tv_addstudy_studyname.text.toString()

            isFinish=false
            var isSuccess = false
            showProgress(true)
            api.addStudy(addStudyinput(studyname,UserUid))!!.enqueue(object : Callback<addStudyResponse?> {
                override fun onResponse(
                    call: Call<addStudyResponse?>,
                    response: Response<addStudyResponse?>
                ) {
                    val result = response.body()
                    if (result != null) {
                        if (result.code == 200) {
                            Toastmsg("스터디 추가 완료")
                            newstudyid=result.Rid
                            isSuccess = true
                            Log.d(
                                "TAG","스터디 추가 완료  "
                            )
                        } else {
                            Toastmsg("스터디 추가 실패 ...")
                            Log.d("TAG","오류 . 스터디 추가 실패, ${result.message}")
                        }
                    }
                    isFinish = true
                }
                override fun onFailure(call: Call<addStudyResponse?>, t: Throwable) {
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
                showProgress(false)

                if (isSuccess && newstudyid != -1) {
                    showProgress(true)

                    var isFinish2=false
                    var isSucess2=false

                    api.particiapateStudy(participatestudy(newstudyid,UserUid,ishost = true))!!.enqueue(object : Callback<NormalResponse?> {
                        override fun onResponse(
                            call: Call<NormalResponse?>,
                            response: Response<NormalResponse?>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 200) {
                                    //Toastmsg(" 추가 완료")

                                    isSucess2 = true
                                    Log.d(
                                        "TAG","host 스터디 추가 완료  "
                                    )
                                } else {
                                    Toastmsg("host 스터디 추가 실패 ...")
                                    Log.d("TAG","오류 . host 스터디 추가 실패, ${result.message}")
                                }
                            }
                            isFinish2 = true
                        }
                        override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                            Toastmsg("단어장 추가 실패...")
                            Log.d(
                                "TAG","단어장 추가 실패,  $t")
                            isFinish2 = true
                        }
                    })


                    api.AddWordbook(AddWordbookInput(UserUid,newstudyid,studyname+"단어장"))!!.enqueue(object : Callback<AddwordbookResponse?> {
                        override fun onResponse(
                                call: Call<AddwordbookResponse?>,
                                response: Response<AddwordbookResponse?>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 200) {
                                    //Toastmsg("스터디 단어장 추가 완료")

                                    isSucess2 = true
                                    Log.d(
                                            "TAG","스터디단어장 추가 완료  "
                                    )
                                } else {
                                    Toastmsg("스터디단어장 추가 실패 ...")
                                    Log.d("TAG","오류 . 스터디 추가 실패, ${result.message}")
                                }
                            }
                            isFinish2 = true
                        }
                        override fun onFailure(call: Call<AddwordbookResponse?>, t: Throwable) {
                            Toastmsg("스터디단어장 추가 실패...")
                            Log.d(
                                    "TAG","스터디단어장 추가 실패,  $t")
                            isFinish2 = true
                        }
                    })

                    while (!isFinish2) {
                        delay(100)
                    }
                    showProgress(false)

                    if(isSucess2){
                        val intent= Intent(addstudyact, StudyActivity::class.java)
                        intent.putExtra("Rid",newstudyid)
                        intent.putExtra("host",UserUid)
                        intent.putExtra("notice","아직 공지를 생성하지 않았습니다.")
                        intent.putExtra("room_name",studyname)
                        intent.putExtra("useruid",UserUid)
                        addstudyact.startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
    private fun Toastmsg(text:String){
        Toast.makeText( this,text , Toast.LENGTH_SHORT).show()
    }
    private fun showProgress(show: Boolean) {
        pb_addstudy!!.visibility = if (show) View.VISIBLE else View.GONE
    }

}