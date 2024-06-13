package com.example.mathiq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        // Delayed execution of navigation to another activity after 3 seconds
        Handler().postDelayed({
            // Start the new activity
            startActivity(Intent(this, HomeActivity::class.java))

            // Close this activity
            finish()
        }, 1000)
    }
}