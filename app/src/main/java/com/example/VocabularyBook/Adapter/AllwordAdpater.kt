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




class AllwordAdpater(var context: Context, val wordlist: ArrayList<worddata>,val checkwordset: HashSet<Int>) : RecyclerView.Adapter<AllwordAdpater.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.allword_item,parent,false)
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
        holder.cb_wordmemorize.setOnCheckedChangeListener(null)
        holder.cb_wordmemorize.isChecked=checkwordset.contains(wordlist.get(position).Wordid)

        holder.cb_wordmemorize.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked){  //외운 단어 였으면
                holder.cb_wordmemorize.isChecked = WordbookActivity.wordbookact.uncheckword(wordlist.get(position).Wordid)

            }else{
                holder.cb_wordmemorize.isChecked = !WordbookActivity.wordbookact.checkword(wordlist.get(position).Wordid)

            }
        }

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val word_eng =itemView.findViewById<TextView>(R.id.tv_allword_eng)
        val word_mean=itemView.findViewById<TextView>(R.id.tv_allword_mean)
        val cb_wordmemorize = itemView.findViewById<CheckBox>(R.id.cb_memorize)

    }
}