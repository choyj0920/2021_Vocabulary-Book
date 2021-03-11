package com.example.VocabularyBook.Adapter

import com.example.VocabularyBook.worddata


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.R
import com.example.VocabularyBook.WordbookActivity


class EditWordbookAdapter(var context: Context, val wordlist: ArrayList<worddata>) : RecyclerView.Adapter<EditWordbookAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.editword_item,parent,false)
        return CustomViewHolder(
                view
        ) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return wordlist.size
    }
    //
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.word_eng.text = wordlist.get(position).word_eng
        holder.word_mean.text = wordlist.get(position).mean
        holder.btn_edit.setOnClickListener(null)
        holder.btn_del.setOnClickListener(null)
        holder.btn_edit.setOnClickListener{
            
        }
        holder.btn_del.setOnClickListener{

        }

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val word_eng =itemView.findViewById<TextView>(R.id.tv_editword_eng)
        val word_mean=itemView.findViewById<TextView>(R.id.tv_editword_mean)
        val btn_edit = itemView.findViewById<CheckBox>(R.id.btn_editwordbook_edit)
        val btn_del = itemView.findViewById<CheckBox>(R.id.btn_editwordbook_delete)

    }
}