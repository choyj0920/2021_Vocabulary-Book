package com.example.VocabularyBook.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.AddwordActivity
import com.example.VocabularyBook.R
import com.example.VocabularyBook.WordbookActivity


class Study(val Rid : Int, val room_name :String, val host :Int?, val notice:String)

class StudyAdapter(var context: Context, val studylist: ArrayList<Study>,var UserUid:Int) : RecyclerView.Adapter<StudyAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.study_item ,parent,false)
        return CustomViewHolder(
                view
        ) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return studylist.size
    }
    //
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.studyname.text = studylist.get(position).room_name

        holder.itemView.setOnClickListener{
            val intent= Intent(context, WordbookActivity::class.java)
            intent.putExtra("Rid",studylist.get(position).Rid)
            intent.putExtra("host",studylist.get(position).host)
            intent.putExtra("notice",studylist.get(position).notice)
            intent.putExtra("roomname",studylist.get(position).room_name)
            intent.putExtra("useruid",UserUid)

            context.startActivity(intent)
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studyname =itemView.findViewById<TextView>(R.id.tv_studylist_studyname)         // 이름

    }
}