package com.example.VocabularyBook.Adapter


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.*
import kotlinx.android.synthetic.main.activity_editword.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditWordbookAdapter(var context: EditWordbookActivity, val wordlist: ArrayList<worddata>) : RecyclerView.Adapter<EditWordbookAdapter.CustomViewHolder>(){
    lateinit var api:ServiceApi

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.editword_item,parent,false)

        val retrofit = RetrofitClient.client
        api = retrofit.create(ServiceApi::class.java)

        return CustomViewHolder(
                view
        ) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return wordlist.size
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.word_eng.text = wordlist.get(position).word_eng
        holder.word_mean.text = wordlist.get(position).mean
        holder.btn_edit.setOnClickListener(null)
        holder.btn_del.setOnClickListener(null)
        holder.btn_edit.setOnClickListener{
            val intent= Intent(context, EditwordActivity::class.java)
            intent.putExtra("useruid",context.UserUid)
            intent.putExtra("bookid", context.bookid)
            intent.putExtra("wordid",wordlist.get(position).Wordid)
            intent.putExtra("position",position)
            intent.putExtra("wordeng",wordlist.get(position).word_eng)
            intent.putExtra("wordmean",wordlist.get(position).mean)
            context.startActivity(intent)
        }
        holder.btn_del.setOnClickListener{
            DeleteWord(context.bookid,wordlist.get(position).Wordid,position)
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word_eng =itemView.findViewById<TextView>(R.id.tv_editwordbook_eng)
        val word_mean=itemView.findViewById<TextView>(R.id.tv_editwordbook_mean)
        val btn_edit = itemView.findViewById<Button>(R.id.btn_editwordbook_edit)
        val btn_del = itemView.findViewById<Button>(R.id.btn_editwordbook_delete)
    }

    fun DeleteWord(bookid:Int,wordid:Int,pos:Int){
        context.showProgress(true)
        var isSuccess=false
        var isFinish=false


        api.DelWord(delwordinputdata(bookid, wordid))!!.enqueue(object : Callback<NormalResponse?> {
            override fun onResponse(
                    call: Call<NormalResponse?>,
                    response: Response<NormalResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    if (result.code == 200) {
                        context.Toastmsg("단어 삭제 완료")
                        isSuccess=true
                        Log.d("TAG", "단어 삭제 성공")
                    } else {
                        context.Toastmsg("단어 삭제 실패...")
                        Log.d("TAG", "오류 삭제 실패")
                    }
                }
                isFinish = true
            }
            override fun onFailure(call: Call<NormalResponse?>, t: Throwable) {
                context.Toastmsg("단어 삭제 실패...")
                Log.d("TAG", "오류 삭제 실패")
                isFinish = true
            }
        })
        GlobalScope.launch(Dispatchers.Main) {
            while(!isFinish){
                delay(100)
            }
            context.showProgress(false)
            if(isSuccess){
                EditWordbookActivity.editwordbookact.UpdateData(pos)
            }
        }
    }
}