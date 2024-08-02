package com.example.gpermission

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var permissionStatus: TextView
    private lateinit var locationInfo: TextView
    private lateinit var buttonStart: Button
    private lateinit var locationManager: LocationManager

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionStatus = findViewById(R.id.permissionStatus)
        locationInfo = findViewById(R.id.locationInfo)
        buttonStart = findViewById(R.id.buttonStart)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        checkLocationPermission()

        buttonStart.setOnClickListener {
            getLocation()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            onLocationPermissionGranted()
        }
    }

    private fun onLocationPermissionGranted() {
        permissionStatus.text = "Location permission: Granted"
        permissionStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        buttonStart.isEnabled = true
        buttonStart.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_dark)
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationInfo.text = "Lat: ${location.latitude}\nLng: ${location.longitude}"
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                onLocationPermissionGranted()
            } else {
                permissionStatus.text = "Location permission: Not granted"
                permissionStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                buttonStart.isEnabled = false
                buttonStart.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
