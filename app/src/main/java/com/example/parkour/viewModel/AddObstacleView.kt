package com.example.parkour.viewModel

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.MutableState
import coil.compose.rememberAsyncImagePainter


@Composable
fun AddObstacleView(modifier: Modifier)  {

    val state = remember {  mutableStateOf(StateRequestAuthorization.UNKNOWN) }

    val launcherState = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            state.value = StateRequestAuthorization.AUTHORIZES
        } else {
            state.value = StateRequestAuthorization.REFUSES
        }
    }
    var cliked by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcherImage =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
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

            Button(onClick = { cliked = true}) {
                Text("Sélectionner une image")
            }

            if (cliked) {
                Confirmation(state, modifier)

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
/*
@Composable
fun Confirmation(context: Context, modifier: Modifier = Modifier, launcher: ActivityResultLauncher<String>){
    Column(modifier = modifier) {
        Text(
            text = "Nous avons besoin de ... voulez-vous l'activer?",
            modifier = modifier.padding(20.dp)
        )
        Button(onClick = {
            launcher.launch("image/*")
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
*/*/
@Composable
fun Confirmation(state: MutableState<StateRequestAuthorization>, modifier: Modifier = Modifier) {

    if (state == StateRequestAuthorization.AUTHORIZES) {
       Confirmation2()
    } else {
        Text("il faut autoriser la caméra")
    }

}

@Composable
fun Confirmation2(modifier: Modifier = Modifier) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "Voulez-vous autoriser la galerie",
            modifier = modifier.padding(20.dp)
        )
        Button(onClick = {
            launcher.launch("image/*")
        }, modifier = modifier) {
            Text("Autoriser")
        }
        Button(onClick = {}, modifier = modifier) {
            Text("Refuser")
        }
    }
}
