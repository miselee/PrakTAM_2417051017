package model
import com.example.praktam_2417051017.R

object KategoriSource {
    val dummyKategori = listOf(
        Kategori("Clothes", total = 0, Pengeluaran = 20000, Tanggal = "Sen, 9/3", imageRes = R.drawable.clothes),
        Kategori("Food", total = 0, Pengeluaran = 15000, Tanggal = "Sen, 9/3", imageRes = R.drawable.food),

        Kategori("Transportation", total = 0, Pengeluaran = 10000, Tanggal = "Sel, 10/3", imageRes = R.drawable.transportation),
        Kategori("Food", total = 0, Pengeluaran = 5000, Tanggal = "Sel, 10/3", imageRes = R.drawable.food),

        Kategori("Food", total = 0, Pengeluaran = 12000, Tanggal = "Rab, 11/3", imageRes = R.drawable.food),

        Kategori("Transportation", total = 0, Pengeluaran = 10000, Tanggal = "Sel, 12/3", imageRes = R.drawable.transportation),
        Kategori("Food", total = 0, Pengeluaran = 15000, Tanggal = "Sel, 12/3", imageRes = R.drawable.food),
    )

    val dummyAnggaran = listOf(
        Anggaran(
            nama = "Food",
            total = 300000,
            sisa = 230000,
            Bulan = "Maret",
            imageRes = R.drawable.food
        ),
        Anggaran(
            nama = "Transportation",
            total = 300000,
            sisa = 270000,
            Bulan = "Maret",
            imageRes = R.drawable.transportation
        ),
        Anggaran(
            nama = "Clothes",
            total = 200000,
            sisa = 100000,
            Bulan = "Maret",
            imageRes = R.drawable.clothes
        )
    )
}