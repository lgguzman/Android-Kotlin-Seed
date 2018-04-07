package com.lgguzman.example.kotlinseed.ui.activities.onboarding

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.services.UserService
import com.lgguzman.example.kotlinseed.ui.activities.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by lgguzman on 27/03/18.
 */
class SplashActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashActivity,
                if (!UserService.isLogging()) OnboardingActivity::class.java else MainActivity::class.java))
        finish()
    }
}