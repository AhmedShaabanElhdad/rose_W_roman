package com.smartzone.myapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityMapsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.order_details.address.OrderAddressesViewModel
import com.smartzone.myapp.ui.order_details.details.OrderDetailsActivity
import com.smartzone.myapp.utilis.LocationHelper
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback,
    LocationHelper.LocationListner,
    com.google.android.gms.location.places.ui.PlaceSelectionListener {
    val REQUEST_COARSE_LOCATION = 1
    private lateinit var mMap: GoogleMap
    lateinit var binding: ActivityMapsBinding
    var mLat = 0.0
    var mLon = 0.0
    var mAddress = ""
    var mCity = ""
    var mCountry = ""
    lateinit var location: LocationHelper

    var promocode = ""

    val viewModel: OrderAddressesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        location = LocationHelper(this, this)
        showLoading()
        location.init()
        initializePlaceLayer()


        promocode = intent.extras?.getString("promocode", "").toString()

        observeSuccess()

//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
//        autocompleteFragment!!.setPlaceFields(
//            Arrays.asList(
//                Place.Field.ID,
//                Place.Field.LAT_LNG,
//                Place.Field.NAME
//            )
//        )
//        autocompleteFragment.setCountry("EG")
//        if (autocompleteFragment.view != null) (autocompleteFragment.requireView()
//            .findViewById<View>(R.id.places_autocomplete_search_input) as EditText).textSize = 13.0f
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                val cameraUpdate =
//                    CameraUpdateFactory.newLatLngZoom(place.latLng, 16.0f)
//                mMap.animateCamera(cameraUpdate)
//            }
//
//            override fun onError(status: Status) {}
//        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnConfirm.setOnClickListener {
            if (mLat != 0.0 && mLon != 0.0) {
                when {
                    txt_current_location.text.toString().isEmpty() -> txt_current_location.error =
                        getString(
                            R.string.field_required
                        )
                    txt_name_location.text.toString().isEmpty() -> txt_name_location.error =
                        getString(
                            R.string.field_required
                        )
                    else -> viewModel.saveAddress(
                        txt_name_location.text.toString(),
                        mLon,
                        mLat,
                        txt_current_location.text.toString()
                    )
                }
            }
        }

        binding.buttonDeterminLocation.setOnClickListener {
            binding.locationLayout.visibility = View.VISIBLE
        }

        binding.back.setOnClickListener { onBackPressed() }
        binding.notification.setOnClickListener {
            openNotification()
        }
    }

    private fun initializePlaceLayer() {


        val autocompleteFragment: PlaceAutocompleteFragment =
            fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(this)
        autocompleteFragment.setHint("Search New Location")


//        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
//
//        val typeFilter = AutocompleteFilter.Builder()
//            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
//            .build()
//
//        autocompleteFragment.setFilter(typeFilter)
//
//        autocompleteFragment.setOnPlaceSelectedListener(this)
    }

    private fun callPlaceAutocompleteActivityIntent() {
        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
            //PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (e: GooglePlayServicesRepairableException) {
            // TODO: Handle the error.
        } catch (e: GooglePlayServicesNotAvailableException) {
        }
    }

    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 101

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.i("TAG", "Place:$place")
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Log.i("TAG", status.statusMessage)
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private fun observeSuccess() {
        viewModel.success.observe(this, Observer {
            if (it) {

                startActivity(OrderDetailsActivity.getIntent(this, promocode, mAddress))

//                val returnIntent = Intent()
//                returnIntent.putExtra("lat", mLat)
//                returnIntent.putExtra("lon", mLon)
//
//                setResult(Activity.RESULT_OK, returnIntent)
//                finish()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isRotateGesturesEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false


//        mMap.setOnCameraIdleListener {
//            val latLng = mMap.cameraPosition.target
//            mLat = latLng.latitude
//            mLon = latLng.longitude
//
//            binding.address.setText(getAddress(latLng))
//        }

    }

    companion object {
//        fun getIntent(context: Context): Intent = Intent(context, MapsActivity::class.java)

        fun getIntent(context: Context, promocode: String): Intent {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("promocode", promocode)
            return intent
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_maps
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
//
//    fun getAddress(Location: Location): String? {
//        ReverseGeocoding(
//            Location.latitude,
//            Location.longitude,
//            getString(R.string.google_maps_key)
//        )
//            .setLanguage("en")
//            .fetch(object : Callback {
//                override fun onResponse(response: Response) {
//                    mAddress = response.results[0].formattedAddress
//                    binding.txtCurrentLocation.setText(mAddress)
//                    //binding.address.setText(mAddress)
//                }
//
//                override fun onFailed(
//                    response: Response,
//                    e: IOException
//                ) {
//                }
//            })
//        return mAddress
//    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
            } else {
                Log.w("Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("Current loction address", "Canont get Address!")
        }
        return strAdd
    }

    override fun getLocation(location: Location?) {
        if (location != null) {

            hideLoading()
            mLat = location.latitude
            mLon = location.longitude
            mAddress = getCompleteAddressString(
                location.latitude,
                location.longitude
            ) ?: ""
            txt_current_location.setText(mAddress)
            val latLng = LatLng(mLat, mLon)
            val cameraUpdate =
                CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
            mMap.animateCamera(cameraUpdate)


            val circleOptions = CircleOptions()
                .center(latLng)
                .radius(200.0)
                .strokeWidth(2f)
                .strokeColor(Color.parseColor("#3C5A99"))
                .fillColor(Color.parseColor("#503C5A99"))
            mMap.addCircle(circleOptions)

            this.location.stopLocationUpdate(this)
        }
    }

    override fun sendRequsestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_COARSE_LOCATION
        )
        hideLoading()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showLoading()
                location.init()
            }
        }
    }

    override fun onPlaceSelected(p0: Place?) {

    }

    override fun onError(p0: Status?) {

    }
}