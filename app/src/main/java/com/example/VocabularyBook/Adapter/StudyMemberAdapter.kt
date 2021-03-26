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


class StudyMemberAdapter(var context: StudyMemberActivity ,val studymemberlist: ArrayList<studymsg>, var UserUid:Int) : RecyclerView.Adapter<StudyMemberAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate( R.layout.studymember_item ,parent,false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studymemberlist.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.username.setText(studymemberlist.get(position).username)
        holder.msg.setText(studymemberlist.get(position).msg)
        if (UserUid != studymemberlist.get(position).Uid){
            holder.btn.visibility=View.GONE
        }
        else{
            holder.btn.setOnClickListener {
                // 탈퇴 기능
                context.OutStudy(studymemberlist.get(position).Uid)
            }
        }
    }

    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username =itemView.findViewById<TextView>(R.id.tv_studymember_username)
        val btn =itemView.findViewById<Button>(R.id.btn_studymember)
        val msg=itemView.findViewById<TextView>(R.id.tv_studymember_msg)
    }
}

