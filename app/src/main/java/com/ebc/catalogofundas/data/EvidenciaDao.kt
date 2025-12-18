package com.ebc.catalogofundas.data

import EvidenciaPantalla
import android.content.ContentValues
import android.content.Context
import android.database.Cursor


class EvidenciaDao(context: Context) {

    private val dbHelper = AppDatabaseHelper(context)

    fun insertar(e: EvidenciaPantalla): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre_pantalla", e.nombrePantalla)
            put("descripcion", e.descripcion)
            put("ruta_imagen", e.rutaImagen)
            put("animacion", e.animacion)
            put("fecha", e.fecha)
        }
        return db.insert("EvidenciaPantalla", null, values)
    }

    fun obtenerTodas(): List<EvidenciaPantalla> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM EvidenciaPantalla ORDER BY fecha DESC",
            null
        )
        return cursor.use { c -> cursorToList(c) }
    }

    private fun cursorToList(c: Cursor): List<EvidenciaPantalla> {
        val list = mutableListOf<EvidenciaPantalla>()
        while (c.moveToNext()) {
            list.add(
                EvidenciaPantalla(
                    idEvidencia = c.getLong(c.getColumnIndexOrThrow("id_evidencia")),
                    nombrePantalla = c.getString(c.getColumnIndexOrThrow("nombre_pantalla")),
                    descripcion = c.getString(c.getColumnIndexOrThrow("descripcion")),
                    rutaImagen = c.getString(c.getColumnIndexOrThrow("ruta_imagen")),
                    animacion = c.getString(c.getColumnIndexOrThrow("animacion")),
                    fecha = c.getLong(c.getColumnIndexOrThrow("fecha"))
                )
            )
        }
        return list
    }
}
