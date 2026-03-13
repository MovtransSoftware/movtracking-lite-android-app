package com.movtrans.movtrackinglite.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CameraHelper {

    const val PERMISSION_CAMERA_CODE = 101

    fun checkAndOpenCamera(activity: Activity, requestCode: Int) {
        val permission = Manifest.permission.CAMERA

        when {
            // 1. Já tem permissão: Abre direto
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                executarAberturaCamera(activity, requestCode)
            }

            // 2. Negou antes: Explica e pede de novo
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                mostrarAvisoEPedirPermissao(activity)
            }

            // 3. Primeira vez ou bloqueado permanentemente
            else -> {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_CAMERA_CODE)
            }
        }
    }

    private fun mostrarAvisoEPedirPermissao(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Acesso à Câmera")
            .setMessage("Para registrar a entrega ou ocorrência, o aplicativo precisa usar a câmera para capturar a foto do comprovante.")
            .setPositiveButton("Tentar Novamente") { _, _ ->
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA_CODE)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    fun mostrarAvisoConfiguracoes(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Câmera Bloqueada")
            .setMessage("O acesso à câmera está desativado. Para continuar, habilite a permissão nas configurações do sistema.")
            .setPositiveButton("Configurações") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton("Agora não", null)
            .show()
    }

    private fun executarAberturaCamera(activity: Activity, requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            Toast.makeText(activity, "Nenhum aplicativo de câmera encontrado.", Toast.LENGTH_SHORT).show()
        }
    }

    fun validarResultado(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}