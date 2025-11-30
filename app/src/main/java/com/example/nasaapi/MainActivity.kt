package com.example.nasaapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "photoGallery"
                    ) {
                        composable("photoGallery") {
                            PhotoGalleryScreen(
                                onPhotoClicked = { photoURL, photoTitle ->
                                    val encodedUrl = URLEncoder.encode(
                                        photoURL ?: "",
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    val encodedTitle = URLEncoder.encode(
                                        photoTitle ?: "",
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate("photoSingle/$encodedUrl/$encodedTitle")
                                }
                            )
                        }
                        composable(
                            route = "photoSingle/{photoURL}/{photoTitle}",
                            arguments = listOf(
                                navArgument("photoURL") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("photoTitle") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val encodedUrl = backStackEntry.arguments?.getString("photoURL")
                            val encodedTitle = backStackEntry.arguments?.getString("photoTitle")

                            val photoURL = encodedUrl?.let {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                            }
                            val photoTitle = encodedTitle?.let {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                            }

                            PhotoSingleScreen(photoURL = photoURL, photoTitle = photoTitle)
                        }
                    }
                }
            }
        }
    }
}