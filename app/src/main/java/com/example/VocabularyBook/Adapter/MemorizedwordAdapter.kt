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


class MemorizedwordAdapter(var isshowchecked: Boolean,var context: Context, val wordlist: ArrayList<worddata>,val checkwordset: HashSet<Int>) : RecyclerView.Adapter<MemorizedwordAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(if (viewType==1)  R.layout.allword_item else R.layout.voidword_item ,parent,false)
        return CustomViewHolder(
                view
        ) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return wordlist.size
    }
    //
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        if (getItemViewType(position)==1){
            holder.word_eng.text = wordlist.get(position).word_eng
            holder.word_mean.text = wordlist.get(position).mean
            holder.cb_wordmemorize.setVisibility(View.GONE)

        }else{


        }


    }

    override fun getItemViewType(position: Int): Int {
        if(isshowchecked){
            return if(checkwordset.contains(wordlist[position].Wordid)) 1 else 2
        }
        else{
            return if(checkwordset.contains(wordlist[position].Wordid)) 2 else 1
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val word_eng =itemView.findViewById<TextView>(R.id.tv_allword_eng)
        val word_mean=itemView.findViewById<TextView>(R.id.tv_allword_mean)
        val cb_wordmemorize = itemView.findViewById<CheckBox>(R.id.cb_memorize)

    }
}