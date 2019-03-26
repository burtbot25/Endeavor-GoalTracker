package com.comp3617.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
