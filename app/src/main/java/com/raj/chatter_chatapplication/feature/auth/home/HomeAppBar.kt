package com.raj.chatter_chatapplication.feature.auth.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Chatter Chat",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Cyan
        )
    )
}