package edu.cs371m.fcgooglemaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*



class MainActivity
    : AppCompatActivity(),
    OnMapReadyCallback
{
    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var geocoder: Geocoder
    private val LOCATION_REQUEST_CODE = 101
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        checkGooglePlayServices()
        // XXX Write me.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geocoder = Geocoder(applicationContext, Locale.getDefault())

        // Initialize Places.
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        // Create a new Places client instance.
        placesClient = Places.createClient(this)

        // This code is correct, but it assumes an EditText in your layout
        // called mapET and a go button called goBut
        mapET.setOnEditorActionListener { /*v*/_, actionId, event ->
            // If user has pressed enter, or if they hit the soft keyboard "send" button
            // (which sends DONE because of the XML)
            if ((event != null
                        &&(event.action == KeyEvent.ACTION_DOWN)
                        &&(event.keyCode == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideKeyboard()
                goBut.callOnClick()
            }
            false
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        requestPermission(LOCATION_REQUEST_CODE)
        if( locationPermissionGranted ) {
            if (map != null) {
                val permission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)

                if (permission == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    requestPermission(LOCATION_REQUEST_CODE)
                }
            }
        }

        // XXX Write me.
        // Start the map at the Harry Ransom center
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setOnMapLongClickListener {
            // Goodbye markers
            map.clear()
        }

        val address = geocoder.getFromLocationName("Harry Ransom center", 1)
        val location = address[0]
        val harryRansomCenter = LatLng(location.latitude, location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(harryRansomCenter, 15.0f))
        map.setOnMapClickListener {
            val latitude = String.format("%.3f", it.latitude)
            val longitude = String.format("%.3f", it.longitude)
            val titlePosition = "$latitude, $longitude"
            map.addMarker(
                    MarkerOptions()
                            .position(it)
                            .title(titlePosition)

            )
        }

        goBut.setOnClickListener {
            val newAddress  = geocoder.getFromLocationName(mapET.text.toString(), 3)
            if(newAddress.isEmpty()){
                Toast.makeText(applicationContext, "The address does not exit", Toast.LENGTH_SHORT).show()
            } else{
                val newLocation = newAddress[0]
                val typedAddress =  LatLng(newLocation.latitude, newLocation.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(typedAddress, 15.0f))
            }

        }

        clearBut.setOnClickListener {
            map.clear()
        }
    }

    // Everything below here is correct

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0);
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode =
            googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 257).show()
            } else {
                Log.i(javaClass.simpleName,
                    "This device must install Google Play Services.")
                finish()
            }
        }
    }

    private fun requestPermission(requestCode: Int) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show()
                } else {
                    locationPermissionGranted = true
                }
            }
        }
    }
}
