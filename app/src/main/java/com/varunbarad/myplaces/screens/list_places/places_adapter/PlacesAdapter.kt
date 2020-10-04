package com.varunbarad.myplaces.screens.list_places.places_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.varunbarad.myplaces.databinding.ListItemPlaceBinding
import com.varunbarad.myplaces.model.UiLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PlacesAdapter(
    private val placeClickListener: PlaceClickListener
) : ListAdapter<UiLocation, PlaceViewHolder>(UiLocation.DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            viewBinding = ListItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            placeClickListener = this.placeClickListener
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }
}
