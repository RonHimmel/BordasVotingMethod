package de.fra_uas.fb2.mobiledevices.bordasvotingmethod
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val savedNumberOptions = intent.getIntExtra("numberOptions", 0)
        var savedTextOptions = intent.getStringExtra("textOptions")
        val savedNumberVotes = intent.getIntExtra("numberVotes", 0)
        var savedPairList = intent.getStringExtra("pairList")
        val savedStringList = intent.getStringArrayListExtra("savedStringList")

        val addVoteButton: Button = findViewById(R.id.ButtonAddVote)
        val intOptions: EditText = findViewById(R.id.EditTextOptionsNumber)
        val textOptions : EditText = findViewById(R.id.EditTextOptions)
        val startOverButton: Button = findViewById(R.id.ButtonStartOver)
        val numberOfVotes: TextView = findViewById(R.id.TextViewVotesNumber)
        val resultVotes : TextView = findViewById(R.id.TextViewResults)
        val switchResults : Switch = findViewById(R.id.SwitchResults)


        numberOfVotes.text = savedNumberVotes.toString()                                            // if we come from the second activity the # of votes is rewritten
        intOptions.setText(savedNumberOptions.toString())                                           // also the # of options are displayed again
        textOptions.setText(savedTextOptions)                                                       // and the options

        startOverButton.setOnClickListener{                                                  // here we delete all user inputs to start a new vote
            intOptions.setText("0")
            numberOfVotes.text = "0"
            textOptions.setText("")
            savedStringList?.clear()
            savedPairList=""
            savedTextOptions=""
        }

        var isClear = true

        textOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text is about to change
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that the text has changed

            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that somewhere within s, the text has been changed
                if(isClear) {
                    numberOfVotes.text = "0"
                    savedStringList?.clear()
                    savedPairList = ""
                    savedTextOptions = ""
                    isClear=false
                }
            }
        })

        intOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text is about to change
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that the text has changed

            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that somewhere within s, the text has been changed
                if(isClear) {
                    numberOfVotes.text = "0"
                    savedStringList?.clear()
                    savedPairList = ""
                    savedTextOptions = ""
                    isClear=false
                }
            }
        })

        addVoteButton.setOnClickListener {
            val numberInput = intOptions.text.toString()
            if (numberInput.isNotEmpty()&&numberInput.toInt()>1&&numberInput.toInt()<11&&
                !textOptions.text.contains("<not unique>")) {
                val numberOptions = numberInput.toInt()
                val numberVotes = numberOfVotes.text.toString().toInt() + 1                         //adds 1 to the voting counter
                val intent = Intent(this, MainActivity2::class.java)
                val inputText = if(savedTextOptions!=null&&savedTextOptions!="")savedTextOptions
                                else textOptions.text.toString()
                val options = inputText?.split(",")?.map { it.trim() }                        //splits the string into the different options using the split method with ","
                                                                                                        //sends all values to the other activity
                intent.putStringArrayListExtra("options", ArrayList(options))
                intent.putExtra("textOptions", inputText)
                intent.putExtra("numberOptions", numberOptions)
                intent.putExtra("numberVotes", numberVotes)
                if(savedStringList!=null) {
                    intent.putStringArrayListExtra("savedStringList", ArrayList(savedStringList))
                }
                startActivity(intent)
            } else if(textOptions.text.contains("<not unique>")){
                Toast.makeText(this, "You can not name an option <not unique>", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Please enter a number from 2 to 10", Toast.LENGTH_SHORT).show()
            }
        }

        switchResults.setOnCheckedChangeListener { _, isChecked ->                                  //function for the switch
            if (isChecked&&numberOfVotes.text!="0") {
                var resultText = ""
                val result = savedPairList?.split("->", "\n")?.map { it.trim() }
                var i = 0
                if (result != null) {
                    do{
                        if(result[i+1]==result[1]){
                            resultText=resultText+"***"+result[i]+ " -> "+ result[i+1]+"***\n"
                        }else resultText=resultText +result[i]+" -> "+ result[i+1]+"\n"
                        i += 2
                    }while(i<result.size)
                }
                resultVotes.text = resultText
            }else resultVotes.text = ""
        }

    }

}

