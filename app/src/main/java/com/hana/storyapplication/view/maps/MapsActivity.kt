package com.hana.storyapplication.view.maps

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.hana.storyapplication.R
import com.hana.storyapplication.data.Result
import com.hana.storyapplication.databinding.ActivityMapsBinding
import com.hana.storyapplication.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.hana.storyapplication.di.Injection
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory(Injection.injectionRepository(this))
    }
    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        val unesaKetintang = LatLng(-7.310580, 112.731194)
        mMap.addMarker(
            MarkerOptions()
                .position(unesaKetintang)
                .title("Universitas Negeri Surabaya")
                .snippet("Kampus Ketintang")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(unesaKetintang))


        getLocation()
        getPostLocation()
        setMapStyle(googleMap)
    }



    private fun getPostLocation() {
        lifecycleScope.launch {
            viewModel.getStoriesWithLocation().observe(this@MapsActivity) { stories ->
                if (stories != null) {
                    when (stories) {
                        is Result.Loading -> {
                            Log.d(TAG, "onMapReady: Loading")
                        }

                        is Result.Success -> {
                            stories.data.listStory.forEach { story ->
                                val latlng = LatLng(story.lat!!, story.lon!!)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(story.lat, story.lon))
                                        .title("Story dari : ${story.name}")
                                        .snippet("Deskripsi: ${story.description}")
                                )
                                boundsBuilder.include(latlng)
                            }
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    300
                                )
                            )
                        }

                        is Result.Error -> {
                            showToast(stories.error)
                        }
                    }
                }
            }
        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success =
                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        binding.root.context,
                        R.raw.map_style
                    )
                )
            if (!success) {
                showToast(getString(R.string.invalid_location))
            }
        } catch (exception: Resources.NotFoundException) {
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            }
        }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}