package com.example.praktam_2417051017

import coil.compose.AsyncImage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.praktam_2417051017.model.Kategori
import com.example.praktam_2417051017.ui.theme.PrakTAM_2417051017Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam_2417051017.network.RetrofitClient
import model.Anggaran

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {

    var kategoriList by remember { mutableStateOf<List<Kategori>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {

        composable("home") {
            DaftarPengeluaranScreen(navController) { fetchedData ->
                kategoriList = fetchedData
            }
        }

        composable("detail/{nama}") { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val item = kategoriList.find { it.nama == nama }

            if (item != null) {
                DetailScreen(item, navController, true)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Data tidak ditemukan")
                }
            }
        }
    }
}

@Composable
fun DaftarPengeluaranScreen(
    navController: NavController,
    onDataLoaded: (List<Kategori>) -> Unit = {}
) {
    var data by remember { mutableStateOf<List<Kategori>>(emptyList()) }
    var anggaranData by remember { mutableStateOf<List<Anggaran>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        try {
            val result = RetrofitClient.instance.getKategori()

            data = result.kategori
            anggaranData = result.anggaran

            onDataLoaded(data)

            isLoading = false
            isError = false

        } catch (e: Exception) {
            isLoading = false
            isError = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            isError -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Gagal memuat data", color = Color.Red)
                }
            }

            data.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data")
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {

                    item {
                        Text(
                            "Pengeluaran",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )

                        Text(
                            "Anggaran",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(anggaranData) {
                                AnggaranItem(it)
                            }
                        }
                    }

                    val groupedData = data.groupBy { it.tanggal }

                    groupedData.forEach { (tanggal, listKategori) ->

                        item {
                            HeaderColumn(
                                tanggal,
                                listKategori.sumOf { it.pengeluaran }
                            )
                        }

                        items(listKategori) { kategori ->
                            KategoriItem(
                                kategori,
                                navController
                            ) { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}



@Composable
fun AnggaranItem(anggaran: Anggaran) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {

            AsyncImage(
                model = anggaran.imageUrl,
                contentDescription = anggaran.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.food),
                error = painterResource(R.drawable.food)
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(anggaran.nama, fontWeight = FontWeight.Bold)
                Text(anggaran.bulan)
                Text(
                    "Rp ${anggaran.sisa} / Rp ${anggaran.total}",
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(tanggal, color = Color.Gray)
        Text("Rp $total", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun KategoriItem(
    kategori: Kategori,
    navController: NavController,
    onShowSnackbar: (String) -> Unit
) {
    var isEdited by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    navController.navigate("detail/${kategori.nama}")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = kategori.imageUrl,
                contentDescription = kategori.nama,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.food),
                error = painterResource(R.drawable.food)
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(kategori.nama, fontWeight = FontWeight.SemiBold)
                Text("-Rp ${kategori.pengeluaran}", color = Color.Red)
            }

            IconButton(onClick = {
                scope.launch {
                    isLoading = true
                    delay(800)
                    isEdited = !isEdited
                    onShowSnackbar("Mode edit ${if (isEdited) "aktif" else "mati"}")
                    isLoading = false
                }
            }) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                } else {
                    Icon(
                        if (isEdited) Icons.Filled.Edit else Icons.Outlined.Edit,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    kategori: Kategori,
    navController: NavController,
    isFullScreen: Boolean = false
) {
    var isLoading by remember { mutableStateOf(false) }
    var isEdited by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = kategori.imageUrl,
                        contentDescription = kategori.nama,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.food),
                        error = painterResource(R.drawable.food)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = kategori.nama,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "-Rp ${kategori.pengeluaran}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE53935),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(800)

                                isEdited = !isEdited

                                snackbarHostState.showSnackbar(
                                    if (isEdited)
                                        "Mode edit aktif"
                                    else
                                        "Mode edit mati"
                                )

                                isLoading = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isEdited)
                                Icons.Filled.Edit
                            else
                                Icons.Outlined.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            delay(1000)

                            snackbarHostState.showSnackbar(
                                "Berhasil disimpan!"
                            )

                            isLoading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Menyimpan...")
                    } else {
                        Text("Simpan Pengeluaran")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isFullScreen) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kembali")
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DaftarAnggaranScreenPreview() {
    PrakTAM_2417051017Theme {

        val navController = rememberNavController()

        DaftarPengeluaranScreen(
            navController = navController,
            onDataLoaded = {}
        )
    }
}