package com.example.parkour.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ParkourClassificationView(
    modifier: Modifier,
    parkourId : Int
){

    Column {
        Text(
            text = "Classement"
        )
    }

}