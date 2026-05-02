package com.example.praktam_2417051017.network

import com.example.praktam_2417051017.model.KategoriResponse
import retrofit2.http.GET

interface ApiService {

    @GET("daftar_kategori.json")
    suspend fun getKategori(): KategoriResponse
}