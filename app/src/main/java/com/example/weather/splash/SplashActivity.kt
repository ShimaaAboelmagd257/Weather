package com.example.weather.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.weathear.R
import com.example.weather.weatherMain.MainActivity


class SplashActivity : AppCompatActivity() {

    private  lateinit  var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        setContentView(R.layout.activity_splash)

        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()

        lottieAnimationView = findViewById(R.id.animationViewhome)
        lottieAnimationView.setAnimation("earthmovig.json") // Set the JSON file
        lottieAnimationView.playAnimation() // Start the animation

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 6000)
    }
}