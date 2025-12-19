package com.ebc.catalogofundas.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object BanxicoService {

    private val client = OkHttpClient()


    private const val SERIES_ID = "SF43718"

    suspend fun obtenerTipoCambio(token: String): String {
        // Endpoint “oportuno” (dato más reciente)
        val url = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/$SERIES_ID/datos/oportuno?token=$token"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Accept", "application/json")
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Error Banxico: ${response.code} ${response.message}")
        }

        val body = response.body?.string().orEmpty()
        val json = JSONObject(body)


        val series = json.getJSONObject("bmx").getJSONArray("series")
        val datos = series.getJSONObject(0).getJSONArray("datos")

        val dato = datos.getJSONObject(0).getString("dato")
        val fecha = datos.getJSONObject(0).getString("fecha")

        return "$dato MXN (Fecha: $fecha)"
    }
}
