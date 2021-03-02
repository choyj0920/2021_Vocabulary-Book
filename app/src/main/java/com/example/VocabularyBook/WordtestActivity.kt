package com.example.VocabularyBook

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wordtest.*
import kotlin.random.Random

class WordtestActivity:AppCompatActivity() {
    var anslist:ArrayList<worddata> = arrayListOf()
    var sucessCount: Int=0
    var currentCount:Int =0
    var problemCount=5
    var wordcnt=0

    var curProblem:Int =0
    var curAns:ArrayList<Int> = arrayListOf(0,0,0,0)
    lateinit var tvansviewarray:ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordtest)

        wordcnt=WordbookActivity.wordlistarray.size
        if(wordcnt-WordbookActivity.memorizedwordlist.size <problemCount){
            problemCount = wordcnt-WordbookActivity.memorizedwordlist.size
        }
        tvansviewarray= arrayListOf(tv_wordtest_ans1,tv_wordtest_ans2,tv_wordtest_ans3,tv_wordtest_ans4)
        updateNewproblem()
        for (i in 0..3){
            tvansviewarray[i].setOnClickListener {
                if(curProblem==curAns[i]){ // 문제를 맞았으면
                    Toast.makeText(this, "정답!" ,Toast.LENGTH_SHORT).show()
                    WordbookActivity.wordbookact.checkword(WordbookActivity.wordlistarray[curProblem].Wordid)
                    sucessCount+=1
                }
                else{
                    Toast.makeText(this, "오답ㅠ" ,Toast.LENGTH_SHORT).show()
                }
                currentCount+=1
                tv_wordtestscore.text="$sucessCount/$currentCount"
                if(problemCount>currentCount){
                    updateNewproblem()
                }
                else{
                    Toast.makeText(this, "테스트종료! 수고하셨습니다. 결과 : $sucessCount/$problemCount " ,Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }

    fun updateNewproblem(){

        curProblem= Random.nextInt(wordcnt)
        while (WordbookActivity.memorizedwordlist.contains(WordbookActivity.wordlistarray.get(curProblem).Wordid)){
            curProblem= Random.nextInt(wordcnt)
        }
        tv_wordtest_eng.text=WordbookActivity.wordlistarray.get(curProblem).word_eng

        var solution=Random.nextInt(4)

        for(i in 0..3){
            curAns[i]=if (i==solution) curProblem else  Random.nextInt(wordcnt)

            tvansviewarray[i].text=WordbookActivity.wordlistarray.get(curAns[i]).mean

        }

    }

}