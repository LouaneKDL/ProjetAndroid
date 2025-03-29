package com.example.parkour.viewModel

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.dialog.Confirmation
import coil.compose.rememberAsyncImagePainter
import com.example.parkour.R

@Composable
fun AddObstacleView(modifier: Modifier)  {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
            }
        }
    var state by remember {  mutableStateOf(StateRequestAuthorization.UNKNOWN) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            state = StateRequestAuthorization.AUTHORIZES
            launcher.launch("image/*")
        } else {
            state = StateRequestAuthorization.REFUSES
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter un obstacle",
            modifier = modifier.padding(50.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            Text(
                text = "Nom",
                modifier = modifier.padding(
                    start = 60.dp,
                    top = 30.dp,
                    end = 60.dp,
                    bottom = 30.dp
                )
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Rentrer un nom") },
                modifier = Modifier.padding(end = 20.dp)
            )
        }


            Row(
                modifier = Modifier.padding(top = 30.dp)
            ) {

                Text(
                    text = "Photo",
                    modifier = modifier.padding(
                        start = 60.dp,
                        top = 30.dp,
                        end = 30.dp,
                        bottom = 30.dp
                    )
                )
                val  context = LocalContext.current
               Button(onClick = { Confirmation(
                   context = context, modifier = modifier,
                   launcher = launcher
               ) }) {
                   Text("Sélectionner une image")
               }






            }


            imageUri?.let { uri ->
                //  Text("oui")

                val painter = rememberAsyncImagePainter(uri)
                Image(
                    painter = painter,
                    contentDescription = "Image sélectionnée",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
}




@Composable
fun Confirmation(context: Context, modifier: Modifier = Modifier, launcher: ActivityResultLauncher<String>){
    Column(modifier = modifier) {
        Text(
            text = "Nous avons besoin de ... voulez-vous l'activer?",
            modifier = modifier.padding(20.dp)
        )
        Button(onClick = {
            launcher.launch(Manifest.permission.CAMERA)
        },
            modifier = modifier) {
            Text("Autoriser")
        }
        Button(onClick = {

        },
            modifier = modifier) {
            Text("Refuser")
        }
    }
}