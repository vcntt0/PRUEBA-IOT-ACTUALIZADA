package com.example.pruebaiot

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var isConectado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val btnConnectBluetooth = findViewById<Button>(R.id.btnConnectBluetooth)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnSimulateData = findViewById<Button>(R.id.btnSimulateData)

        btnConnectBluetooth.setOnClickListener {
            isConectado = true
            tvStatus.text = "Estado: Conectado al Basurero_Principal"
            Toast.makeText(this, "Bluetooth conectado al basurero", Toast.LENGTH_SHORT).show()
        }

        btnSimulateData.setOnClickListener {
            if (isConectado) {
                val nivelLlenado = (0..100).random()

                val mensajeBasurero = "Proximidad detectada: Tapa abierta. Nivel de basura: $nivelLlenado%"

                dbHelper.insertSensorData("Basurero_Principal", mensajeBasurero)
                Toast.makeText(this, mensajeBasurero, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Primero debes conectar el Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }
}