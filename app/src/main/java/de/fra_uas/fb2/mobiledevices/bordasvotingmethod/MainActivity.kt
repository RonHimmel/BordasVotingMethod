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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private var savedNumberOptions = 0
    private var savedTextOptions: String? = null
    private var savedNumberVotes = 0
    private var savedPairList: String? = null
    private var savedStringList: ArrayList<String>? = null
    private var isClear = true


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


        val addVoteButton: Button = findViewById(R.id.ButtonAddVote)
        val intOptions: EditText = findViewById(R.id.EditTextOptionsNumber)
        val textOptions : EditText = findViewById(R.id.EditTextOptions)
        val startOverButton: Button = findViewById(R.id.ButtonStartOver)
        val numberOfVotes: TextView = findViewById(R.id.TextViewVotesNumber)
        val resultVotes : TextView = findViewById(R.id.TextViewResults)
        val switchResults : Switch = findViewById(R.id.SwitchResults)


        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extras = result.data?.extras
                if (extras != null){
                    savedNumberOptions = extras.getInt("numberOptions", 0) ?: 0
                    savedTextOptions = extras.getString("textOptions")
                    savedNumberVotes = extras.getInt("numberVotes", 0) ?: 0
                    savedPairList = extras.getString("pairList")
                    savedStringList = extras.getStringArrayList("savedStringList")

                    isClear =false
                    numberOfVotes.text = savedNumberVotes.toString()                                            // if we come from the second activity the # of votes is rewritten
                    intOptions.setText(savedNumberOptions.toString())                                           // also the # of options are displayed again
                    textOptions.setText(savedTextOptions)
                    isClear = true
                }

            } else if (result.resultCode == RESULT_CANCELED) {
                // Handle cancellation if needed
            }
        }


        startOverButton.setOnClickListener{                                                  // here we delete all user inputs to start a new vote
            intOptions.setText("0")
            numberOfVotes.text = "0"
            textOptions.setText("")
            savedStringList?.clear()
            savedPairList=""
            savedTextOptions=""
        }



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
                val inputText = if(savedTextOptions!=null&&savedTextOptions!="")savedTextOptions
                                else textOptions.text.toString()
                val options =
                    inputText?.split(",")?.map { it.trim() }                        //splits the string into the different options using the split method with ","

                val myIntent = Intent(this, MainActivity2::class.java)
                val bundle = Bundle();
                bundle.putInt("numberOptions", numberOptions)
                bundle.putInt("numberVotes", numberVotes)
                bundle.putString("textOptions", inputText)
                bundle.putStringArrayList("options", ArrayList(options!!))
                if(savedStringList!=null) {
                    bundle.putStringArrayList("savedStringList", ArrayList(savedStringList))
                }
                myIntent.putExtras(bundle)
                activityLauncher.launch(myIntent)


                                                                                                        //sends all values to the other activity
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

