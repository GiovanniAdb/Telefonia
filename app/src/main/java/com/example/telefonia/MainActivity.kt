package com.example.telefonia

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.Config

class MainActivity : AppCompatActivity() {
    private lateinit var editTextNumber: EditText
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a los elementos de la interfaz de usuario
        editTextNumber = findViewById(R.id.editTextNumber)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSave = findViewById(R.id.buttonSave)
        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)

        // Configurar el evento onClick del botón "Guardar"
        buttonSave.setOnClickListener {
            Config.saveConfig(this, editTextNumber.text.toString(), editTextMessage.text.toString())
        }

        // Configurar el evento onClick del botón "Iniciar servicio"
        buttonStart.setOnClickListener {
            // Verificar si se tienen los permisos necesarios
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Iniciar el servicio de respuesta automática
                startService()
            } else {
                // Solicitar los permisos necesarios
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS),
                    0
                )
            }
        }

        // Configurar el evento onClick del botón "Detener servicio"
        buttonStop.setOnClickListener {
            // Detener el servicio de respuesta automática
            stopService()
        }

        // Cargar la configuración previa
        val config = Config.loadConfig(this)
        editTextNumber.setText(config.number)
        editTextMessage.setText(config.message)
    }

    private fun startService() {
        // Obtener el número y el mensaje de respuesta automática
        val config = Config.loadConfig(this)
        val number = config.number
        val message = config.message

        // Registrar el listener de llamadas telefónicas
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                super.onCallStateChanged(state, incomingNumber)

                // Verificar si la llamada es entrante
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Verificar si el número entrante coincide con el número configurado
                    if (incomingNumber == number) {
                        // Enviar la respuesta automática
                        SmsSender.sendSms(this@MainActivity, number, message)
                    }
                }
            }
            // Configurar el listener de llamadas telefónicas
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

            // Iniciar el servicio de respuesta automática
            val intent = Intent(this, AutoReplyService::class.java)
            startService(intent)
        }
    }

        private fun stopService() {
            // Detener el listener de llamadas telefónicas
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.listen(null, PhoneStateListener.LISTEN_NONE)

            // Detener el servicio de respuesta automática
            val intent = Intent(this, AutoReplyService::class.java)
            stopService(intent)
        }
    }
