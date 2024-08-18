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
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityLevelOneBinding
import com.example.projekt.databinding.ActivityMainBinding

class LevelOneActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityLevelOneBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var overlay: TextView

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

        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)

        val layout = intent.getStringExtra("layout")

        when (layout) {
            "layout1" -> setContentView(R.layout.activity_level_one)
            "layout2" -> setContentView(R.layout.activity_level_one_four_buttons)
            "layout3" -> setContentView(R.layout.activity_level_one_five_buttons)
        }

        overlay = findViewById(R.id.warningView)
        overlay.visibility = View.INVISIBLE

        val graphView = findViewById<GraphView>(R.id.graphView)
        val starView = findViewById<StarRatingView>(R.id.starView)

        val square1 = findViewById<Button>(R.id.button11)
        val square2 = findViewById<Button>(R.id.button12)
        val square3 = findViewById<Button>(R.id.button13)
        val square4 = findViewById<Button>(R.id.button14)
        val square5 = findViewById<Button>(R.id.button18)

        val text = findViewById<TextView>(R.id.textView)
        val verify = findViewById<Button>(R.id.button7)
        val lightbulb = findViewById<ImageView>(R.id.lightbulb)
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)

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

        square5?.setOnClickListener {
            if (currentIndex5 == viewModel.getNumberOfVerticesForGraph()) {
                currentIndex5 = -1
            }
            currentIndex5 += 1
            square5.text = options[currentIndex5]
        }

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        if (prefs.getBoolean("levelThree", true)) {
                graphView.setNumVertices(4)
                viewModel.setNumberOfVerticesForGraph(4)
        } else {
            if (prefs.getBoolean("levelFour", true)) {
                graphView.setNumVertices(5)
                viewModel.setNumberOfVerticesForGraph(5)
            } else {
                graphView.setNumVertices(3)
            }
        }

        viewModel.createGraph()
        graphView.redArrowPoints = viewModel.redArrowPoints
        graphView.blueArrowPoints = viewModel.blueArrowPoints

        val spannableString = SpannableString(viewModel.getCommand())
        text.text = color(viewModel.getCommand(), spannableString)

        buttonReturn.setOnClickListener{
            prefs.edit().putBoolean("levelThree", false).apply()
            prefs.edit().putBoolean("levelFour", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        lightbulb.setOnClickListener {
            verify.isEnabled = false

            graphView.startArrowAnimation(viewModel.getCommand(), 2000L, object :
                GraphView.AnimationCallback {
                override fun onAnimationStart() {
                }

                override fun onAnimationEnd() {
                    verify.isEnabled = true
                }
            })
        }

        overlay.setOnClickListener {
            lightbulb.isEnabled = true
            square1.isEnabled = true
            square2.isEnabled = true
            square3.isEnabled = true
            square4?.isEnabled = true
            square5?.isEnabled = true
            verify.isEnabled = true
            overlay.visibility = View.INVISIBLE
        }

        verify.setOnClickListener {
            if (viewModel.verify(listOf(currentIndex1,currentIndex2,currentIndex3,currentIndex4,currentIndex5))) {

                val levelIndex = viewModel.nextLevel()

                if (levelIndex == 10) {

                    if (prefs.getBoolean("button1", true)) {
                        prefs.edit().putBoolean("button2", true).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        viewModel.resetLevel()
                    }
                    if (prefs.getBoolean("button3", true)) {
                        prefs.edit().putBoolean("button4", true).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        viewModel.resetLevel()
                    }
                    if (prefs.getBoolean("button5", true)) {
                        prefs.edit().putBoolean("button6", true).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        viewModel.resetLevel()
                    }
                }

                starView.rating = levelIndex
                viewModel.createGraph()
                graphView.redArrowPoints = viewModel.redArrowPoints
                graphView.blueArrowPoints = viewModel.blueArrowPoints

                val spannableString = SpannableString(viewModel.getCommand())

                text.text = color(viewModel.getCommand(), spannableString)
                graphView.invalidate()

                currentIndex1 = 0
                currentIndex2 = 0
                currentIndex3 = 0
                currentIndex4 = 0
                currentIndex5 = 0

                square1.text = options[currentIndex1]
                square2.text = options[currentIndex2]
                square3.text = options[currentIndex3]
                square4?.text = options[currentIndex4]
                square5?.text = options[currentIndex5]

            } else {
                overlay.text = "Tvoja odpoveď je nesprávna!"
                lightbulb.isEnabled = false
                square1.isEnabled = false
                square2.isEnabled = false
                square3.isEnabled = false
                square4?.isEnabled = false
                square5?.isEnabled = false
                verify.isEnabled = false
                overlay.visibility = View.VISIBLE
            }
        }
    }

    fun color(command: String, spannableString: SpannableString): SpannableString {
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