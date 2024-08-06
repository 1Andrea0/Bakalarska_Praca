package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityLevelOneBinding

class LevelOneActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityLevelOneBinding
    private lateinit var prefs: SharedPreferences

    private val options = mutableListOf("A", "B", "C", "")
    private var currentIndex1 = 3
    private var currentIndex2 = 3
    private var currentIndex3 = 3
    private var currentIndex4 = 4
    private var currentIndex5 = 5

    private var buttonAdded = false
    private var buttonFour = false
    private var buttonFive = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)
        var clickCount = 0

        val square1 = findViewById<Button>(R.id.button11)
        val square2 = findViewById<Button>(R.id.button12)
        val square3 = findViewById<Button>(R.id.button13)

        val text = findViewById<TextView>(R.id.textView)
        val verify = findViewById<Button>(R.id.button7)

        val graphView = findViewById<GraphView>(R.id.graphView)
        val starView = findViewById<StarRatingView>(R.id.starView)

        square1.text = options[currentIndex1]
        square2.text = options[currentIndex2]
        square3.text = options[currentIndex3]

        square1.setOnClickListener {
            currentIndex1 = (currentIndex1 + 1) % options.size
            square1.text = options[currentIndex1]
        }

        square2.setOnClickListener {
            currentIndex2 = (currentIndex2 + 1) % options.size
            square2.text = options[currentIndex2]
        }

        square3.setOnClickListener {
            currentIndex3 = (currentIndex3 + 1) % options.size
            square3.text = options[currentIndex3]
        }

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        text.text = viewModel.getCommand()
        graphView.setNumVertices(3)

//        if (prefs.getBoolean("levelThree", false) && (prefs.getBoolean("levelOne", true))) {
//            addFourthButton()
//            options.add(3,"D")
////            viewModel.addFour()
//            graphView.redArrowPoints = viewModel.redArrowPoints
//            graphView.blueArrowPoints = viewModel.blueArrowPoints
//            graphView.setNumVertices(4)
//            Log.d("DEBUG", "$clickCount")
//            if (clickCount > 5) {
//                addFourthButton()
//                options.add(4,"E")
////                viewModel.addFive()
//                graphView.redArrowPoints = viewModel.redArrowPoints
//                graphView.blueArrowPoints = viewModel.blueArrowPoints
//                graphView.setNumVertices(5)
//            }
//        }

        binding.button6.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.lightbulb.setOnClickListener {
            graphView.startArrowAnimation(viewModel.getCommand(), 2000L)
//            graphView.bitmapPositions.clear()
        }

        verify.setOnClickListener {
            // Set the starting and ending coordinates here
//            Toast.makeText(this, "NESPRÁVNE. SKÚS ZNOVU", Toast.LENGTH_LONG).show()
            if (viewModel.verify(listOf(currentIndex1,currentIndex2,currentIndex3))) {
//            Log.d("DEBUG", "PODARILO SA")
//                Log.d("DEBUG", "$currentIndex1")
//                Log.d("DEBUG", "$currentIndex2")
//                Log.d("DEBUG", "$currentIndex3")


                val levelIndex = viewModel.nextLevel()
                clickCount = levelIndex
//                Log.d("DEBUG", "$levelIndex")

                if (levelIndex == 4) {
//                    Log.d("DEBUG", "VSETKO")
//                    viewModel.nextStage()

                    prefs.edit().putBoolean("button2", true).apply()
//                    Log.d("DEBUG", "${prefs.all}")

                    startActivity(Intent(this, MainActivity::class.java))
                    viewModel.resetLevel()
                }

                starView.rating = levelIndex
                viewModel.createGraph()
                graphView.redArrowPoints = viewModel.redArrowPoints
                graphView.blueArrowPoints = viewModel.blueArrowPoints

//                Log.d("GRAFIKA", "Red: ${graphView.redArrowPoints}")
//                Log.d("GRAFIKA", "Blue: ${graphView.blueArrowPoints}")
                Log.d("LOGIKA", "Red: ${viewModel.redArrowPoints}")
                Log.d("LOGIKA", "Blue: ${viewModel.blueArrowPoints}")
                text.text = viewModel.getCommand()
                graphView.invalidate()

//                if (prefs.getBoolean("button3", false)) {
//                    currentIndex1 = 4
//                    currentIndex2 = 4
//                    currentIndex3 = 4
//                    currentIndex4 = 4
//
//                    square1.text = options[currentIndex1]
//                    square2.text = options[currentIndex2]
//                    square3.text = options[currentIndex3]
////                    square4.text = options[currentIndex4]
//
//                    graphView.invalidate()
//                }
                currentIndex1 = 3
                currentIndex2 = 3
                currentIndex3 = 3

                square1.text = options[currentIndex1]
                square2.text = options[currentIndex2]
                square3.text = options[currentIndex3]
//                graphView.invalidate()

            } else {
//                Toast.makeText(this, "NESPRÁVNE. SKÚS ZNOVU", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "NESPRÁVNE. SKÚS ZNOVU", Toast.LENGTH_SHORT).show()
                Log.d("DEBUG", "NEPODARILO SA")
            }
        }
    }

    private fun addFourthButton() {
        val newButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 3
                marginEnd = 3
            }
            isEnabled = false
            text = "D"
            id = View.generateViewId()
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
        }

        val newButton2 = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 3
                marginEnd = 3
            }
            text = ""
            id = View.generateViewId()
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
//            background = null
            setPadding(8, 0, 8, 0) // Adjust the padding as needed

            setBackgroundColor(ContextCompat.getColor(this@LevelOneActivity, R.color.purple_500))
            setTextColor(Color.WHITE)
            setOnClickListener {
                currentIndex4 = (currentIndex4+1)%options.size
                text = options[currentIndex4]
            }
        }
//        binding.buttonContainer.addView(newButton)
//        binding.buttonContainer2.addView(newButton2)
        buttonAdded = true
    }
}