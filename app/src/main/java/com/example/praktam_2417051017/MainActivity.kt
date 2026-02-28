package com.example.praktam_2417051017

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import model.KategoriSource
import com.example.praktam_2417051017.ui.theme.PrakTAM_2417051017Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051017Theme {
            }
        }
    }
}

@Composable
fun Greeting() {
    val kategori = KategoriSource.dummyKategori[0]

Column(modifier = Modifier. fillMaxSize().padding(24.dp)){
    Text(text = "Keterangan: ${kategori.keterangan}")
    Text(text = "Jumlah: Rp. ${kategori.jumlah}")
}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM_2417051017Theme {
        Greeting()
    }
}