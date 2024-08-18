package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.projekt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)

        binding.warningView.visibility = View.INVISIBLE
        viewModel.currentLevel(prefs.getInt("stars",0))

        updateButtonStates()

        binding.button1.setOnClickListener {
            prefs.edit().putBoolean("levelThree", false).apply()
            prefs.edit().putBoolean("levelFour", false).apply()
            val intent = Intent(this, LevelOneActivity::class.java)
            intent.putExtra("layout", "layout1")
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            prefs.edit().putBoolean("levelThree", false).apply()
            prefs.edit().putBoolean("levelFour", false).apply()
            if (prefs.getBoolean("button2", false)) {
                prefs.edit().putBoolean("button2", true).apply()
                val intent = Intent(this, LevelTwoActivity::class.java)
                intent.putExtra("layout", "layout1")
                startActivity(intent)
            }
        }

        binding.button3.setOnClickListener {
            prefs.edit().putBoolean("levelThree", true).apply()
            prefs.edit().putBoolean("levelFour", false).apply()
            if (prefs.getBoolean("button3", false)) {
                prefs.edit().putBoolean("button3", true).apply()
                val intent = Intent(this, LevelOneActivity::class.java)
                intent.putExtra("layout", "layout2")
                startActivity(intent)
            }
        }

        binding.button4.setOnClickListener {
            prefs.edit().putBoolean("levelThree", true).apply()
            prefs.edit().putBoolean("levelFour", false).apply()
            if (prefs.getBoolean("button4", false)) {
                prefs.edit().putBoolean("button4", true).apply()
                val intent = Intent(this, LevelTwoActivity::class.java)
                intent.putExtra("layout", "layout2")
                startActivity(intent)
            }
        }

        binding.button5.setOnClickListener {
            prefs.edit().putBoolean("levelThree", false).apply()
            prefs.edit().putBoolean("levelFour", true).apply()
            if (prefs.getBoolean("button5", false)) {
                prefs.edit().putBoolean("button5", true).apply()
                val intent = Intent(this, LevelOneActivity::class.java)
                intent.putExtra("layout", "layout3")
                startActivity(intent)
            }
        }

        binding.button6.setOnClickListener {
            prefs.edit().putBoolean("levelThree", false).apply()
            prefs.edit().putBoolean("levelFour", true).apply()
            if (prefs.getBoolean("button6", false)) {
                prefs.edit().putBoolean("button6", true).apply()
                val intent = Intent(this, LevelTwoActivity::class.java)
                intent.putExtra("layout", "layout3")
                startActivity(intent)
            }
        }

        binding.button11.setOnClickListener {
            binding.warningView.visibility = View.VISIBLE
            binding.warningView.text = "Tvoje odpovede boli vymazané!"
            createStates()
            viewModel.currentLevel(0)
            prefs.edit().putInt("stars",0).apply()
        }

        binding.button12.setOnClickListener {
            binding.warningView.visibility = View.VISIBLE
            binding.warningView.text = "Všetky levely sú odomknuté. Pre zamknutie klikni na Vymazať."
            updateButtonStates2()
        }

        binding.warningView.setOnClickListener {
            binding.warningView.visibility = View.INVISIBLE
        }
    }

    private fun createStates() {
        val editor = prefs.edit()
        editor.putBoolean("button1", true)
        editor.putBoolean("button2", false)
        editor.putBoolean("button3", false)
        editor.putBoolean("button4", false)
        editor.putBoolean("button5", false)
        editor.putBoolean("button6", false)
        editor.putBoolean("levelThree", false)
        editor.putBoolean("levelFour", false)
        editor.apply()
        updateButtonStates()
    }

    private fun updateButtonStates() {
        binding.button1.isEnabled = prefs.getBoolean("button1", true)
        binding.button2.isEnabled = prefs.getBoolean("button2", false)
        binding.button3.isEnabled = prefs.getBoolean("button3", false)
        binding.button4.isEnabled = prefs.getBoolean("button4", false)
        binding.button5.isEnabled = prefs.getBoolean("button5", false)
        binding.button6.isEnabled = prefs.getBoolean("button6", false)
    }

    private fun updateButtonStates2() {
        val editor = prefs.edit()
        editor.putBoolean("button1", true)
        editor.putBoolean("button2", true)
        editor.putBoolean("button3", true)
        editor.putBoolean("button4", true)
        editor.putBoolean("button5", true)
        editor.putBoolean("button6", true)
        editor.putBoolean("levelThree", false)
        editor.putBoolean("levelFour", false)
        editor.apply()
        binding.button1.isEnabled = prefs.getBoolean("button1", true)
        binding.button2.isEnabled = prefs.getBoolean("button2", true)
        binding.button3.isEnabled = prefs.getBoolean("button3", true)
        binding.button4.isEnabled = prefs.getBoolean("button4", true)
        binding.button5.isEnabled = prefs.getBoolean("button5", true)
        binding.button6.isEnabled = prefs.getBoolean("button6", true)
    }
}