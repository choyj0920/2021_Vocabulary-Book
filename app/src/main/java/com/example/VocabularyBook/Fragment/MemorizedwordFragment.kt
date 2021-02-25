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

class MemorizedwordFragment: Fragment() {
    lateinit var rv_checkedwordlist : RecyclerView

    companion object {
        lateinit var memorizedwordfragment: MemorizedwordFragment
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_memorized, container, false)
        rv_checkedwordlist=view.findViewById((R.id.rv_memorizedwordlist))as RecyclerView
        memorizedwordfragment=this
        loaddata()
        return view
    }

    fun loaddata() {
        rv_checkedwordlist.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        rv_checkedwordlist.adapter =  MemorizedwordAdapter(true,requireContext(), WordbookActivity.wordlistarray, WordbookActivity.memorizedwordlist)

    }
}