package com.example.simpleapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private var counter = 0
    private lateinit var counterTextView: TextView
    private lateinit var clickButton: Button
    private lateinit var resetButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        counterTextView = findViewById(R.id.counterTextView)
        clickButton = findViewById(R.id.clickButton)
        resetButton = findViewById(R.id.resetButton)
        
        // Update counter display
        updateCounterDisplay()
        
        // Set click listener for increment button
        clickButton.setOnClickListener {
            counter++
            updateCounterDisplay()
        }
        
        // Set click listener for reset button
        resetButton.setOnClickListener {
            counter = 0
            updateCounterDisplay()
        }
    }
    
    private fun updateCounterDisplay() {
        counterTextView.text = getString(R.string.counter_text, counter)
    }
}
