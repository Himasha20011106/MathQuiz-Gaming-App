package com.example.mathiq

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var gamelayout: LinearLayout
    private lateinit var dashboard: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var timerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private var currentQuestionNumber: Int = 1
    private lateinit var questionNumberTextView: Button
    private lateinit var backButton: Button
    private lateinit var settingsView: View

    private val random = Random()
    private var score = 0
    private lateinit var currentQuestion: Questions
    private var timer: CountDownTimer? = null
    private var totalGameTime: Long = 0
    private var gameStartTime: Long = 0
    private var currentLevel: Int = 1

    private lateinit var scoreTextView: TextView
    private lateinit var title: TextView
    private val correctColor: Int by lazy { ContextCompat.getColor(this, R.color.correctColor) }
    private val incorrectColor: Int by lazy { ContextCompat.getColor(this, R.color.incorrectColor) }

    private lateinit var sharedPreferences: SharedPreferences
    private val SCORES_KEY = "HIGH_SCORES"

    private var consecutiveCorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backButton = findViewById(R.id.back_button)
        settingsView = findViewById(R.id.settingsView)

        backButton.setOnClickListener {
            startHomeActivity()
        }

        settingsView.setOnClickListener {
            // Create an Intent to navigate to the SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)

            // Start the MainActivity
            startActivity(intent)

            // Finish the current activity to prevent navigating back to it
            finish()
        }

        sharedPreferences = getSharedPreferences("MathScorePrefs", Context.MODE_PRIVATE)
        score = sharedPreferences.getStringSet(SCORES_KEY, mutableSetOf())?.firstOrNull()?.toIntOrNull() ?: 0

        startButton = findViewById(R.id.start_button)
        gamelayout = findViewById(R.id.gamelayout)
        dashboard = findViewById(R.id.dashboard)
        progressBar = findViewById(R.id.progressBar)
        timerTextView = findViewById(R.id.timerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        title = findViewById(R.id.title)
        questionNumberTextView = findViewById(R.id.questionNumberTextView)

        gamelayout.visibility = View.GONE

        startButton.setOnClickListener {
            startGame()
        }

        questionTextView = findViewById(R.id.questionTextView)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)
        timerTextView = findViewById(R.id.timerTextView)
        progressBar = findViewById(R.id.progressBar)

        option1Button.setOnClickListener { onOptionSelected(it) }
        option2Button.setOnClickListener { onOptionSelected(it) }
        option3Button.setOnClickListener { onOptionSelected(it) }
        option4Button.setOnClickListener { onOptionSelected(it) }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startGame() {
        option1Button.setBackgroundResource(R.drawable.button_backgound)
        option2Button.setBackgroundResource(R.drawable.button_backgound)
        option3Button.setBackgroundResource(R.drawable.button_backgound)
        option4Button.setBackgroundResource(R.drawable.button_backgound)

        dashboard.visibility = View.GONE
        gamelayout.visibility = View.VISIBLE
        score = 0
        totalGameTime = 0
        gameStartTime = System.currentTimeMillis()
        currentLevel = 1
        showNextQuestion()
        updateScoreDisplay()
        updateLevelText()
        startTotalGameTimer()
        consecutiveCorrectAnswers = 0
    }

    private fun showNextQuestion() {
        currentQuestion = generateRandomQuestion()
        updateQuestionUI()
        startQuestionTimer()
        questionNumberTextView.text = "Question $currentQuestionNumber/10"
    }

    private fun updateQuestionUI() {
        questionTextView.text = currentQuestion.question
        val options = currentQuestion.options
        option1Button.text = options[0]
        option2Button.text = options[1]
        option3Button.text = options[2]
        option4Button.text = options[3]
    }

    private fun startQuestionTimer() {
        val questionTime = if (currentLevel == 1) 10000L else 15000L
        timer?.cancel()
        timer = object : CountDownTimer(questionTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((questionTime - millisUntilFinished) * 100 / questionTime).toInt()
                progressBar.progress = progress
                timerTextView.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                progressBar.progress = 100
                timerTextView.text = "Time's up!"
                endGame()
            }
        }.start()
    }

    private fun generateRandomQuestion(): Questions {
        val num1 = random.nextInt(100)
        val num2 = random.nextInt(100)
        val operation = random.nextInt(4)
        val correctAnswer: Int
        val question: String

        if (currentLevel == 1) {
            when (operation) {
                0 -> { // Addition
                    correctAnswer = num1 + num2
                    question = "$num1 + $num2?"
                }
                1 -> { // Subtraction
                    correctAnswer = num1 - num2
                    question = "$num1 - $num2?"
                }
                2 -> { // Multiplication
                    correctAnswer = num1 * num2
                    question = "$num1 * $num2?"
                }
                else -> { // Division
                    correctAnswer = num1 / (if (num2 != 0) num2 else 1)
                    question = "$num1 ÷ $num2?"
                }
            }
        } else { // Level 2
            val num3 = random.nextInt(100)
            when (operation) {
                0 -> { // Addition
                    correctAnswer = num1 + num2 + num3
                    question = "$num1 + $num2 + $num3?"
                }
                1 -> { // Subtraction
                    correctAnswer = num1 - num2 - num3
                    question = "$num1 - $num2 - $num3?"
                }
                2 -> { // Multiplication
                    correctAnswer = num1 * num2 * num3
                    question = "$num1 * $num2 * $num3?"
                }
                else -> { // Division
                    correctAnswer = num1 / (if (num2 != 0) num2 else 1) / (if (num3 != 0) num3 else 1)
                    question = "$num1 ÷ $num2 ÷ $num3?"
                }
            }
        }

        val options = generateOptions(correctAnswer)

        return Questions(question, options, options.indexOf(correctAnswer.toString()))
    }

    private fun generateOptions(correctAnswer: Int): List<String> {
        val options = mutableListOf<String>()
        options.add(correctAnswer.toString())

        while (options.size < 4) {
            val wrongAnswer = correctAnswer + random.nextInt(20) - 10
            if (wrongAnswer != correctAnswer && !options.contains(wrongAnswer.toString())) {
                options.add(wrongAnswer.toString())
            }
        }

        return options.shuffled()
    }

    private fun endGame() {
        totalGameTime += System.currentTimeMillis() - gameStartTime
        gameStartTime = System.currentTimeMillis()

        if (totalGameTime >= 30000) {
            Toast.makeText(this, "You have successfully played for 30 seconds. Congratulations!", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                dashboard.visibility = View.VISIBLE
                gamelayout.visibility = View.GONE
            }, 1000)
            return
        }

        Toast.makeText(this, "Game Over! Your score: $score", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            dashboard.visibility = View.VISIBLE
            gamelayout.visibility = View.GONE
        }, 1000)

        timer?.cancel()
        val gameOverText = "Game Over! \n"
        val scoreText = "Your Score: $score"

        val spannable = SpannableString(gameOverText + scoreText)

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.incorrectColor)),
            0,
            gameOverText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.correctColor)),
            gameOverText.length,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        title.text = spannable

        saveHighScore(score)
    }

    private fun onOptionSelected(view: View) {
        val selectedOption = when (view.id) {
            R.id.option1Button -> 0
            R.id.option2Button -> 1
            R.id.option3Button -> 2
            R.id.option4Button -> 3
            else -> -1
        }

        if (selectedOption == currentQuestion.correctOption) {
            score += 10
            updateScoreDisplay()
            view.setBackgroundColor(correctColor)
            Toast.makeText(this, "Correct! You scored 10", Toast.LENGTH_SHORT).show()
            consecutiveCorrectAnswers++ // Increment consecutive correct answers
            if (consecutiveCorrectAnswers == 5 && currentLevel == 1) { // Check if 5 consecutive correct answers are reached
                showLevelUpMessage() // Show level up message
                return
            }
        } else {
            view.setBackgroundColor(incorrectColor)
            Toast.makeText(this, "Wrong! Game Over.", Toast.LENGTH_SHORT).show()
            endGame()
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBackgroundResource(R.drawable.button_backgound)
            showNextQuestion()
        }, 1000)
    }

    private fun updateScoreDisplay() {
        scoreTextView.text = "Score: $score"
    }

    private fun saveHighScore(score: Int) {
        val scoresSet = sharedPreferences.getStringSet(SCORES_KEY, mutableSetOf()) ?: mutableSetOf()
        scoresSet.add(score.toString())
        val top3Scores = scoresSet.mapNotNull { it.toIntOrNull() }.sortedDescending().take(3).toSet()
        sharedPreferences.edit().putStringSet(SCORES_KEY, top3Scores.map { it.toString() }.toSet()).apply()
    }

    private fun startTotalGameTimer() {
        object : CountDownTimer(30000 - totalGameTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalGameTime += 1000
            }

            override fun onFinish() {

            }
        }.start()
    }

    private fun showLevelUpMessage() {
        Toast.makeText(this, "Congratulations! You've reached Level 2.", Toast.LENGTH_SHORT).show()
        startLevel2()
    }

    private fun startLevel2() {
        currentLevel = 2
        totalGameTime = 0
        showNextQuestion()
        updateLevelText()
        consecutiveCorrectAnswers = 0
    }

    private fun updateLevelText() {
        val levelTextView: TextView = findViewById(R.id.level)
        if (currentLevel == 1) {
            levelTextView.text = "Level 1"
        } else {
            levelTextView.text = "Level 2"
        }
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
