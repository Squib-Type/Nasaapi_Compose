package com.example.nasaapi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nasaapi.api.ArtworkItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private const val TAG = "PhotoGalleryScreen"

@Composable
fun PhotoGalleryScreen(
    onPhotoClicked: (photoURL: String?, photoTitle: String?) -> Unit,
    viewModel: PhotoGalleryViewModel = viewModel(factory = PhotoGalleryViewModelFactory())
) {
    val galleryItems by viewModel.galleryItems.collectAsState(initial = emptyList())
    var isRefreshing by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }

    LaunchedEffect(galleryItems) {
        Log.d(TAG, "Response received:: $galleryItems")
        if (galleryItems.isNotEmpty()) {
            showProgressBar = false
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 50.dp)
    ) {
        Column {
            if (showProgressBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    showProgressBar = true
                    viewModel.getStuff(null)
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(galleryItems) { item ->
                        ArtworkGridItem(
                            artworkItem = item,
                            onPhotoClicked = onPhotoClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArtworkGridItem(
    artworkItem: ArtworkItem,
    onPhotoClicked: (photoURL: String?, photoTitle: String?) -> Unit
) {
    AsyncImage(
        model = artworkItem.imageUrl,
        contentDescription = artworkItem.title,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .clickable {
                onPhotoClicked(artworkItem.largeImageUrl, artworkItem.detailText)
            },
        contentScale = ContentScale.Crop
    )
}