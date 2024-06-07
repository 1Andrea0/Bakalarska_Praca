package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityLevelTwoBinding

class LevelTwoActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
//    lateinit var graphView: GraphView
    private lateinit var binding: ActivityLevelTwoBinding
    private lateinit var prefs: SharedPreferences

    private val options = listOf("A", "B", "C")
    private var currentIndex1 = 3
    private var currentIndex2 = 3
    private var currentIndex3 = 3

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)

        val square1 = findViewById<Button>(R.id.button11)
        val square2 = findViewById<Button>(R.id.button12)
        val square3 = findViewById<Button>(R.id.button13)

        val verify = findViewById<Button>(R.id.button7)

        val graphView = findViewById<GraphView>(R.id.graphView)
        val starView = findViewById<StarRatingView>(R.id.starView)

        var clickCount = 0

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
//        graphView = GraphView(this)

        var result = viewModel.verify2()

        currentIndex1 = result[0].second
        currentIndex2 = result[1].second
        currentIndex3 = result[2].second

        square1.text = options[currentIndex1]
        square2.text = options[currentIndex2]
        square3.text = options[currentIndex3]

//        text.text = viewModel.getCommand()

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = ""
        val buttonBlue = findViewById<Button>(R.id.button)
        val buttonRed = findViewById<Button>(R.id.button5)

        buttonBlue.setOnClickListener {
            if (clickCount<10)
            textView.text = textView.text.toString() + "M"
            clickCount++
        }

        buttonRed.setOnClickListener {
            if (clickCount<10)
            textView.text = textView.text.toString() + "Č"
            clickCount++
        }

//        val commands = listOf("Č", "M", "ČM", "MČ", "MM", "ČM", "MČ", "ČČ", "ČMM", "MČM")
//        val commands2 = listOf("M", "Č", "MČ", "ČM", "ČČ", "MM", "ČM", "MČ", "MČM", "MČČ")
        val idea = textView.text.toString().toList()

        Log.d("DEBUG", "$currentIndex1,$currentIndex2,$currentIndex3,$idea")

        binding.button6.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        verify.setOnClickListener {
            if (viewModel.verify3(currentIndex1,currentIndex2,currentIndex3,textView.text.toString().toList())) {
//                val levelIndex = viewModel.nextLevel()
//                Log.d("DEBUG", "PODARILO SA,$levelIndex")

                val levelIndex = viewModel.nextLevel()
                clickCount = 0
                if (levelIndex == 2) {
                    Log.d("DEBUG", "VSETKO")
                    viewModel.nextStage()

                    prefs.edit().putBoolean("button3", true).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    viewModel.resetLevel()
                }

                starView.rating = levelIndex
                viewModel.arrows()
                graphView.redArrowPoints = viewModel.redArrowPoints
                graphView.blueArrowPoints = viewModel.blueArrowPoints

                result = viewModel.verify2()

                currentIndex1 = result[0].second
                currentIndex2 = result[1].second
                currentIndex3 = result[2].second

                square1.text = options[currentIndex1]
                square2.text = options[currentIndex2]
                square3.text = options[currentIndex3]

                textView.text = ""
                graphView.invalidate()

            } else {
                Log.d("DEBUG", "NEPODARILO SA")
            }


        }
    }

}