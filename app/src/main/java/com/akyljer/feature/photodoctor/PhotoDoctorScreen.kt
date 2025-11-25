package com.akyljer.feature.photodoctor

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoDoctorScreen(viewModel: PhotoDoctorViewModel) {
    val state = viewModel.state.collectAsState().value
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Photo Doctor", style = MaterialTheme.typography.titleLarge)
        Button(
            onClick = { viewModel.analyze(dummyBitmap()) },
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text("Analyze sample photo")
        }
        if (state.isProcessing) {
            CircularProgressIndicator()
        }
        state.latest?.let { diagnosis ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Latest: ${diagnosis.label}")
                    Text("Confidence: ${(diagnosis.confidence * 100).toInt()}%")
                    Text(diagnosis.recommendation)
                }
            }
        }
        Text(text = "History", modifier = Modifier.padding(top = 12.dp))
        LazyColumn {
            items(state.history) { item ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(item.label)
                        Text("Conf: ${(item.confidence * 100).toInt()}%")
                        Text(item.recommendation)
                    }
                }
            }
        }
    }
}

private fun dummyBitmap(): Bitmap =
    Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.GREEN)
    }
