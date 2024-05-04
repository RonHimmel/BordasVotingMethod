package de.fra_uas.fb2.mobiledevices.bordasvotingmethod
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
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
        val savedOptions = intent.getStringArrayListExtra("options") ?: emptyList<String>()
        val savedTextOptions = intent.getStringExtra("textOptions")
        val savedNumberVotes = intent.getIntExtra("numberVotes", 0)

        val addVoteButton: Button = findViewById(R.id.ButtonAddVote)
        val intOptions: EditText = findViewById(R.id.EditTextOptionsNumber)
        val textOptions : EditText = findViewById(R.id.EditTextOptions)
        val startOverButton: Button = findViewById(R.id.ButtonStartOver)
        val numberOfVotes: TextView = findViewById(R.id.TextViewVotesNumber)


        numberOfVotes.text = savedNumberVotes.toString()
        intOptions.setText(savedNumberOptions.toString())
        textOptions.setText(savedTextOptions)

        startOverButton.setOnClickListener{
            intOptions.setText("0")
            numberOfVotes.text = "0"
            textOptions.setText("")
        }

        addVoteButton.setOnClickListener {
            val numberInput = intOptions.text.toString()
            if (numberInput.isNotEmpty()&&numberInput.toInt()>1&&numberInput.toInt()<11) {
                val numberOptions = numberInput.toInt()
                val numberVotes = numberOfVotes.text.toString().toInt() + 1
                val intent = Intent(this, MainActivity2::class.java)
                val inputText = textOptions.text.toString()
                val options = inputText.split(",").map { it.trim() }                        //splits the string into the different options using the split method with ","
                                                                                                        //sends all values to the other activity
                intent.putStringArrayListExtra("options", ArrayList(options))
                intent.putExtra("textOptions", inputText)
                intent.putExtra("numberOptions", numberOptions)
                intent.putExtra("numberVotes", numberVotes)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter a number from 2 to 10", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

