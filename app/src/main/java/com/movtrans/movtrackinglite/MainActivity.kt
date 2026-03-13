package com.movtrans.movtrackinglite.activities

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.utils.PreferencesHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSOES = arrayOf(Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferencesHelper.isRegistered(this)) {
            startActivity(Intent(this, RomaneioActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        requestPermissions(PERMISSOES, 123)

        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        findViewById<TextView>(R.id.tvIdDisplay).text = "ID: $deviceId"

        findViewById<Button>(R.id.btnCopy).setOnClickListener {
            val clip = ClipData.newPlainText("ID", deviceId)
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
            Toast.makeText(this, "ID Copiado", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            val emp = findViewById<EditText>(R.id.etEmpresa).text.toString()
            if (emp.isNotEmpty()) {
                PreferencesHelper.saveRegistration(this, emp, deviceId)
                startActivity(Intent(this, RomaneioActivity::class.java))
                finish()
            }
        }
    }
}