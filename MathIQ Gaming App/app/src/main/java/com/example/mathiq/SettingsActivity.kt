package com.example.mathiq

// SettingsActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var playMusicButton: ImageButton
    private lateinit var pauseMusicButton: ImageButton
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        playMusicButton = findViewById(R.id.playMusicButton)
        pauseMusicButton = findViewById(R.id.pauseMusicButton)
        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        backButton = findViewById(R.id.closeButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        playMusicButton.setOnClickListener {
            MediaPlayerManager.getMediaPlayer()?.let { mediaPlayer ->
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
        }

        pauseMusicButton.setOnClickListener {
            MediaPlayerManager.getMediaPlayer()?.let { mediaPlayer ->
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
        }

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                MediaPlayerManager.getMediaPlayer()?.let { mediaPlayer ->
                    val volume = progress / 100.0f
                    mediaPlayer.setVolume(volume, volume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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