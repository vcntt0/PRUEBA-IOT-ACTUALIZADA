package com.example.pruebaiot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "IoTAppDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)")
        db.execSQL("CREATE TABLE sensor_data (id INTEGER PRIMARY KEY AUTOINCREMENT, device_name TEXT, data_value TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)")

        val adminPassword = hashPassword("admin")
        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', '$adminPassword')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS sensor_data")
        onCreate(db)
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun checkUser(username: String, pass: String): Boolean {
        val db = this.readableDatabase
        val hashedPass = hashPassword(pass)
        val cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", arrayOf(username, hashedPass))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertSensorData(deviceName: String, value: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("device_name", deviceName)
        cv.put("data_value", value)
        db.insert("sensor_data", null, cv)
    }
}