package com.example.vehicleid

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class VerifiedVehiclesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verified)

        val list = VehicleDbHelper(this).getVerifiedVehicles()
        val listView = findViewById<ListView>(R.id.listVerified)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    }
}
