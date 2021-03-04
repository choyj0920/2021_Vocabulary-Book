package com.example.VocabularyBook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.VocabularyBook.Fragment.AllwordFragment
import com.example.VocabularyBook.Fragment.MemorizedwordFragment
import com.example.VocabularyBook.Fragment.WordFragment
import com.google.android.material.tabs.TabLayoutMediator


private const val NUM_PAGES = 3

class WordlistActivity: FragmentActivity() {


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordlist)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        var tabList=arrayListOf(
                "전체 단어","모르는 단어","외운 단어"
        )
        TabLayoutMediator(findViewById(R.id.tabLayout), viewPager){tab,position->
            tab.text=tabList[position]
        }.attach()


    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AllwordFragment()
                1 -> WordFragment()
                else -> MemorizedwordFragment()
            }
        }

    }
}

