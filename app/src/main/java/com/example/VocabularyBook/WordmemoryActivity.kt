package com.example.VocabularyBook

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import com.magicgoop.tagsphere.item.TextTagItem
import kotlinx.android.synthetic.main.activity_wordmemory.*
import android.graphics.Canvas
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.magicgoop.tagsphere.OnTagTapListener

import com.magicgoop.tagsphere.item.TagItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class WordmemoryActivity:AppCompatActivity() {
    lateinit var memorywordlist : ArrayList<worddata>
    lateinit var memorywordmap : HashMap<String?,String?>
    companion object{
        lateinit var wordmemoryact:WordmemoryActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordmemory)
        wordmemoryact=this
        initTagView()



    }

    private fun initTagView() {
        tagSphereView.setTextPaint(
            TextPaint().apply {
                isAntiAlias = true
                textSize = resources.getDimension(R.dimen.tag_text_size)
                color = Color.DKGRAY
            }
        )
        memorywordmap= hashMapOf()

        memorywordlist= arrayListOf()
        for(i in WordbookActivity.wordlistarray){
            if (!WordbookActivity.memorizedwordlist.contains(i.Wordid)){
                memorywordlist.add(i)
            }
        }


        val loremSize = memorywordlist.size
        (0..40).map {
            var tempindex=Random.nextInt(loremSize)
            memorywordmap.set(memorywordlist[tempindex].word_eng,memorywordlist[tempindex].mean)
            TextTagItem(text = memorywordlist[tempindex].word_eng.toString())
        }.toList().let {
            tagSphereView.addTagList(it)
        }
        tagSphereView.setRadius(3f)
        tagSphereView.setOnTagTapListener(object : OnTagTapListener {
            override fun onTap(tagItem: TagItem) {
                Toast.makeText(
                    wordmemoryact ,
                    "${(tagItem as TextTagItem).text}: ${memorywordmap[(tagItem as TextTagItem).text]}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }




}

