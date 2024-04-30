package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        val addVoteButton: Button = findViewById(R.id.ButtonAddVote)
        val numberInputEditText: EditText = findViewById(R.id.EditTextOptionsNumber)
        val textInputEditTextOptions : EditText = findViewById(R.id.EditTextOptions)

        addVoteButton.setOnClickListener {
            val numberInput = numberInputEditText.text.toString()
            if (numberInput.isNotEmpty()&&numberInput.toInt()>1&&numberInput.toInt()<11) {
                val number = numberInput.toInt()
                val intent = Intent(this, MainActivity2::class.java)
                intent.putExtra("number", number)
                val inputText = textInputEditTextOptions.text.toString()
                val options = inputText.split(",").map { it.trim() }                        //splits the string into the different options using the split method with ","
                intent.putStringArrayListExtra("options", ArrayList(options))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter a number from 2 to 10", Toast.LENGTH_SHORT).show()
            }
        }




    }
}

