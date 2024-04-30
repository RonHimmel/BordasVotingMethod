package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val confirmVoteButton: Button = findViewById(R.id.ButtonConfirmVote)

        confirmVoteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val cancelVoteButton: Button = findViewById(R.id.ButtonCancelVote)

        cancelVoteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val number = intent.getIntExtra("number", 0)
        val seekBarsLayout: LinearLayout = findViewById(R.id.seekBarsLayout)
        val options = intent.getStringArrayListExtra("options") ?: emptyList<String>()
        val seekBarValues: MutableList<Int> = MutableList(options.size) { 0 }

        val dynamicTextView: TextView = findViewById(R.id.textViewBordaVoteDynamic)

        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val index = seekBarsLayout.indexOfChild(seekBar?.parent as? LinearLayout) / 2
                if (index >= 0 && index < seekBarValues.size) {
                    seekBarValues[index] = progress
                    println(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        for (i in 0 until number) {
            val seekBar = SeekBar(this)
            seekBar.progress = 0
            val seekBarName = if (i < options.size&&options[i]!="") options[i] else "Option ${i+1}"
            val textView = TextView(this)
            textView.text = seekBarName
            seekBarsLayout.addView(textView)
            seekBarsLayout.addView(seekBar)
            seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        }
    }
}