package com.varunbarad.myplaces.screens.add_place

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityAddPlaceBinding
import com.varunbarad.myplaces.screens.place_details.PlaceDetailsActivity
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.Event
import com.varunbarad.myplaces.util.PERMISSION_REQUEST_LOCATION
import com.varunbarad.myplaces.util.PermissionRequestResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AddPlaceActivity : AppCompatActivity(), AddPlaceView {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AddPlaceActivity::class.java))
        }
    }

    private lateinit var viewBinding: ActivityAddPlaceBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var presenter: AddPlacePresenter
    private val locationPermissionResultSubject = PublishSubject.create<PermissionRequestResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_place
        )

        this.setSupportActionBar(this.viewBinding.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.presenter = AddPlacePresenter(
            this,
            Dependencies.getRoomPlacesRepository(this)
        )
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
    }

    override fun onStop() {
        this.presenter.onStop()

        this.viewBinding.buttonFetchCoordinates.setOnClickListener(null)
        this.viewBinding.buttonSavePlace.setOnClickListener(null)

        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    this.locationPermissionResultSubject.onNext(PermissionRequestResult.GRANTED)
                } else {
                    this.locationPermissionResultSubject.onNext(PermissionRequestResult.DENIED)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun isLocationPermissionAvailable(): Boolean {
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return (hasCoarseLocationPermission || hasFineLocationPermission)
    }

    override fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_LOCATION
        )
    }

    override fun permissionRequestResultLocationObservable(): Observable<PermissionRequestResult> {
        return this.locationPermissionResultSubject
    }

    override fun requestLocation(
        locationListener: (Location) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            this.fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locationListener(location)
                }
            }.addOnFailureListener {
                errorListener(it)
            }
        } else {
            errorListener(UnsupportedOperationException("Google Play Services not available"))
        }
    }

    override fun onButtonFetchCoordinatesClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonFetchCoordinates.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonFetchCoordinates.setOnClickListener(null) }
        }
    }

    override fun onButtonSavePlaceClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonSavePlace.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonSavePlace.setOnClickListener(null) }
        }
    }

    override fun getNameEditTextValue(): String {
        return this.viewBinding.editTextName.text.toString()
    }

    override fun getCommentsEditTextValue(): String {
        return this.viewBinding.editTextComment.text.toString()
    }

    override fun updateScreen(viewState: AddPlaceViewState) {
        this.viewBinding.editTextName.setText(viewState.nameValue)
        this.viewBinding.editTextComment.setText(viewState.commentsValue)

        this.viewBinding.textInputName.error = viewState.nameErrorText

        this.viewBinding.textInputName.isErrorEnabled = viewState.showNameError

        this.viewBinding.textViewCoordinates.text = viewState.coordinatesText
        this.viewBinding.textViewCoordinates.visibility =
            if (viewState.coordinatesText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        this.viewBinding.textViewGpsAccessRequiredMessage.visibility =
            if (viewState.showLocationPermissionExplanationText) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    override fun close() {
        this.finish()
    }

    override fun openPlaceDetailsScreen(placeId: Long) {
        PlaceDetailsActivity.start(this, placeId)
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
