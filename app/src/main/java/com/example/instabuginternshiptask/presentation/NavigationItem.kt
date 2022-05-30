package com.example.instabuginternshiptask.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Get : NavigationItem("GET", Icons.Default.Check, "GET")
    object Post : NavigationItem("POST", Icons.Default.Star, "POST")

}