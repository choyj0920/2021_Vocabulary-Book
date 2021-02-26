package com.example.VocabularyBook

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import com.magicgoop.tagsphere.item.TextTagItem
import kotlinx.android.synthetic.main.activity_wordmemory.*

class WordmemoryActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordmemory)


        (0..40).map {
            TextTagItem(text = "Some text $it")
        }.toList().let {
            tagView.addTagList(it)
        }

        tagView.setTextPaint(
                TextPaint().apply {
                    isAntiAlias = true
                    textSize = resources.getDimension(R.dimen.tag_text_size)
                    color = Color.DKGRAY
                }
        )

        


    }

}