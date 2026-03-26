package model

data class Kategori(
    val nama: String,
    val total: Int,
    val Pengeluaran: Int,
    val Tanggal: String,
    val imageRes: Int,
)

data class Anggaran(
    val nama: String,
    val total: Int,
    val sisa: Int,
    val Bulan : String,
    val imageRes: Int,
)

