package com.varunbarad.myplaces.model

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class UiLocation(
    val locationId: Long,
    val name: String,
    val comments: String,
    val timestamp: Date,
    val latitude: String,
    val longitude: String
) {
    companion object {
        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiLocation>() {
            override fun areItemsTheSame(oldItem: UiLocation, newItem: UiLocation): Boolean {
                return (oldItem.locationId == newItem.locationId)
            }

            override fun areContentsTheSame(oldItem: UiLocation, newItem: UiLocation): Boolean {
                return (oldItem == newItem)
            }
        }
    }
}
