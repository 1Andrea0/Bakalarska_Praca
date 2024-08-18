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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityLevelTwoBinding

class LevelTwoActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityLevelTwoBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var overlay: TextView

    private val options = mutableListOf("A", "B", "C", "D", "E")
    private var currentIndex1 = 0
    private var currentIndex2 = 0
    private var currentIndex3 = 0
    private var currentIndex4 = 0
    private var currentIndex5 = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)

        val layout = intent.getStringExtra("layout")

        when (layout) {
            "layout1" -> setContentView(R.layout.activity_level_two)
            "layout2" -> setContentView(R.layout.activity_level_two_four_buttons)
            "layout3" -> setContentView(R.layout.activity_level_two_five_buttons)
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

        val textView = findViewById<TextView>(R.id.textView)
        val verify = findViewById<Button>(R.id.button7)
        val lightbulb = findViewById<ImageView>(R.id.lightbulb)
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)
        val redX = findViewById<ImageView>(R.id.red_x)

        var clickCount = 0

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
        viewModel.createCommands()
        graphView.redArrowPoints = viewModel.redArrowPoints
        graphView.blueArrowPoints = viewModel.blueArrowPoints

        var result = viewModel.verify()

        currentIndex1 = result[0].second
        currentIndex2 = result[1].second
        currentIndex3 = result[2].second
        currentIndex4 = if (result.size > 3) result[3].second else 0
        currentIndex5 = if (result.size > 4) result[4].second else 0

        square1.text = options[currentIndex1]
        square2.text = options[currentIndex2]
        square3.text = options[currentIndex3]
        square4?.text = options[currentIndex4]
        square5?.text = options[currentIndex5]

        textView.text = ""

        val buttonBlue = findViewById<Button>(R.id.button)
        val buttonRed = findViewById<Button>(R.id.button5)

        var spannableString: SpannableString

        buttonBlue.setOnClickListener {
            if (clickCount < 10) {
                textView.text = textView.text.toString() + "M"
                spannableString = SpannableString(textView.text)
                textView.text = color(spannableString)
                clickCount++
            }
        }

        buttonRed.setOnClickListener {
            if (clickCount < 10) {
                textView.text = textView.text.toString() + "Č"
                spannableString = SpannableString(textView.text)
                textView.text = color(spannableString)
                clickCount++
            }
        }

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

        redX.setOnClickListener {
            textView.text = ""
            clickCount = 0
        }

        overlay.setOnClickListener {
            lightbulb.isEnabled = true
            verify.isEnabled = true
            overlay.visibility = View.INVISIBLE
        }

        viewModel.currentLevel = prefs.getInt("stars",0)
        starView.rating = viewModel.currentLevel

        verify.setOnClickListener {
            if (viewModel.verify(listOf(currentIndex1,currentIndex2,currentIndex3,currentIndex4,currentIndex5),textView.text.toString().toList())) {

                prefs.edit().putInt("stars",viewModel.nextLevel()).apply()
                clickCount = 0

                if (prefs.getInt("stars",0) == 10) {
                    prefs.edit().putInt("stars",0).apply()

                    if (prefs.getBoolean("button2", true)) {
                        prefs.edit().putBoolean("button3", true).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        viewModel.resetLevel()
                    }
                    if (prefs.getBoolean("button4", true)) {
                        prefs.edit().putBoolean("button5", true).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        viewModel.resetLevel()
                    }
                }

                starView.rating = viewModel.currentLevel
                viewModel.createGraph()
                viewModel.createCommands()
                graphView.redArrowPoints = viewModel.redArrowPoints
                graphView.blueArrowPoints = viewModel.blueArrowPoints

                result = viewModel.verify()

                currentIndex1 = result[0].second
                currentIndex2 = result[1].second
                currentIndex3 = result[2].second
                currentIndex4 = if (result.size > 3) result[3].second else 0
                currentIndex5 = if (result.size > 4) result[4].second else 0

                square1.text = options[currentIndex1]
                square2.text = options[currentIndex2]
                square3.text = options[currentIndex3]
                square4?.text = options[currentIndex4]
                square5?.text = options[currentIndex5]

                textView.text = ""
                graphView.invalidate()

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

    fun color(spannableString: SpannableString): SpannableString {
        val command = spannableString.toString()
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