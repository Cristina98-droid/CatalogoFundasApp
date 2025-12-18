package com.ebc.catalogofundas.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "catalogo_fundas.db"
        private const val DATABASE_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                modelo_telefono TEXT,
                foto_uri TEXT
            );
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Categoria (
                id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Funda (
                id_funda INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT,
                precio REAL NOT NULL,
                imagen_res TEXT,
                id_categoria INTEGER,
                FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
            );
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Favorito (
                id_favorito INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                id_funda INTEGER NOT NULL,
                fecha_agregado TEXT,
                FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
                FOREIGN KEY (id_funda) REFERENCES Funda(id_funda),
                UNIQUE (id_usuario, id_funda)
            );
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS EvidenciaPantalla (
                id_evidencia INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_pantalla TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                ruta_imagen TEXT NOT NULL,
                animacion TEXT,
                fecha INTEGER NOT NULL
            );
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (oldVersion < 3) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS EvidenciaPantalla (
                    id_evidencia INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre_pantalla TEXT NOT NULL,
                    descripcion TEXT NOT NULL,
                    ruta_imagen TEXT NOT NULL,
                    animacion TEXT,
                    fecha INTEGER NOT NULL
                );
                """
            )
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}

