package com.varunbarad.myplaces.screens.place_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityPlaceDetailsBinding
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.Event
import com.varunbarad.myplaces.util.copyStringToClipboard
import com.varunbarad.myplaces.util.createIntentToOpenCoordinatesOnMap
import io.reactivex.Observable

class PlaceDetailsActivity : AppCompatActivity(), PlaceDetailsView {
    companion object {
        private const val ERROR_MESSAGE_NO_PLACE_ID_PASSED =
            "No place-id was passed to place-details screen"

        private const val EXTRA_PLACE_ID = "place-id"

        @JvmStatic
        fun start(context: Context, placeId: Long) {
            context.startActivity(Intent(context, PlaceDetailsActivity::class.java).apply {
                putExtra(EXTRA_PLACE_ID, placeId)
            })
        }
    }

    private lateinit var viewBinding: ActivityPlaceDetailsBinding

    private lateinit var presenter: PlaceDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_place_details
        )

        this.setSupportActionBar(this.viewBinding.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.presenter = PlaceDetailsPresenter(
            this,
            Dependencies.getRoomPlacesRepository(this),
            this.getPassedPlaceId()
        )
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
    }

    override fun onStop() {
        this.presenter.onStop()

        this.viewBinding.buttonOpenInMap.setOnClickListener(null)
        this.viewBinding.buttonDelete.setOnClickListener(null)

        super.onStop()
    }

    private fun getPassedPlaceId(): Long {
        val passedPlaceId = this.intent.extras?.getLong(EXTRA_PLACE_ID)

        if (passedPlaceId == null) {
            throw IllegalArgumentException(ERROR_MESSAGE_NO_PLACE_ID_PASSED)
        } else {
            return passedPlaceId
        }
    }

    override fun onButtonCopyCoordinatesClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonCopyCoordinates.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonCopyCoordinates.setOnClickListener(null) }
        }
    }

    override fun onButtonDeletePlaceClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonDelete.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonDelete.setOnClickListener(null) }
        }
    }

    override fun onButtonOpenInMapPlaceClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonOpenInMap.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonOpenInMap.setOnClickListener(null) }
        }
    }

    override fun updateScreen(viewState: PlaceDetailsViewState) {
        this.viewBinding.textViewName.text = viewState.placeNameText
        this.viewBinding.textViewTimestamp.text = viewState.placeTimestampText
        this.viewBinding.textViewComment.text = viewState.placeCommentText
        this.viewBinding.textViewComment.visibility = if (viewState.placeCommentText.isBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        this.viewBinding.textViewCoordinates.text = viewState.placeCoordinatesText

        if (viewState.isLoaderVisible) {
            this.viewBinding.containerPlaceDetails.visibility = View.GONE
            this.viewBinding.progressBar.visibility = View.VISIBLE
        } else {
            this.viewBinding.containerPlaceDetails.visibility = View.VISIBLE
            this.viewBinding.progressBar.visibility = View.GONE
        }
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showTemporaryMessage(messageText: String) {
        Toast.makeText(
            this,
            messageText,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun close() {
        this.finish()
    }

    override fun copyTextToClipboard(textContents: String) {
        this.copyStringToClipboard(textContents)
    }

    override fun openPlaceInMap(latitude: Double, longitude: Double) {
        this.startActivity(createIntentToOpenCoordinatesOnMap(latitude, longitude))
    }
}
