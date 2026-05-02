package com.example.praktam_2417051017.model

import com.google.gson.annotations.SerializedName

data class Kategori(

    @SerializedName("nama")
    val nama: String,

    @SerializedName("total")
    val total: Int,

    @SerializedName("pengeluaran")
    val pengeluaran: Int,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("image_url")
    val imageUrl: String
)