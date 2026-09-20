package com.example.vehicleid

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class CaptureActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var txtResults: TextView
    private lateinit var btnConfirm: Button
    private lateinit var btnWebSearch: Button
    private var photoUri: Uri? = null
    private var bestMatch: String? = null

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            imgPreview.setImageURI(photoUri)
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            photoUri = uri
            imgPreview.setImageURI(uri)
        }
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) launchCamera() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        imgPreview = findViewById(R.id.imgPreview)
        txtResults = findViewById(R.id.txtResults)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnWebSearch = findViewById(R.id.btnWebSearch)

        findViewById<Button>(R.id.btnCamera).setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                requestCameraPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        findViewById<Button>(R.id.btnGallery).setOnClickListener {
            pickImage.launch("image/*")
        }

        findViewById<Button>(R.id.btnAnalyze).setOnClickListener {
            runMockAnalysis()
        }

        btnConfirm.setOnClickListener {
            bestMatch?.let { match ->
                VehicleDbHelper(this).saveVerifiedVehicle(match)
                android.widget.Toast.makeText(this, "Saved to Verified Vehicles", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        btnWebSearch.setOnClickListener {
            maybeShowConsentThenSearch()
        }
    }

    private fun launchCamera() {
        val imagesDir = File(getExternalFilesDir("images"), "")
        imagesDir.mkdirs()
        val photoFile = File(imagesDir, "capture_${System.currentTimeMillis()}.jpg")
        photoUri = FileProvider.getUriForFile(this, "com.example.vehicleid.fileprovider", photoFile)
        takePicture.launch(photoUri)
    }

    private fun runMockAnalysis() {
        // Placeholder for the on-device AI model + local database matching engine.
        bestMatch = "Toyota Corolla"
        txtResults.text = "Best match: Toyota Corolla — 91%\nAlternative: Toyota Camry — 73%\nAlternative: Toyota Yaris — 62%"
        btnConfirm.visibility = android.view.View.VISIBLE
        btnWebSearch.visibility = android.view.View.VISIBLE
    }

    private fun maybeShowConsentThenSearch() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        if (prefs.getBoolean("web_search_consent", false)) {
            openWebSearch()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Search the web?")
                .setMessage("This sends the vehicle name to a web search provider to fetch comparison images. Continue?")
                .setPositiveButton("Allow") { _, _ ->
                    prefs.edit().putBoolean("web_search_consent", true).apply()
                    openWebSearch()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun openWebSearch() {
        val query = Uri.encode(bestMatch ?: "vehicle")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?tbm=isch&q=$query"))
        startActivity(intent)
    }
}
