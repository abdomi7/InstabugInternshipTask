@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.instabuginternshiptask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.instabuginternshiptask.R
import com.example.instabuginternshiptask.presentation.screens.RequestScreen
import com.example.instabuginternshiptask.presentation.theme.InstabugInternshipTaskTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstabugInternshipTaskTheme {
                RequestScreen()
            }
        }
    }
}

@Composable
fun TopBar() {
    SmallTopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            titleContentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}



