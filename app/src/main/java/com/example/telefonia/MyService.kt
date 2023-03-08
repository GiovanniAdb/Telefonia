package com.example.telefonia

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Aquí se realiza el código para el servicio
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Si no se va a utilizar un servicio enlazado, se puede devolver null
        return null
    }

    override fun onDestroy() {
        // Aquí se realiza el código para detener el servicio
        super.onDestroy()
    }
}
