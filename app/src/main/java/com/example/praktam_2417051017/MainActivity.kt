package com.example.praktam_2417051017

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    Box(){
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

            val groupedData = KategoriSource.dummyKategori.groupBy { it.Tanggal }

            Spacer(modifier = Modifier.height(5.dp))

            groupedData.forEach { (tanggal, listKategori) ->
                HeaderColumn(
                    tanggal = tanggal,
                    total = 0,
                )

                Spacer(modifier = Modifier.height(8.dp))

                listKategori.forEach { kategori ->
                    DetailScreen(kategori = kategori,box = Modifier)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ){
            Text("Tambah")
        }
    }
}

@Composable
fun HeaderColumn(tanggal: String, total: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = tanggal,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Pengeluaran: Rp $total",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun DetailScreen(
    kategori: Kategori,
    box: Modifier = Modifier
) {
    var isEdited by remember { mutableStateOf(false) }

    Column(
        modifier = box
            .fillMaxWidth()
            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = kategori.imageRes),
                    contentDescription = kategori.nama,
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text(
                        text = kategori.nama,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "-Rp ${kategori.Pengeluaran}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                }
            }

            IconButton(
                onClick = { isEdited = !isEdited },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = if (isEdited) Icons.Filled.Edit else Icons.Outlined.Edit,
                    contentDescription = "Edited Icon",
                    tint = if (isEdited) Color.Black else Color.Gray
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