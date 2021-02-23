package com.example.VocabularyBook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback


class Wordbook(val bookid : Int, val Rid :Int?, val Uid :Int?, val wordbookname:String)

class WordbookAdapter(var context: Context, val wordbooklist: ArrayList<Wordbook>) : RecyclerView.Adapter<WordbookAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.wordbook_item,parent,false)
        return CustomViewHolder(
            view
        ) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return wordbooklist.size
    }
    //
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.wordbookname.text = wordbooklist.get(position).wordbookname

        holder.itemView.setOnClickListener{
            val intent= Intent(context, WordbookActivity::class.java)
            intent.putExtra("bookid",wordbooklist.get(position).bookid)
            intent.putExtra("Rid",wordbooklist.get(position).Rid)
            intent.putExtra("Uid",wordbooklist.get(position).Uid)
            intent.putExtra("wordbookname",wordbooklist.get(position).wordbookname)

            context.startActivity(intent)
        }

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordbookname =itemView.findViewById<TextView>(R.id.board_name)         // 이름

    }
}