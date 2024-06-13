package com.example.mathiq

// HomeActivity.kt
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var gameButton: Button
    private lateinit var scoreButton: Button
    private lateinit var exitButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        MediaPlayerManager.initializeMediaPlayer(mediaPlayer)

        gameButton = findViewById(R.id.gameButton)
        scoreButton = findViewById(R.id.scoreButton)
        exitButton = findViewById(R.id.exitButton)
        settingsButton = findViewById(R.id.settingsButton)

        gameButton.setOnClickListener {
            startMainActivity()
        }

        scoreButton.setOnClickListener {
            startHighScoresActivity()
        }

        exitButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        settingsButton.setOnClickListener {
            startSettingsActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startHighScoresActivity() {
        val intent = Intent(this, HighScoresActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Exit") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SETTINGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SETTINGS) {
            // Handle result from SettingsActivity if needed
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerManager.releaseMediaPlayer()
    }

    companion object {
        private const val REQUEST_CODE_SETTINGS = 1
    }

}
