package com.varunbarad.myplaces.model

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class UiNote(
    val noteId: Long,
    val title: String,
    val content: String,
    val timestamp: Date
) {
    companion object {
        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiNote>() {
            override fun areItemsTheSame(oldItem: UiNote, newItem: UiNote): Boolean {
                return (oldItem.noteId == newItem.noteId)
            }

            override fun areContentsTheSame(oldItem: UiNote, newItem: UiNote): Boolean {
                return (oldItem == newItem)
            }
        }
    }
}
