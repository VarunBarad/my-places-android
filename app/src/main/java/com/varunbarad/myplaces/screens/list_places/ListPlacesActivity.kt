package com.varunbarad.myplaces.screens.list_places

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    companion object {
        private const val REQUEST_CODE_FILE_CHOOSER = 1809
    }

    private lateinit var viewBinding: ActivityListPlacesBinding

    private val placesAdapter: PlacesAdapter = PlacesAdapter(this)

    private lateinit var presenter: ListPlacesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityListPlacesBinding.inflate(layoutInflater)
        this.setContentView(this.viewBinding.root)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_list_places, menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
        this.viewBinding.buttonAddPlace.setOnClickListener { this.presenter.addPlace() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.button_export -> {
                this.presenter.exportData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        this.presenter.onStop()

        this.viewBinding.buttonAddPlace.setOnClickListener(null)

        super.onStop()
    }

    @Deprecated(message = "Because its parent method is deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
            try {
                val result = when (resultCode) {
                    RESULT_OK -> {
                        val fileUri = data!!.data!!
                        val outputStream = contentResolver.openOutputStream(fileUri)!!
                        FileChooserResult.Success(outputStream)
                    }
                    RESULT_CANCELED -> FileChooserResult.Error
                    else -> FileChooserResult.Error
                }
                this.presenter.onExportDataFileChooserResult(result)
            } catch (e: Exception) {
                Log.e("MyPlaces", e.message, e)
                this.showMessage(getString(R.string.message_errorInExport))
            }
        }
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

    override fun openExportFileChooser(fileName: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER)
    }
}
