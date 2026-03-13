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

class OcorrenciaActivity : AppCompatActivity() {

    private val REQ_FOTO_OCORRENCIA = 303
    private lateinit var tvNomeArquivo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencia)

        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener { finish() }

        val tvDataHora = findViewById<TextView>(R.id.tvDataHoraOcorrencia)
        val spinner = findViewById<Spinner>(R.id.spinnerMotivos)
        val layoutFoto = findViewById<View>(R.id.layoutFotoOcorrencia)
        val btnFoto = layoutFoto.findViewById<TextView>(R.id.btnCapturarFoto)
        tvNomeArquivo = layoutFoto.findViewById<TextView>(R.id.tvNomeArquivo)

        tvDataHora.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val motivos = arrayOf("Selecione o motivo...", "Cliente Ausente", "Endereço não localizado", "Recusado", "Veículo Quebrado", "Outros")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, motivos)

        btnFoto.setOnClickListener {
            CameraHelper.checkAndOpenCamera(this, REQ_FOTO_OCORRENCIA)
        }

        findViewById<Button>(R.id.btnSalvarOcorrencia).setOnClickListener {
            if (spinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Selecione o motivo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ocorrência registrada!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CameraHelper.PERMISSION_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraHelper.checkAndOpenCamera(this, REQ_FOTO_OCORRENCIA)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                    CameraHelper.mostrarAvisoConfiguracoes(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_FOTO_OCORRENCIA && CameraHelper.validarResultado(resultCode)) {
            val ts = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            tvNomeArquivo.text = "✅ Foto capturada às $ts"
            tvNomeArquivo.setTextColor(Color.parseColor("#2E7D32"))
        }
    }
}