package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            createStates()
        }
    }

    private fun createStates() {
        val editor = prefs.edit()
        editor.putBoolean("button1", true)
        editor.putBoolean("button2", true)
        editor.putBoolean("button3", true)
        editor.putBoolean("button4", true)
        editor.putBoolean("button5", true)
        editor.putBoolean("button6", false)
        editor.putBoolean("levelThree", false)
        editor.putBoolean("levelFour", true)
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
}