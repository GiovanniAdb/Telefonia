package com.example.telefonia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener los valores guardados en las SharedPreferences
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val savedPhoneNumber = sharedPref.getString(getString(R.string.phone_number_key), null)
        val savedReplyText = sharedPref.getString(getString(R.string.reply_text_key), null)

        // Asignar los valores guardados a las vistas
        phoneInput.setText(savedPhoneNumber)
        replyInput.setText(savedReplyText)

        // Configurar el listener de cambios de estado en llamadas telefónicas
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)

                if (state == TelephonyManager.CALL_STATE_RINGING && phoneNumber == savedPhoneNumber) {
                    // Si el número que está llamando coincide con el número guardado, enviar respuesta automática
                    val replyIntent = Intent(applicationContext, AutoReplyService::class.java)
                    replyIntent.putExtra("replyText", savedReplyText)
                    startService(replyIntent)
                }
            }
        }

        // Configurar los botones de la interfaz de usuario
        saveButton.setOnClickListener {
            saveConfig()
        }

        startButton.setOnClickListener {
            startService()
        }

        stopButton.setOnClickListener {
            stopService()
        }
    }

    private fun saveConfig() {
        // Obtener los valores de las vistas
        val phoneNumber = phoneInput.text.toString()
        val replyText = replyInput.text.toString()

        // Guardar los valores en las SharedPreferences
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(getString(R.string.phone_number_key), phoneNumber)
            putString(getString(R.string.reply_text_key), replyText)
            apply()
        }
    }

    private fun startService() {
        // Configurar el listener de llamadas telefónicas
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)

                if (state == TelephonyManager.CALL_STATE_RINGING && phoneNumber == phoneInput.text.toString()) {
                    // Si el número que está llamando coincide con el número ingresado, enviar respuesta automática
                    val replyIntent = Intent(applicationContext, AutoReplyService::class.java)
                    replyIntent.putExtra("replyText", replyInput.text.toString())
                    startService(replyIntent)
                }
            }
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

        // Iniciar el servicio de respuesta automática
        val intent = Intent(this, AutoReplyService::class.java)
        startService(intent)
    }

    private fun stopService() {
        // Detener el listener de llamadas telefónicas
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)

        // Detener el servicio de respuesta automática
        val intent = Intent(this, AutoReplyService::class.java)
        stopService(intent)
    }
}