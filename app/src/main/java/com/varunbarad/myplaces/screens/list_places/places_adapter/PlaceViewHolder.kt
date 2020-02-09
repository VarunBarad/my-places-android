package com.varunbarad.myplaces.screens.list_places.places_adapter

import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.myplaces.databinding.ListItemPlaceBinding
import com.varunbarad.myplaces.model.UiLocation

class PlaceViewHolder(
    private val viewBinding: ListItemPlaceBinding,
    private val openInMapButtonClickListener: (UiLocation) -> Unit,
    private val openDetailsButtonClickListener: (UiLocation) -> Unit,
    private val deleteButtonClickListener: (UiLocation) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(place: UiLocation) {
        this.viewBinding.textViewName.text = place.name
        this.viewBinding.textViewComment.text = place.comments

        this.viewBinding.buttonOpenInMap.setOnClickListener {
            this.openInMapButtonClickListener(place)
        }
        this.viewBinding.buttonOpenDetails.setOnClickListener {
            this.openDetailsButtonClickListener(place)
        }
        this.viewBinding.buttonDelete.setOnClickListener {
            this.deleteButtonClickListener(place)
        }

        this.viewBinding.executePendingBindings()
    }
}
