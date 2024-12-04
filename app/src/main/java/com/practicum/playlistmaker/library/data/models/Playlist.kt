package com.practicum.playlistmaker.library.data.models

class Playlist(
    val id: Long,
    val artwork: String,
    val playlistName: String,
    val description: String,
    var numOfTracks: Int,
    var savedTracksIDs: ArrayList<Int>?
) {

    fun addTrackIDtoSavedTracksIDsArray(id: Int) {
        savedTracksIDs?.add(id)
        updateNumOfTracks()
    }

    fun deleteTrackIDfromSavedTracksIDsArray(id: Int) {
        if (!savedTracksIDs.isNullOrEmpty() && savedTracksIDs!!.contains(id)) {
            savedTracksIDs!!.remove(id)
        }
        updateNumOfTracks()
    }

    fun updateNumOfTracks() {
        if (!savedTracksIDs.isNullOrEmpty()) {

            numOfTracks = savedTracksIDs!!.size
        } else numOfTracks = 0
    }
}