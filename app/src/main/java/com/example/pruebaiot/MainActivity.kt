package com.example.pruebaiot

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val btnConnect = findViewById<Button>(R.id.btnConnectBluetooth)
        val btnSimulate = findViewById<Button>(R.id.btnSimulateData)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnConnect.setOnClickListener {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth no soportado", Toast.LENGTH_SHORT).show()
            } else {
                if (!bluetoothAdapter.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, 1)
                } else {
                    isConnected = true
                    tvStatus.text = "Estado: Conectado a Dispositivo_IoT_1"
                    Toast.makeText(this, "Conectado exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSimulate.setOnClickListener {
            if (isConnected) {
                val simulatedTemp = (20..35).random().toString() + "°C"
                dbHelper.insertSensorData("Dispositivo_IoT_1", simulatedTemp)
                Toast.makeText(this, "Dato guardado: $simulatedTemp", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Conecte el dispositivo primero", Toast.LENGTH_SHORT).show()
            }
        }
    }
}