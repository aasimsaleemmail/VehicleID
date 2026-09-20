package com.example.vehicleid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DatabaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        val txtNhtsa = findViewById<TextView>(R.id.txtNhtsaStatus)
        findViewById<Button>(R.id.btnInstallNhtsa).setOnClickListener {
            txtNhtsa.text = "Status: Installed"
        }

        val txtOpen = findViewById<TextView>(R.id.txtOpenStatus)
        findViewById<Button>(R.id.btnInstallOpen).setOnClickListener {
            txtOpen.text = "Status: Installed"
        }
    }
}
