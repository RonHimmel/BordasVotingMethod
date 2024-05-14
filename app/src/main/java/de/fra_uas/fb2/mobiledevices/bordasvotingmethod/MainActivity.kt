package de.fra_uas.fb2.mobiledevices.bordasvotingmethod
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val savedTextOptions = intent.getStringExtra("textOptions")
        val savedNumberVotes = intent.getIntExtra("numberVotes", 0)
        val savedPairList = intent.getStringExtra("pairList")
        val savedStringList = intent.getStringArrayListExtra("savedStringList")

        val addVoteButton: Button = findViewById(R.id.ButtonAddVote)
        val intOptions: EditText = findViewById(R.id.EditTextOptionsNumber)
        val textOptions : EditText = findViewById(R.id.EditTextOptions)
        val startOverButton: Button = findViewById(R.id.ButtonStartOver)
        val numberOfVotes: TextView = findViewById(R.id.TextViewVotesNumber)
        val resultVotes : TextView = findViewById(R.id.TextViewResults)
        val switchResults : Switch = findViewById(R.id.SwitchResults)


        numberOfVotes.text = savedNumberVotes.toString()                                            // if we came from the second activity the # of votes is rewritten
        intOptions.setText(savedNumberOptions.toString())                                           // also the # of options are displayed again
        textOptions.setText(savedTextOptions)                                                       // and the options

        startOverButton.setOnClickListener{                                                  // here we delete all user inputs to start a new vote
            intOptions.setText("0")
            numberOfVotes.text = "0"
            textOptions.setText("")
        }

        textOptions.setOnClickListener{
            if(savedNumberVotes!=0&&intOptions.text.toString().toInt()!=savedNumberVotes){          //if its not the first vote and the number of options is changed we have to restart
                numberOfVotes.text = "0"
                textOptions.setText("")
            }
        }

        addVoteButton.setOnClickListener {
            val numberInput = intOptions.text.toString()
            if (numberInput.isNotEmpty()&&numberInput.toInt()>1&&numberInput.toInt()<11&&
                !textOptions.text.contains("<not unique>")) {
                val numberOptions = numberInput.toInt()
                val numberVotes = numberOfVotes.text.toString().toInt() + 1                         //adds 1 to the voting counter
                val intent = Intent(this, MainActivity2::class.java)
                val inputText = savedTextOptions ?: textOptions.text.toString()
                val options = inputText.split(",").map { it.trim() }                        //splits the string into the different options using the split method with ","
                                                                                                        //sends all values to the other activity
                intent.putStringArrayListExtra("options", ArrayList(options))
                intent.putExtra("textOptions", inputText)
                intent.putExtra("numberOptions", numberOptions)
                intent.putExtra("numberVotes", numberVotes)
                intent.putStringArrayListExtra("savedStringList",
                    savedStringList?.let { it1 -> ArrayList(it1) })
                startActivity(intent)
            } else if(textOptions.text.contains("<not unique>")){
                Toast.makeText(this, "You can not name an option <not unique>", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Please enter a number from 2 to 10", Toast.LENGTH_SHORT).show()
            }
        }

        switchResults.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked&&numberOfVotes.text!="0") {
                resultVotes.text = savedPairList
            }else resultVotes.text = ""
        }

    }

}

