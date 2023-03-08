package com.example.telefonia

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.EditText

class MyBroadcastReceiver : BroadcastReceiver() {

    private lateinit var prefs: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        // Obtener el número de teléfono entrante
        val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val phoneNumber = telephonyManager.line1Number
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Request the permission
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.SEND_SMS), 1)
                return
            }
        }

        // Obtener el número de teléfono de destino y mensaje de respuesta de las preferencias
        prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val targetNumber = prefs.getString("targetNumber", "")
        val autoResponse = prefs.getString("autoResponse", "")

        // Verificar si el número entrante es el número de destino y enviar un mensaje de respuesta automático
            val smsManager = SmsManager.getDefault()

            smsManager.sendTextMessage("+52" + phoneNumber, null, autoResponse, null, null)

    }
}
