package com.example.VocabularyBook

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import kotlin.properties.Delegates


class LoginActivity : AppCompatActivity() {
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mEmailLoginButton: Button? = null
    private var mJoinButton: TextView? = null
    private var mProgressView: ProgressBar? = null
    private var service: ServiceApi? = null
    var loginactivity :LoginActivity? =null
    companion object{
        lateinit var maincontext : LoginActivity
        var Useruid :Int=-1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maincontext=this
        loginactivity=this
        setContentView(R.layout.activity_login)
        mEmailView =findViewById<View>(R.id.email_edittext) as AutoCompleteTextView
        mPasswordView = findViewById<View>(R.id.password_edittext) as EditText
        mEmailLoginButton =
            findViewById<View>(R.id.signIn_button) as Button
        mJoinButton = findViewById<View>(R.id.register_button) as TextView
        mProgressView = findViewById<View>(R.id.login_progress) as ProgressBar
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        maincontext=this@LoginActivity

        mEmailLoginButton!!.setOnClickListener {
            attemptLogin()
        }
        mJoinButton!!.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
        //카카오 api 키 해시 가져오기
        getAppKeyHash()
    }

    private fun attemptLogin() {
        mEmailView!!.error = null
        mPasswordView!!.error = null
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        var cancel = false
        var focusView: View? = null

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mEmailView!!.error = "비밀번호를 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (!isPasswordValid(password)) {
            mPasswordView!!.error = "6자 이상의 비밀번호를 입력해주세요."
            focusView = mPasswordView
            cancel = true
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            mEmailView!!.error = "이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = "@를 포함한 유효한 이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {
            Log.d("debug","$email, $password 로그인 시도")
            showProgress(true)
            startLogin(LoginData(email, password))
        }
    }

    private fun startLogin(data: LoginData) {
        service!!.userLogin(data)!!.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                val result = response.body()
                showProgress(false)
                if (result != null) {
                    Toast.makeText(LoginActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        LoginActivity.Useruid=result.Uid
                        Log.d("debug","${result.Uid}")
                        val intent= Intent(loginactivity,MainActivity::class.java)
                        intent.putExtra("useruid",result.Uid)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(LoginActivity.maincontext, "이메일 혹은 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show()

                    }
                }

            }

            override fun onFailure(
                call: Call<LoginResponse?>,
                t: Throwable
            ) {
                Toast.makeText(LoginActivity.maincontext, "로그인 에러 발생", Toast.LENGTH_SHORT).show()
                Log.e("로그인 에러 발생", t.message!!)
                showProgress(false)
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun showProgress(show: Boolean) {
        mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
    }
    // 카카오 api 키 해시 받아오기
    fun getAppKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for(i in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(i.toByteArray())

                val something = String(Base64.encode(md.digest(), 0)!!)
                Log.e("Debug key", something)
            }
        } catch(e: Exception) {
            Log.e("Not found", e.toString())
        }
    }
}