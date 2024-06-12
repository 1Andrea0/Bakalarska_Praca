package com.example.projekt

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
//        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        prefs = getSharedPreferences("button_prefs", MODE_PRIVATE)

        updateButtonStates()
        prefs.edit().putBoolean("levelThree", false).apply()
        prefs.edit().putBoolean("levelFour", false).apply()

        // Save data to SharedPreferences


//        Log.d("SharedPreferences", "Button 1 value: ${prefs.getBoolean("button1", false)}")
//        Log.d("SharedPreferences", "Button 2 value: ${prefs.getBoolean("button2", false)}")
//        Log.d("SharedPreferences", "Button 3 value: ${prefs.getBoolean("button3", false)}")
//        Log.d("SharedPreferences", "Button 4 value: ${prefs.getBoolean("button4", false)}")

        binding.button1.setOnClickListener {
            val intent = Intent(this, LevelOneActivity::class.java)
            startActivity(intent)
        }

       binding.button2.setOnClickListener {
           if (prefs.getBoolean("button2", false)) {
               startActivity(Intent(this, LevelTwoActivity::class.java))
           }
       }

       binding.button3.setOnClickListener {
           if (prefs.getBoolean("button3", false)) {
               prefs.edit().putBoolean("levelThree", true).apply()
               startActivity(Intent(this, LevelOneActivity::class.java))
           }
       }

        binding.button4.setOnClickListener {
           if (prefs.getBoolean("button4", false)) {
               prefs.edit().putBoolean("levelFour", true).apply()
               startActivity(Intent(this, LevelTwoActivity::class.java))
           }
       }

        binding.button11.setOnClickListener {
//            val editor = prefs.edit()
//            editor.putBoolean("button1", true)
//            editor.putBoolean("button2", false)
//            editor.putBoolean("button3", false)
//            editor.putBoolean("button4", false)
//            editor.putBoolean("levelThree", false)
//            editor.putBoolean("levelFour", false)
//            editor.putBoolean("first", false)
//            editor.apply()
//            prefs.edit().clear().apply()
//            updateButtonStates()
            createStates()
        }
    }

    private fun createStates() {
        val editor = prefs.edit()
        editor.putBoolean("button1", true)
        editor.putBoolean("button2", false)
        editor.putBoolean("button3", false)
        editor.putBoolean("button4", false)
        editor.putBoolean("levelThree", false)
        editor.putBoolean("levelFour", false)
        editor.apply()
        updateButtonStates()
    }

    private fun updateButtonStates() {
        binding.button1.setBackgroundColor(Color.GREEN)
        binding.button1.isEnabled = prefs.getBoolean("button1", true)
        binding.button2.setBackgroundColor(if (prefs.getBoolean("button2", false)) Color.GREEN else Color.RED)
        binding.button2.isEnabled = prefs.getBoolean("button2", false)
        binding.button3.setBackgroundColor(if (prefs.getBoolean("button3", false)) Color.GREEN else Color.RED)
        binding.button3.isEnabled = prefs.getBoolean("button3", false)
        binding.button4.setBackgroundColor(if (prefs.getBoolean("button4", false)) Color.GREEN else Color.RED)
        binding.button4.isEnabled = prefs.getBoolean("button4", false)
//        prefs.getBoolean("levelThree", false)
//        prefs.getBoolean("levelFour", false)
//        Log.d("DEBUG", "${prefs.all}")
    }
}