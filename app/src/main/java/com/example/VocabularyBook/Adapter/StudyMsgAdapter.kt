package com.example.VocabularyBook.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.*


class StudyMsgAdapter(var context: StudyNoticeActivity, val studylist: ArrayList<studymsg>,var UserUid:Int) : RecyclerView.Adapter<StudyMsgAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate( R.layout.studymsg_item ,parent,false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studylist.size
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.username.setText(studylist.get(position).username)
        holder.msg.setText(studylist.get(position).msg)
        if (UserUid != studylist.get(position).Uid){
            holder.msg.isEnabled=false
            holder.btn.visibility=View.GONE
        }
        else{
            holder.btn.setOnClickListener(null)
            holder.btn.setOnClickListener{
                context.updateMsg(UserUid,holder.msg.text.toString())

            }
        }

    }

    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username =itemView.findViewById<TextView>(R.id.tv_studymsg_username)
        val msg =itemView.findViewById<EditText>(R.id.tv_studymsg_msg)
        val btn =itemView.findViewById<Button>(R.id.btn_studymsg)
    }


}