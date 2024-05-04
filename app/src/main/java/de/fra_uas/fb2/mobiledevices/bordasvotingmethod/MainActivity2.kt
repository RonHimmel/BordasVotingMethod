package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
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
        val seekBarsLayout: LinearLayout = findViewById(R.id.seekBarsLayout)

        confirmVoteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("numberOptions", numberOptions)
            intent.putStringArrayListExtra("options", ArrayList(options))
            intent.putExtra("textOptions", text)
            intent.putExtra("numberVotes", savedNumberVotes )
            startActivity(intent)
        }

        val cancelVoteButton: Button = findViewById(R.id.ButtonCancelVote)

        cancelVoteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("numberOptions", numberOptions)
            intent.putStringArrayListExtra("options", ArrayList(options))
            intent.putExtra("textOptions", text)
            intent.putExtra("numberVotes", savedNumberVotes -1)
            startActivity(intent)
        }

        for (i in 0 until numberOptions) {
            val seekBar = SeekBar(this)
            seekBar.progress = 0
            val seekBarName = if (i < options.size&&options[i]!="") options[i] else "Option ${i+1}"
            val textView = TextView(this)
            textView.text = seekBarName
            seekBarsLayout.addView(textView)
            seekBarsLayout.addView(seekBar)
        }
    }
}