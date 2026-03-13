package com.movtrans.movtrackinglite.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.utils.CameraHelper
import java.text.SimpleDateFormat
import java.util.*

class ConfirmacaoActivity : AppCompatActivity() {

    private val REQ_FOTO_CONFIRMACAO = 200
    private lateinit var tvNomeArquivo: TextView
    private var fotoCapturada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacao)

        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener { finish() }

        val tvDataHora = findViewById<TextView>(R.id.tvDataHora)
        val etRecebedor = findViewById<EditText>(R.id.etRecebedor)
        val layoutFoto = findViewById<View>(R.id.layoutFotoConfirmacao)
        val btnFoto = layoutFoto.findViewById<TextView>(R.id.btnCapturarFoto)
        tvNomeArquivo = layoutFoto.findViewById<TextView>(R.id.tvNomeArquivo)

        tvDataHora.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        btnFoto.setOnClickListener {
            CameraHelper.checkAndOpenCamera(this, REQ_FOTO_CONFIRMACAO)
        }

        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            val nome = etRecebedor.text.toString().trim()
            when {
                nome.length < 3 -> Toast.makeText(this, "Informe o recebedor", Toast.LENGTH_SHORT).show()
                !fotoCapturada -> Toast.makeText(this, "A foto é obrigatória", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this, "Entrega finalizada!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CameraHelper.PERMISSION_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraHelper.checkAndOpenCamera(this, REQ_FOTO_CONFIRMACAO)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                    CameraHelper.mostrarAvisoConfiguracoes(this)
                } else {
                    Toast.makeText(this, "Permissão necessária para a foto.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_FOTO_CONFIRMACAO && CameraHelper.validarResultado(resultCode)) {
            fotoCapturada = true
            val ts = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            tvNomeArquivo.text = "✅ Foto capturada às $ts"
            tvNomeArquivo.setTextColor(Color.parseColor("#2E7D32"))
        }
    }
}