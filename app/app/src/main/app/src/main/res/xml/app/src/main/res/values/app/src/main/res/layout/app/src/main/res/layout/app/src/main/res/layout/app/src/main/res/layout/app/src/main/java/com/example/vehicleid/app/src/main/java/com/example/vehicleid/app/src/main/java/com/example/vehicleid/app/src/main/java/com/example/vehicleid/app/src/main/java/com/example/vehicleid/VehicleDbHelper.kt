package com.example.vehicleid

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VehicleDbHelper(context: Context) :
    SQLiteOpenHelper(context, "vehicle_id.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE verified_vehicles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "match_name TEXT, " +
                "confirmed_at INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS verified_vehicles")
        onCreate(db)
    }

    fun saveVerifiedVehicle(matchName: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("match_name", matchName)
            put("confirmed_at", System.currentTimeMillis())
        }
        db.insert("verified_vehicles", null, values)
    }

    fun getVerifiedVehicles(): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            "verified_vehicles", arrayOf("match_name", "confirmed_at"),
            null, null, null, null, "confirmed_at DESC"
        )
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        return results
    }
    }
