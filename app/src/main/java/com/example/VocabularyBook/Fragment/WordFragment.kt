package com.example.VocabularyBook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.Adapter.AllwordAdpater
import com.example.VocabularyBook.Adapter.MemorizedwordAdapter
import com.example.VocabularyBook.R
import com.example.VocabularyBook.WordbookActivity

class WordFragment: Fragment() {
    lateinit var rv_uncheckwordlist : RecyclerView

    companion object {
        var wordFragment: WordFragment? =null
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_word, container, false)
        rv_uncheckwordlist=view.findViewById((R.id.rv_wordlist))as RecyclerView
        wordFragment=this
        loaddata()
        return view
    }

    fun loaddata() {
        rv_uncheckwordlist.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        rv_uncheckwordlist.adapter =  MemorizedwordAdapter(false,requireContext(), WordbookActivity.wordlistarray, WordbookActivity.memorizedwordlist)

    }
}