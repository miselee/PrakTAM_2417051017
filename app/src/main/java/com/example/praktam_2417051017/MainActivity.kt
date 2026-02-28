package com.example.praktam_2417051017

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import model.KategoriSource
import com.example.praktam_2417051017.ui.theme.PrakTAM_2417051017Theme
import model.Kategori

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051017Theme {
                DaftarPengeluaranScreen()
            }
        }
    }
}

@Composable
fun DaftarPengeluaranScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(all = 24.dp)
    ) {

        Text(
            text="Pengeluaran",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        HeaderColumn(KategoriSource.dummyKategori[0])

        Spacer(modifier = Modifier.height(5.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
        ){
            KategoriSource.dummyKategori.forEach { kategori ->
                DetailScreen(kategori = kategori)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Tambah")
        }
    }

}

@Composable
fun HeaderColumn(kategori: Kategori){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween // ni buat biar pinggirmya sama
    ){
        Text(
            text = kategori.Tanggal,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text ="Pengeluaran: Rp ${kategori.TotalPengeluaran}" ,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun DetailScreen(kategori: Kategori){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Image(
                painter = painterResource(id = kategori.imageRes),
                kategori.nama,
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ){
                Text(
                    text = kategori.nama,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text ="-Rp ${kategori.Pengeluaran}" ,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DaftarAnggaranScreenPreview() {
    PrakTAM_2417051017Theme {
        DaftarPengeluaranScreen()
    }
}