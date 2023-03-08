package com.example.telefonia
import android.content.*
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        //targetNumber.setText(prefs.getString("targetNumber", ""))
        //autoResponse.setText(prefs.getString("autoResponse", ""))

        Toast.makeText(this, targetNumber.text.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "---"+autoResponse.text.toString(), Toast.LENGTH_SHORT).show();
        Log.d("esto es el mensaje de texto que se manda", autoResponse.toString())
        //targetNumber.setText("4171400040")
        //autoResponse.setText("que onda")

    val saveConfigButton = findViewById<Button>(R.id.buttonSave)
    saveConfigButton.setOnClickListener {
        // Obtener el número de teléfono y el mensaje de respuesta automáticos ingresados por el usuario
        // Guardarlos en las preferencias compartidas usando la clase SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("targetNumber", targetNumber.text.toString())
        editor.putString("responseMessage", autoResponse.text.toString())
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

        //val smsManager = SmsManager.getDefault()
        //smsManager.sendTextMessage("+52" + targetNumber.text.toString(), null, autoResponse.text.toString(), null, null)

        val intent = Intent(this, MyService::class.java)
        //stopService(intent)

        //Toast.makeText(this, "number: "+targetNumber+", autoResponse: "+autoResponse, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, targetNumber.text.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "---"+autoResponse.text.toString(), Toast.LENGTH_SHORT).show();
    }
}
}