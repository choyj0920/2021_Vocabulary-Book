package com.example.VocabularyBook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.VocabularyBook.Adapter.AllwordAdpater
import com.example.VocabularyBook.R
import com.example.VocabularyBook.WordbookActivity
import com.example.VocabularyBook.worddata
import kotlinx.android.synthetic.main.fragment_allword.*

class AllwordFragment: Fragment() {
    lateinit var rv_allword :RecyclerView
    lateinit var allwordlist : ArrayList<worddata>

    companion object {
        lateinit  var allwordfragment : AllwordFragment
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_allword , container, false)
        rv_allword = view.findViewById((R.id.rv_allwordlist))as RecyclerView
        allwordfragment=this
        loaddata()
        rv_allword.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        rv_allword.adapter =  AllwordAdpater( requireContext(), WordbookActivity.wordlistarray,WordbookActivity.memorizedwordlist)




        return view
    }

    fun loaddata() {
        rv_allword.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        rv_allword.adapter =  AllwordAdpater( requireContext(), WordbookActivity.wordlistarray,WordbookActivity.memorizedwordlist)

    }


}