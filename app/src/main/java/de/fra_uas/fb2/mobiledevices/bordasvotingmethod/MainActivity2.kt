package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val numberOptions = intent.getIntExtra("numberOptions", 0)
        val options = intent.getStringArrayListExtra("options") ?: emptyList<String>()
        val text = intent.getStringExtra("textOptions")
        val savedNumberVotes = intent.getIntExtra("numberVotes", 0)

        val confirmVoteButton: Button = findViewById(R.id.ButtonConfirmVote)
        val cancelVoteButton: Button = findViewById(R.id.ButtonCancelVote)
        val seekBarsLayout: LinearLayout = findViewById(R.id.seekBarsLayout)
        val voteScreen: TextView = findViewById(R.id.textViewBordaVoteDynamic)

        var updatedListString = ""
        val seekBarValues = IntArray(numberOptions)
        val seekBar = ArrayList<SeekBar>(numberOptions)


        fun intentions(a: Int){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("numberOptions", numberOptions)
            intent.putStringArrayListExtra("options", ArrayList(options))
            intent.putExtra("textOptions", text)
            if(a==1) {intent.putExtra("numberVotes", savedNumberVotes )                         //if vote is cancelled we have to remove the counter vote added
            } else intent.putExtra("numberVotes", savedNumberVotes -1)
            intent.putExtra("pairList", updatedListString)
            startActivity(intent)
        }

        fun checkIfUnique(value: Int, options: List<Triple<String, Int, Int>>): Boolean{
            var test = 0
            for(element in options){
                if(value== element.second)
                    test++
            }
            return test <= 1                                                                            //true if it is only true once ( with itself)
        }


        fun calculateResults(){
            val tripleList = mutableListOf(
                Triple(if(options[0]!="")options[0]else "Option 1", seekBarValues[0], 0)
            )
            for (j in 0 until numberOptions) {
                if(j in 1 until numberOptions){
                    if(j < options.size && options[j] != "") {
                        tripleList.add(Triple(options[j], seekBarValues[j], 0))
                    }else{
                        tripleList.add(Triple("Option ${j+1}", seekBarValues[j], 0))
                    }
                }
            }
            val sortedList = tripleList.sortedByDescending { it.second }.toMutableList()
            for(y in 0 until numberOptions){
                val existingPair = sortedList[y]
                var checkNumber = 0
                if(checkIfUnique(existingPair.second, sortedList)) checkNumber = numberOptions-y-1
                else checkNumber =-2                                                                    //-2 means not unique
                val updatedPair = Triple(existingPair.first, existingPair.second, checkNumber)
                sortedList[y] = updatedPair
            }
            updatedListString = sortedList.joinToString(separator = "\n") {if(it.third!=-2){ "${it.first} -> ${it.third}" }else {"${it.first} -> *invalid*" }}
            voteScreen.text = updatedListString

        }

        confirmVoteButton.setOnClickListener {
            if(updatedListString.contains("*invalid*")){
                Toast.makeText(this, "All Values have to be unique", Toast.LENGTH_SHORT).show()
            }else intentions(1)
        }
        cancelVoteButton.setOnClickListener {
            intentions(0)
        }

        for (i in 0 until numberOptions) {
            val bar = SeekBar(this)
            bar.progress = 0
            seekBarValues[i] = bar.progress
            seekBar.add(bar)
            val seekBarName = if (i < options.size&&options[i]!="") options[i] else "Option ${i+1}"
            val textView = TextView(this)
            textView.text = seekBarName
            seekBarsLayout.addView(textView)
            seekBarsLayout.addView(bar)

            val listener = object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    seekBarValues[i] = progress
                    calculateResults()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
            bar.setOnSeekBarChangeListener(listener)
        }
    }

}