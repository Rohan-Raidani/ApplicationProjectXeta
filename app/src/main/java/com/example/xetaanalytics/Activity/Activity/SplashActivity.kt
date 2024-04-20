package com.example.xetaanalytics.Activity.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.xetaanalytics.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        // Call API to fetch data
        fetchDataFromApi()
    }

    private fun fetchDataFromApi() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://52.25.229.242:8000/homepage_v2/"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Check if data fetched successfully
                val success = response.getBoolean("success")
                if (success) {
                    Log.d("SplashActivity", "Data fetched successfully")
                    // Data fetched successfully, proceed to MainActivity after a delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        startMainActivity()
                    }, 1000) // Delay of 1 seconds (2000 milliseconds)
                } else {
                    Log.e("SplashActivity", "API response indicates failure")
                    // Handle error case if needed
                }
            },
            { error ->
                Log.e("SplashActivity", "API error: ${error.message}")
                // Handle error case if needed
            })

        queue.add(jsonObjectRequest)
    }

    private fun startMainActivity() {
        // Create an intent to start MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the splash activity to prevent going back to it when pressing back button
    }
    }