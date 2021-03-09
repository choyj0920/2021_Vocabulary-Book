package com.example.VocabularyBook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.VocabularyBook.Fragment.MemorizedwordFragment
import com.example.VocabularyBook.Fragment.MemorizedwordFragment.Companion.memorizedwordfragment
import com.example.VocabularyBook.Fragment.WordFragment.Companion.wordFragment
import kotlinx.android.synthetic.main.activity_wordbook.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        var wordlistarray:ArrayList<worddata> = arrayListOf<worddata>()
        lateinit var wordbookact: WordbookActivity
        var memorizedwordlist: HashSet<Int> = hashSetOf()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordbook)

        wordbookact=this
        WordbookActivity.wordbookact=this
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        bookid= intent.getIntExtra("bookid",0)
        // Rid= intent.getIntExtra("Rid",1)
        Uid= intent.getIntExtra("Uid",1)
        wordbookname= intent.getStringExtra("wordbookname")?.toString()

        loaddata(bookid)

        tv_wordbookname.text=wordbookname


        tv_wordlist.setOnClickListener {
            val intent= Intent(this, WordlistActivity::class.java)
            startActivity(intent)
        }
        tv_wordmemorize.setOnClickListener {
            val intent= Intent(this, WordmemoryActivity::class.java)
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

    private fun loaddata(bookid : Int?) { // 단어장 단어리스트 , 외운 단어 가져오는 함수
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
        // set 초기화 -DD 외운 단어 가져오기
        //memorizedwordlist

        var checkset= hashSetOf<Int>()
        
        service!!.getCheckword(getcheckwordinputdata(LoginActivity.Useruid,bookid))!!.enqueue(object : Callback<checkwordResponse?> {
            override fun onResponse(
                    call: Call<checkwordResponse?>,
                    response: Response<checkwordResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        result.memoword as Array<checkworddata>
                        for (i in result.memoword ) {
                            checkset.add(i.Wordid)

                        }
                        memorizedwordlist= checkset
                    }
                }
            }
            override fun onFailure(
                    call: Call<checkwordResponse?>,
                    t: Throwable
            ) {
                Toast.makeText(wordbookact, "암기한 단어를 불러오던중 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("암기 단어리스트를 가져오던 중 에러 발생", t.message!!)

            }
        })



    }

    fun checkword(wordid: Int): Boolean {

        var isSucess: Boolean=false
        var isFail=false
        service!!.Checkword(checkwordinputdata(LoginActivity.Useruid,wordid,bookid))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                    call: Call<NormalResponse?>,
                    response: Response<NormalResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        WordbookActivity.memorizedwordlist.add(wordid)
                        isSucess=true
                    }
                }
            }
            override fun onFailure(
                    call: Call<NormalResponse?>,
                    t: Throwable
            ) {
                Toast.makeText(wordbookact, "암기한 단어를 체크하던 중 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("암기한 단어를 체크하던 중 에러 발생", t.message!!)
                isFail=true

            }
        })
        CoroutineScope(Dispatchers.Main).launch {
            while(!isSucess){
                delay(50)
                if(isFail)
                    break
            }
            if(isSucess)
                updatedata()

        }
        return isSucess
    }
    fun uncheckword(wordid :Int):Boolean {

        var isSucess: Boolean=false
        var isFail=false

        service!!.Uncheckword(checkwordinputdata(LoginActivity.Useruid,wordid,bookid))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                    call: Call<NormalResponse?>,
                    response: Response<NormalResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){

                        isSucess=true
                        WordbookActivity.memorizedwordlist.remove(wordid)

                    }
                }
            }
            override fun onFailure(
                    call: Call<NormalResponse?>,
                    t: Throwable
            ) {
                Toast.makeText(wordbookact, "암기한 단어를 체크해제 하던 중 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("암기한 단어를 체크해제하던 중 에러 발생", t.message!!)
                isFail=true

            }
        })
        CoroutineScope(Dispatchers.Main).launch {
            while(!isSucess){
                delay(50)
                if(isFail)

                    break
            }
            if(isSucess)
                updatedata()

        }


        return isSucess
    }
    private fun updatedata(){

        loaddata(bookid)
        MemorizedwordFragment.memorizedwordfragment?.loaddata()
        wordFragment?.loaddata()
    }


}