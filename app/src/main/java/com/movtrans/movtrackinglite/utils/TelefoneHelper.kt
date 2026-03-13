package com.movtrans.movtrackinglite.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object TelefoneHelper {
    fun fazerLigacao(activity: Activity, telefone: String?) {
        if (telefone.isNullOrBlank()) {
            Toast.makeText(activity, "Telefone não disponível", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefone")
            activity.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(activity, "Erro ao abrir o discador", Toast.LENGTH_SHORT).show()
        }
    }
}