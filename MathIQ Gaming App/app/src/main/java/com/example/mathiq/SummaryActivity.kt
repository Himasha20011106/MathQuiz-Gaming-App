package com.example.mathiq

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var userAnswerTextView: TextView
    private lateinit var correctAnswerTextView: TextView
    private lateinit var timeSpentTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // Initialize TextViews
        scoreTextView = findViewById(R.id.scoreTextView)
        questionTextView = findViewById(R.id.questionTextView)
        userAnswerTextView = findViewById(R.id.userAnswersTextView)
        correctAnswerTextView = findViewById(R.id.correctAnswersTextView)
        timeSpentTextView = findViewById(R.id.timeSpentTextView)

        // Retrieve data from intent extras
        val score = intent.getIntExtra("SCORE", 0)
        val userAnswers = intent.getIntegerArrayListExtra("USER_ANSWERS")
        val correctAnswers = intent.getIntegerArrayListExtra("CORRECT_ANSWERS")

        // Log intent extras for debugging
        Log.d("SummaryActivity", "Score: $score")
        Log.d("SummaryActivity", "User Answers: $userAnswers")
        Log.d("SummaryActivity", "Correct Answers: $correctAnswers")

        // Display score
        scoreTextView.text = "Your Score: $score"

        // Display question summaries if data is valid
        if (userAnswers != null && correctAnswers != null && userAnswers.size == correctAnswers.size) {
            displayQuestionSummaries(userAnswers, correctAnswers)
        } else {
            // Handle invalid or missing data
            questionTextView.text = "Data Error: Unable to display question summaries"
        }
    }

    private fun displayQuestionSummaries(userAnswers: ArrayList<Int>, correctAnswers: ArrayList<Int>) {
        val numQuestions = userAnswers.size
        val summaryBuilder = StringBuilder()

        for (i in 0 until numQuestions) {
            val questionNumber = i + 1
            val userAnswer = getOptionText(userAnswers[i])
            val correctAnswer = getOptionText(correctAnswers[i])

            // Prepare summary text for each question
            val questionSummary = "Question $questionNumber:\n" +
                    "User's Answer: $userAnswer\n" +
                    "Correct Answer: $correctAnswer\n\n"

            summaryBuilder.append(questionSummary)
        }

        // Display question summaries in TextView
        questionTextView.text = summaryBuilder.toString()
    }

    private fun getOptionText(optionIndex: Int): String {
        val options = listOf("A", "B", "C", "D") // Example options (customize as needed)
        return if (optionIndex in 0 until options.size) options[optionIndex] else "N/A"
    }
}
