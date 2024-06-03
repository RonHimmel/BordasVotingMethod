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
        val stringToPairList = mutableListOf<Pair<String, Int>>()                                       //list of the old values from the last vote


        val bundle = intent.extras
        val numberOptions = bundle!!.getInt("numberOptions")
        val options = bundle.getStringArrayList("options")
        val text = bundle.getString("textOptions")
        val savedNumberVotes = bundle.getInt("numberVotes")



        if(savedNumberVotes>1) {
            val savedStringList = bundle.getStringArrayList("savedStringList")
            savedStringList?.forEach { string ->
                val parts = string.split(",")
                if (parts.size == 2) {
                    val first = parts[0]
                    val second = parts[1].toInt()
                    stringToPairList.add(Pair(first, second))
                }
            }
        }else{
            ArrayList<String>()
            for(i in 0 until numberOptions){
                stringToPairList.add(Pair("", 0))
            }
        }


        val confirmVoteButton: Button = findViewById(R.id.ButtonConfirmVote)
        val cancelVoteButton: Button = findViewById(R.id.ButtonCancelVote)
        val seekBarsLayout: LinearLayout = findViewById(R.id.seekBarsLayout)
        val voteScreen: TextView = findViewById(R.id.textViewBordaVoteDynamic)

        var updatedListString = ""
        val seekBarValues = IntArray(numberOptions)
        val seekBar = ArrayList<SeekBar>(numberOptions)
        val stringList = ArrayList<String>()
        var newStringToPairList = mutableListOf<Pair<String, Int>>()


        fun intentions(a: Int){
            val resultIntent = Intent()
            resultIntent.putExtra("numberOptions", numberOptions)
            resultIntent.putExtra("textOptions", text)
            if(a==1) {
                resultIntent.putExtra("numberVotes", savedNumberVotes )
                resultIntent.putExtra("pairList", updatedListString)
                resultIntent.putStringArrayListExtra("savedStringList", ArrayList(stringList))
            } else {
                resultIntent.putExtra("numberVotes", savedNumberVotes -1)                 //if vote is cancelled we have to remove the counter vote added
                resultIntent.putExtra("pairList", stringToPairList.sortedByDescending { it.second }
                    .joinToString (separator = "\n"){"${it.first} -> ${it.second}"})
                resultIntent.putStringArrayListExtra("savedStringList", ArrayList(stringList))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
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
            val tripleList = mutableListOf(                                                             //used a List of Triples to always have the name, value of the seekbar and actual value in one place
                Triple(if(options!![0]!="") options[0]else "Option 1", seekBarValues[0], 0)
            )
            for (j in 1 until numberOptions) {
                if(j < options.size && options[j] != "") {
                    tripleList.add(Triple(options[j], seekBarValues[j], 0))
                }else{
                    tripleList.add(Triple("Option ${j+1}", seekBarValues[j], 0))
                }

            }
            val sortedList = tripleList.sortedByDescending { it.second }.toMutableList()                //sort the list by the seekbar value
            for(y in 0 until numberOptions){                                                      //now i iterate through the list and check if the seekbar value is unique
                val existingTriple = sortedList[y]
                var checkNumber = 0
                if(checkIfUnique(existingTriple.second, sortedList)) checkNumber = numberOptions-y-1    //if the seekbar value is unique it gets the borda value assigned according to its place in the sorted list
                else checkNumber =-2                                                                    //-2 means not unique
                val updatedTriple = Triple(existingTriple.first, existingTriple.second, checkNumber)
                sortedList[y] = updatedTriple
            }
            updatedListString = sortedList.joinToString(separator = "\n")
            {if(it.third!=-2){ "${it.first} -> ${it.third}" }else {"${it.first} -> <not unique>" }}     //here i put the borda value and the name in the string list
            voteScreen.text = updatedListString
            if(!updatedListString.contains("<not unique>")){
                val alphabeticList = sortedList.sortedByDescending { it.first }.toMutableList()         //now we have to sort the list by the name to prevent points getting to the wrong place
                stringList.clear()
                for(i in 0 until numberOptions){
                    stringList.add("${alphabeticList[i].first},${alphabeticList[i].third}")
                }
                newStringToPairList.clear()                                                             //the list has to be emptied first
                stringList.forEach { string ->
                    val parts = string.split(",")
                    if (parts.size == 2) {
                        val first = parts[0]
                        val second = parts[1].toInt()
                        newStringToPairList.add(Pair(first, second))
                    }
                }
            }
        }

        confirmVoteButton.setOnClickListener {
            if(updatedListString.contains("<not unique>")||voteScreen.text==""){
                Toast.makeText(this, "Vote is not unique!", Toast.LENGTH_SHORT).show()
            }else {
                for(i in 0 until numberOptions){
                    newStringToPairList[i] = Pair(newStringToPairList[i].first,
                        newStringToPairList[i].second+stringToPairList[i].second )                      //here the points would be assigned wrong if it was not sorted alphabetically
                    stringList[i]="${newStringToPairList[i].first},${newStringToPairList[i].second}"
                }
                newStringToPairList = newStringToPairList.sortedByDescending { it.second }.toMutableList()
                updatedListString = newStringToPairList.joinToString (separator = "\n"){"${it.first} -> ${it.second}"}
                intentions(1)
            }
        }
        cancelVoteButton.setOnClickListener {
            Toast.makeText(this, "Vote has been canceled!", Toast.LENGTH_SHORT).show()
            stringList.clear()
            for(i in 0 until numberOptions){
                stringList.add("${stringToPairList[i].first},${stringToPairList[i].second}")
            }
            intentions(0)
        }

        for (i in 0 until numberOptions) {                                                      //creates the seekbars and the textviews
            val bar = SeekBar(this)
            bar.progress = 0
            seekBarValues[i] = bar.progress                                                             //the array is filled with 0
            seekBar.add(bar)
            val seekBarName = if (i < options!!.size&& options[i]!="") options[i] else "Option ${i+1}"
            val textView = TextView(this)
            textView.text = seekBarName
            seekBarsLayout.addView(textView)
            seekBarsLayout.addView(bar)

            val listener = object : SeekBar.OnSeekBarChangeListener {                                   //each seekbar has its own listener
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    seekBarValues[i] = progress                                                         //the progress is put in the array
                    calculateResults()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
            bar.setOnSeekBarChangeListener(listener)
        }
    }

}