package com.varunbarad.myplaces.screens.list_places.places_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.varunbarad.myplaces.databinding.ListItemPlaceBinding
import com.varunbarad.myplaces.model.UiLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PlacesAdapter : ListAdapter<UiLocation, PlaceViewHolder>(UiLocation.DIFF_CALLBACK) {
    private val openInMapClickSubject = PublishSubject.create<UiLocation>()
    private val openDetailsClickSubject = PublishSubject.create<UiLocation>()
    private val deleteClickSubject = PublishSubject.create<UiLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            viewBinding = ListItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            openInMapButtonClickListener = this::openInMapButtonClickListener,
            openDetailsButtonClickListener = this::openDetailsButtonClickListener,
            deleteButtonClickListener = this::deleteButtonClickListener
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    private fun openInMapButtonClickListener(place: UiLocation) {
        this.openInMapClickSubject.onNext(place)
    }

    private fun openDetailsButtonClickListener(place: UiLocation) {
        this.openDetailsClickSubject.onNext(place)
    }

    private fun deleteButtonClickListener(place: UiLocation) {
        this.deleteClickSubject.onNext(place)
    }

    fun getOpenInMapObservable(): Observable<UiLocation> = this.openInMapClickSubject

    fun getOpenDetailsObservable(): Observable<UiLocation> = this.openDetailsClickSubject

    fun getDeleteObservable(): Observable<UiLocation> = this.deleteClickSubject
}
