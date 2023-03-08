package com.example.telefonia

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
class MyBroadcastReceiver : BroadcastReceiver() {

    private lateinit var prefs: SharedPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                if (phoneNumber == getAutoReplyNumber(context)) {
                    sendAutoReplySms(context)
                }
            }
        }
    }
    private fun sendAutoReplySms(context: Context) {
        val smsManager = SmsManager.getDefault()
        val autoReplyText = getAutoReplyText(context)
        val autoReplyNumber = getAutoReplyNumber(context)
        smsManager.sendTextMessage("+52"+autoReplyNumber, null, autoReplyText, null, null)
    }

    private fun getAutoReplyNumber(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("targetNumber", null)
    }

    private fun getAutoReplyText(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("responseMessage", null)
    }

    /**
    override fun onReceive(context: Context?, intent: Intent?) {
        // Obtener el número de teléfono entrante
        val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val phoneNumber = telephonyManager.line1Number

        // Obtener el número de teléfono de destino y mensaje de respuesta de las preferencias
        prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val targetNumber = prefs.getString("targetNumber", "")
        val autoResponse = prefs.getString("autoResponse", "")


        //val toast = Toast.makeText(targetNumber,autoResponse, Toast.LENGTH_LONG)
        // toast.show()
        //Toast.show(this, targetNumber.text.toString(), Toast.LENGTH_SHORT).show();

         Log.d(", targetNumber", targetNumber.toString())
         Log.d(", phoneNumber", phoneNumber.toString())
         Log.d(", mensaje de responder en receiver", autoResponse.toString())

        // Verificar si el número entrante es el número de destino y enviar un mensaje de respuesta automático
        //if(phoneNumber!=null && phoneNumber == targetNumber){
        //    val smsManager = SmsManager.getDefault()
          //  smsManager.sendTextMessage("+52" + targetNumber, null, autoResponse, null, null)
        //}
        //else{
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage("+524661606569", null, "No entra aqui", null, null)
    //    }
    }
    */
}
