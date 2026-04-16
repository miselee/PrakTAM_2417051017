package com.example.praktam_2417051017

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.net.Uri

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

    val daftarKategori = remember {
        mutableStateListOf<Kategori>().apply {
            addAll(KategoriSource.dummyKategori)
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {

        composable("home") {
            DaftarPengeluaranScreen(navController, daftarKategori)
        }

        composable("detail/{nama}") { backStackEntry ->

            val nama = backStackEntry.arguments?.getString("nama")

            val item = daftarKategori.find { it.nama == nama }

            if (item != null) {
                DetailScreen(item, navController, true)
            }
        }
    }
}

@Composable
fun DaftarPengeluaranScreen(
    navController: NavController,
    daftarKategori: MutableList<Kategori>
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ){
            item {
                Text(
                    text = "Pengeluaran",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 5.dp)
                )

                Text(
                    text = "Anggaran",
                    style = MaterialTheme.typography.titleMedium,
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

            val groupedData = daftarKategori.groupBy { it.Tanggal }

            groupedData.forEach { (tanggal, listKategori) ->
                item {
                    HeaderColumn(
                        tanggal = tanggal,
                        total = 0
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(listKategori) { kategori ->
                    KategoriItem(
                        kategori,
                        navController,
                        onShowSnackbar = { message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
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

            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    }
}


@Composable
fun AnggaranItem(anggaran: Anggaran) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Image(
                painter = painterResource(id = anggaran.imageRes),
                contentDescription = anggaran.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Inside
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = anggaran.nama,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = anggaran.Bulan,
                    style = MaterialTheme.typography.bodyMedium
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
fun KategoriItem(
    kategori: Kategori,
    navController: NavController,
    onShowSnackbar: (String) -> Unit
) {
    var isEdited by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Box {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        navController.navigate("detail/${kategori.nama}")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = kategori.imageRes),
                    contentDescription = kategori.nama,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(kategori.nama)
                    Text("-Rp ${kategori.Pengeluaran}")
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            delay(1000)

                            isEdited = !isEdited

                            onShowSnackbar(
                                if (isEdited)
                                    "Mode edit ${kategori.nama} aktif"
                                else
                                    "Mode edit dimatikan"
                            )

                            isLoading = false
                        }
                    }
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isEdited)
                                Icons.Filled.Edit
                            else
                                Icons.Outlined.Edit,
                            contentDescription = "Edit"
                        )
                    }
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
            .padding(horizontal = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = kategori.nama,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "-Rp ${kategori.Pengeluaran}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(1500)

                                isEdited = !isEdited

                                snackbarHostState.showSnackbar(
                                    if (isEdited)
                                        "Mode edit aktif untuk ${kategori.nama}"
                                    else
                                        "Mode edit dimatikan"
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
                            contentDescription = "Edit Icon",
                            tint = if (isEdited) Color.Black else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(1500)

                                snackbarHostState.showSnackbar(
                                    "Pengeluaran ${kategori.nama} berhasil dicatat!"
                                )

                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
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
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Kembali")
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

@Preview(showBackground = true)
@Composable
fun DaftarAnggaranScreenPreview() {
    PrakTAM_2417051017Theme {

        val navController = rememberNavController()

        val daftarKategori = remember {
            mutableStateListOf<Kategori>().apply {
                addAll(KategoriSource.dummyKategori)
            }
        }

        DaftarPengeluaranScreen(
            navController,
            daftarKategori
        )
    }
}