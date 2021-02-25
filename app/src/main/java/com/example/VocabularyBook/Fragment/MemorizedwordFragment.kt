package com.example.VocabularyBook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.VocabularyBook.R

class MemorizedwordFragment: Fragment() {

    companion object {
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_memorized, container, false)




        return view
    }
}