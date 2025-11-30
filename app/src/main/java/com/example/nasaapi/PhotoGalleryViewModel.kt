package com.example.nasaapi

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nasaapi.api.ArtworkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val TAG = "PhotoGalleryViewModel"

open class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository()

    private val _galleryItems: MutableStateFlow<List<ArtworkItem>> =
        MutableStateFlow(emptyList())
    open val galleryItems: Flow<List<ArtworkItem>>
        get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos()
                Log.d(TAG, "Initial Items received: $items")

                if (items.isNotEmpty()) {
                    _galleryItems.value = items
                } else {
                    refreshData()
                }

            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch Initial gallery items", ex)
                refreshData()
            }
        }
    }

    fun getStuff(progressBar: ProgressBar?) {
        viewModelScope.launch {
            try {
                progressBar?.visibility = View.GONE
                val items = photoRepository.fetchPhotos()
                Log.d(TAG, " Refreshed Items received: $items")
                _galleryItems.value = items

            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch Refresh gallery items", ex)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos()
                Log.d(TAG, " Refreshed Items received: $items")

                if (items.isNotEmpty()) {
                    _galleryItems.value = items
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch Refresh gallery items", ex)
            }
        }
    }

    fun setGalleryItems(dummyData: List<ArtworkItem>) {
        _galleryItems.value = dummyData
    }
}

class PhotoGalleryViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoGalleryViewModel() as T
    }
}