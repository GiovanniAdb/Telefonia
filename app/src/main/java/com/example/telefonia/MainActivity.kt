package com.example.telefonia
import android.Manifest
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
class MainActivity : AppCompatActivity() {

    private lateinit var targetNumber: EditText
    private lateinit var autoResponse: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetNumber = findViewById(R.id.editTextPhone)
        autoResponse = findViewById(R.id.editTextMessage)

        // Cargar las preferencias guardadas
        val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        targetNumber.setText(prefs.getString("targetNumber", ""))
        autoResponse.setText(prefs.getString("autoResponse", ""))

    val saveConfigButton = findViewById<Button>(R.id.buttonSave)
    saveConfigButton.setOnClickListener {
        // Obtener el número de teléfono y el mensaje de respuesta automáticos ingresados por el usuario
        // Guardarlos en las preferencias compartidas usando la clase SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("targetNumber", targetNumber.toString())
        editor.putString("responseMessage", autoResponse.toString())
        editor.apply()
        Toast.makeText(this, "¡La configuracion se ha guardado!", Toast.LENGTH_SHORT).show();
    }

    val startServiceButton = findViewById<Button>(R.id.buttonStart)
    startServiceButton.setOnClickListener {
        // Iniciar el servicio MyService usando la función startService()
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        Toast.makeText(this, "¡El servicio se ha iniciado!", Toast.LENGTH_SHORT).show();
    }

    val stopServiceButton = findViewById<Button>(R.id.buttonStop)
    stopServiceButton.setOnClickListener {
        // Detener el servicio MyService usando la función stopService()
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
        Toast.makeText(this, "¡El servicio se ha detenido!", Toast.LENGTH_SHORT).show();
    }
}
}