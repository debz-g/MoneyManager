package dev.refox.moneymanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import dev.redfox.moneymanager.auth.LoginActivity
import dev.refox.moneymanager.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setStatusBarColor(this.getResources().getColor(R.color.white))

        Handler().postDelayed({

            val sharedPreference = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
            val hasLoggedIn: Boolean = sharedPreference.getBoolean("hasLoggedIn", false)

            if (hasLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }, 2000)

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
    }
}