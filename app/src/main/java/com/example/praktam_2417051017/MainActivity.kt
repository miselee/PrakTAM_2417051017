package com.example.praktam_2417051017

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import model.KategoriSource
import com.example.praktam_2417051017.ui.theme.PrakTAM_2417051017Theme
import model.Anggaran
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
    Box {
        LazyColumn{

            item {
                Text(
                    text = "Pengeluaran",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 5.dp)
                )

                Text(
                    text = "Anggaran",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp, 6.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(KategoriSource.dummyAnggaran) { kategori ->
                        AnggaranItem(kategori)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            val groupedData = KategoriSource.dummyKategori.groupBy { it.Tanggal }

            groupedData.forEach { (tanggal, listKategori) ->
                item {
                    HeaderColumn(
                        tanggal = tanggal,
                        total = 0
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(listKategori) { kategori ->
                    DetailScreen(kategori = kategori)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah")
            }
        }
    }
}

@Composable
fun AnggaranItem(anggaran: Anggaran) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = anggaran.imageRes),
                contentDescription = anggaran.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = anggaran.nama,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = anggaran.Bulan,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Rp ${anggaran.sisa} / Rp ${anggaran.total}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HeaderColumn(tanggal: String, total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
            .padding(horizontal = 20.dp)
            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
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
                modifier = Modifier.align(Alignment.CenterEnd)
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