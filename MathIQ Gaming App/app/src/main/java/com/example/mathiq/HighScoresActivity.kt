package com.example.mathiq

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HighScoresActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val SCORES_KEY = "HIGH_SCORES"

    private lateinit var backButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        backButton = findViewById(R.id.backButton)
        clearButton = findViewById(R.id.clearButton)

        backButton.setOnClickListener {
            // Create an Intent to navigate to the MainActivity
            val intent = Intent(this, HomeActivity::class.java)

            // Start the MainActivity
            startActivity(intent)

            // Finish the current activity to prevent navigating back to it
            finish()
        }

        clearButton.setOnClickListener {
            clearHighScores()
        }

        sharedPreferences = getSharedPreferences("MathScorePrefs", Context.MODE_PRIVATE)

        // Get the highest scores and display them
        val highScoresTextView: TextView = findViewById(R.id.highScoresTextView)
        val highScoresText = getHighScoresText()
        val hasProgress = checkProgress()
        if (hasProgress) {
            highScoresTextView.text = "$highScoresText\nCongratulations! You've made progress!"
        } else {
            highScoresTextView.text = highScoresText
        }
    }

    private fun getHighScoresText(): String {
        val scores = sharedPreferences.getStringSet(SCORES_KEY, setOf()) ?: setOf()
        val sortedScores = scores.map { it.toInt() }.sortedDescending().take(3)
        val formattedScores = sortedScores.mapIndexed { index, score ->
            "${index + 1}) $score"
        }
        return formattedScores.joinToString("\n")
    }

    private fun checkProgress(): Boolean {
        val scores = sharedPreferences.getStringSet(SCORES_KEY, setOf()) ?: setOf()
        return scores.any { it.toInt() > 0 }
    }

    private fun clearHighScores() {
        sharedPreferences.edit().remove(SCORES_KEY).apply()
        // After clearing, you may want to update the displayed scores
        val highScoresTextView: TextView = findViewById(R.id.highScoresTextView)
        highScoresTextView.text = ""
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Exit") { dialog, which ->
                finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
