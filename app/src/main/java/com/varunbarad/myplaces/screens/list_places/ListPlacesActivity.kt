package com.varunbarad.myplaces.screens.list_places

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityListPlacesBinding
import com.varunbarad.myplaces.model.UiLocation
import com.varunbarad.myplaces.screens.add_place.AddPlaceActivity
import com.varunbarad.myplaces.screens.list_places.places_adapter.PlaceClickListener
import com.varunbarad.myplaces.screens.list_places.places_adapter.PlacesAdapter
import com.varunbarad.myplaces.screens.place_details.PlaceDetailsActivity
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.createIntentToOpenCoordinatesOnMap

class ListPlacesActivity : AppCompatActivity(), ListPlacesView, PlaceClickListener {
    private lateinit var viewBinding: ActivityListPlacesBinding

    private val placesAdapter: PlacesAdapter = PlacesAdapter(this)

    private lateinit var presenter: ListPlacesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_places)
        this.setSupportActionBar(this.viewBinding.toolbar)

        val recyclerViewLayoutManager = LinearLayoutManager(
            this.viewBinding.recyclerViewPlaces.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        this.viewBinding.recyclerViewPlaces.layoutManager = recyclerViewLayoutManager
        this.viewBinding.recyclerViewPlaces.addItemDecoration(
            DividerItemDecoration(
                this.viewBinding.recyclerViewPlaces.context,
                recyclerViewLayoutManager.orientation
            )
        )
        this.viewBinding.recyclerViewPlaces.adapter = this.placesAdapter

        this.presenter = ListPlacesPresenter(
            this,
            Dependencies.getRoomPlacesRepository(this)
        )
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
        this.viewBinding.buttonAddPlace.setOnClickListener { this.presenter.addPlace() }
    }

    override fun onStop() {
        this.presenter.onStop()

        this.viewBinding.buttonAddPlace.setOnClickListener(null)

        super.onStop()
    }

    override fun onClickOpenInMap(place: UiLocation) {
        this.presenter.openInMap(place)
    }

    override fun onClickOpenDetails(place: UiLocation) {
        this.presenter.openDetails(place)
    }

    override fun onClickDelete(place: UiLocation) {
        this.presenter.deletePlace(place)
    }

    override fun updateScreen(viewState: ListPlacesViewState) {
        this.placesAdapter.submitList(viewState.places)

        if (viewState.isNoStoredPlacesMessageVisible) {
            this.viewBinding.recyclerViewPlaces.visibility = View.GONE
            this.viewBinding.textViewEmptyPlacesMessage.visibility = View.VISIBLE
        } else {
            this.viewBinding.recyclerViewPlaces.visibility = View.VISIBLE
            this.viewBinding.textViewEmptyPlacesMessage.visibility = View.GONE
        }
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun openAddPlaceScreen() {
        AddPlaceActivity.start(this)
    }

    override fun openPlaceInMap(latitude: Double, longitude: Double) {
        this.startActivity(createIntentToOpenCoordinatesOnMap(latitude, longitude))
    }

    override fun openPlaceDetailsScreen(placeId: Long) {
        PlaceDetailsActivity.start(this, placeId)
    }
}
