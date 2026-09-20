package com.example.vehicleid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<android.widget.Button>(R.id.btnIdentify).setOnClickListener {
            startActivity(Intent(this, CaptureActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnDatabases).setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnVerified).setOnClickListener {
            startActivity(Intent(this, VerifiedVehiclesActivity::class.java))
        }
    }
}
