package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityLevelOneBinding

class LevelOneActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityLevelOneBinding
    private lateinit var prefs: SharedPreferences

    private val options = mutableListOf("", "A", "B", "C", "D", "E")
    private var currentIndex1 = 0
    private var currentIndex2 = 0
    private var currentIndex3 = 0
    private var currentIndex4 = 0
    private var currentIndex5 = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding = ActivityLevelOneBinding.inflate(layoutInflater)
//        binding2 = ActivityLevelOneFourButtonsBinding.inflate(layoutInflater)

        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)
//        val letters = options.take(viewModel.getNumberOfVerticesForGraph()+1)

        // Get the layout extra from the intent
        val layout = intent.getStringExtra("layout")
        Log.d("LAYOUT:","$layout")

        // Set the content view based on the layout value
        when (layout) {
            "layout1" -> setContentView(R.layout.activity_level_one) // Your first layout XML
            "layout2" -> setContentView(R.layout.activity_level_one_four_buttons) // Your second layout XML
//            "layout3" -> setContentView(R.layout.activity_level_one_five_buttons)
        }

        val square1 = findViewById<Button>(R.id.button11)
        val square2 = findViewById<Button>(R.id.button12)
        val square3 = findViewById<Button>(R.id.button13)
        val square4 = findViewById<Button>(R.id.button14)

        val text = findViewById<TextView>(R.id.textView)
        val verify = findViewById<Button>(R.id.button7)
        val lightbulb = findViewById<ImageView>(R.id.lightbulb)
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)

        val graphView = findViewById<GraphView>(R.id.graphView)
        val starView = findViewById<StarRatingView>(R.id.starView)

        square1.text = options[currentIndex1]
        square2.text = options[currentIndex2]
        square3.text = options[currentIndex3]

        square1.setOnClickListener {
            if (currentIndex1 == viewModel.getNumberOfVerticesForGraph()) {
                currentIndex1 = -1
            }
            currentIndex1 += 1
            square1.text = options[currentIndex1]
        }

        square2.setOnClickListener {
            if (currentIndex2 == viewModel.getNumberOfVerticesForGraph()) {
                currentIndex2 = -1
            }
            currentIndex2 += 1
            square2.text = options[currentIndex2]
        }

        square3.setOnClickListener {
            if (currentIndex3 == viewModel.getNumberOfVerticesForGraph()) {
                currentIndex3 = -1
            }
            currentIndex3 += 1
            square3.text = options[currentIndex3]
        }

        square4?.setOnClickListener {
            if (currentIndex4 == viewModel.getNumberOfVerticesForGraph()) {
                currentIndex4 = -1
            }
            currentIndex4 += 1
            square4.text = options[currentIndex4]
        }

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        if (prefs.getBoolean("levelThree", true)) {
            if (viewModel.currentLevel() < 5) {
                graphView.setNumVertices(4)
                viewModel.setNumberOfVerticesForGraph(4)
            } else {
                graphView.setNumVertices(5)
                viewModel.setNumberOfVerticesForGraph(5)
            }
        } else {
            graphView.setNumVertices(3)
        }

        viewModel.createGraph()
        graphView.redArrowPoints = viewModel.redArrowPoints
        graphView.blueArrowPoints = viewModel.blueArrowPoints

        val spannableString = SpannableString(viewModel.getCommand())
        text.text = color(viewModel.getCommand(), spannableString)

        buttonReturn.setOnClickListener{
            prefs.edit().putBoolean("levelThree", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        lightbulb.setOnClickListener {
            graphView.startArrowAnimation(viewModel.getCommand(), 2000L)
        }

        verify.setOnClickListener {
            if (viewModel.verify(listOf(currentIndex1,currentIndex2,currentIndex3,currentIndex4))) {

                val levelIndex = viewModel.nextLevel()

                if (levelIndex == 2) {
//                    Log.d("DEBUG", "VSETKO")
//                    viewModel.nextStage()

                    prefs.edit().putBoolean("button2", true).apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    viewModel.resetLevel()
                }

                starView.rating = levelIndex
                viewModel.createGraph()
                graphView.redArrowPoints = viewModel.redArrowPoints
                graphView.blueArrowPoints = viewModel.blueArrowPoints
                Log.d("DEBUG", "${prefs.all}")

//                Log.d("GRAFIKA", "Red: ${graphView.redArrowPoints}")
//                Log.d("GRAFIKA", "Blue: ${graphView.blueArrowPoints}")
                Log.d("LOGIKA", "Red: ${viewModel.redArrowPoints}")
                Log.d("LOGIKA", "Blue: ${viewModel.blueArrowPoints}")

                // Create SpannableString
                val spannableString = SpannableString(viewModel.getCommand())

                // Set the spannable string to the TextView
                text.text = color(viewModel.getCommand(), spannableString)
//                text.text = viewModel.getCommand()
                graphView.invalidate()

//                if (prefs.getBoolean("button3", true)) {
//                    Log.d("STAGE:","Level 3")
//                    graphView.setNumVertices(4)
//                    viewModel.setNumberOfVerticesForGraph(4)
//                    viewModel.createGraph()
//                    graphView.redArrowPoints = viewModel.redArrowPoints
//                    graphView.blueArrowPoints = viewModel.blueArrowPoints
////
////                    currentIndex1 = 4
////                    currentIndex2 = 4
////                    currentIndex3 = 4
////                    currentIndex4 = 4
////
////                    square1.text = options[currentIndex1]
////                    square2.text = options[currentIndex2]
////                    square3.text = options[currentIndex3]
//////                    square4.text = options[currentIndex4]
////
////                    graphView.invalidate()
//                }
                currentIndex1 = 0
                currentIndex2 = 0
                currentIndex3 = 0
                currentIndex4 = 0

                square1.text = options[currentIndex1]
                square2.text = options[currentIndex2]
                square3.text = options[currentIndex3]
                square4?.text = options[currentIndex4]

            } else {
                Log.d("DEBUG", "NEPODARILO SA")
            }
        }
    }

    fun color(command: String, spannableString: SpannableString): SpannableString {
        // Apply color spans
        for (i in command.indices) {
            when (command[i]) {
                'M' -> spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue)),
                    i, i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                'Č' -> spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
                    i, i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }
}