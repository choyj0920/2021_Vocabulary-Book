package com.example.VocabularyBook.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.MystudylistActivity
import com.example.VocabularyBook.R
import com.example.VocabularyBook.StudyActivity
import com.example.VocabularyBook.WordbookActivity


class Study(val Rid : Int, val room_name :String, val host :Int?, val notice:String, val isaccept:Boolean)

class StudyAdapter(var context: MystudylistActivity, val studylist: ArrayList<Study>,var UserUid:Int) : RecyclerView.Adapter<StudyAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate( if(viewType==1 )R.layout.study_item else R.layout.study_unparticipate_item ,parent,false)
        return if(viewType==1) CustomViewHolder(view) else CustomViewHoder2(view) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return studylist.size
    }

    override fun getItemViewType(position: Int): Int {
        if(studylist[position].isaccept){
            return 1
        }
        else{
            return 2
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.studyname.text = studylist.get(position).room_name
        if(studylist[position].isaccept) {

            holder.itemView.setOnClickListener{
                val intent= Intent(context, StudyActivity::class.java)
                intent.putExtra("Rid",studylist.get(position).Rid)
                intent.putExtra("host",studylist.get(position).host)
                intent.putExtra("notice",studylist.get(position).notice)
                intent.putExtra("room_name",studylist.get(position).room_name)
                intent.putExtra("useruid",UserUid)
                context.startActivity(intent)
            }
        }else{
            holder as CustomViewHoder2
            holder.aceptbtn.setOnClickListener{
                context.aceptStudy(studylist.get(position).Rid)

            }
            holder.rejectbtn.setOnClickListener{
                context.rejectStudy(studylist.get(position).Rid)

            }


        }
    }

    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studyname =itemView.findViewById<TextView>(R.id.tv_studylist_studyname)
    }

    class CustomViewHoder2(itemView: View): CustomViewHolder(itemView){
        val aceptbtn=itemView.findViewById<Button>(R.id.btn_studylist_acept)
        val rejectbtn=itemView.findViewById<Button>(R.id.btn_studylist_reject)
    }
}