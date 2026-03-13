package com.movtrans.movtrackinglite.activities

import android.Manifest
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.service.MovtransClient
import com.movtrans.movtrackinglite.model.LoginRequest
import com.movtrans.movtrackinglite.model.LoginResponse
import com.movtrans.movtrackinglite.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val PERMISSOES = arrayOf(Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)

    private val apiService = MovtransClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferencesHelper.isRegistered(this)) {
            irParaRomaneio()
            return
        }
        setContentView(R.layout.activity_main)

        requestPermissions(PERMISSOES, 123)

        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val tvIdDisplay = findViewById<TextView>(R.id.tvIdDisplay)
        val etEmpresa = findViewById<EditText>(R.id.etEmpresa)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        tvIdDisplay.text = "ID: $deviceId"

        findViewById<Button>(R.id.btnCopy).setOnClickListener {
            val clip = ClipData.newPlainText("ID", deviceId)
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
            Toast.makeText(this, "ID Copiado", Toast.LENGTH_SHORT).show()
        }

        btnSalvar.setOnClickListener {
            val emp = etEmpresa.text.toString().trim()

            if (emp.isNotEmpty()) {
                btnSalvar.text = ""
                btnSalvar.isEnabled = false
                progressBar.visibility = View.VISIBLE

                val loginData = LoginRequest(aliases = emp, imei = deviceId)

                apiService.realizarLogin(loginData).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        resetarBotao(btnSalvar, progressBar)

                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            val token = loginResponse?.dados?.token

                            if (loginResponse?.success == true && token != null) {
                                PreferencesHelper.saveRegistration(this@MainActivity, emp, deviceId, token)
                                irParaRomaneio()
                            } else {
                                val msg = loginResponse?.message ?: "Dados inválidos"
                                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Erro no servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        resetarBotao(btnSalvar, progressBar)
                        Toast.makeText(this@MainActivity, "Falha de conexão: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Informe o nome da empresa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetarBotao(btn: Button, progress: ProgressBar) {
        btn.text = "REGISTRAR CELULAR"
        btn.isEnabled = true
        progress.visibility = View.GONE
    }

    private fun irParaRomaneio() {
        startActivity(Intent(this, RomaneioActivity::class.java))
        finish()
    }
}