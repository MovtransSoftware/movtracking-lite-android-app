package com.movtrans.movtrackinglite.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.model.Entrega
import com.movtrans.movtrackinglite.utils.TelefoneHelper

class DetalheEntregaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_entrega)

        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        val entrega = intent.getSerializableExtra("entrega") as? Entrega

        if (entrega != null) {
            // Dados Destinatário
            findViewById<TextView>(R.id.tvDestinatario).text = entrega.nome
            findViewById<TextView>(R.id.tvEndereco).text = entrega.endereco

            // Dados CT-e (Agora as referências abaixo vão funcionar)
            findViewById<TextView>(R.id.tvCteNumero).text = entrega.cteNumero ?: "Não informado"
            findViewById<TextView>(R.id.tvCteChave).text = entrega.cteChave ?: "Chave não disponível"

            // Dados NF-e
            findViewById<TextView>(R.id.tvNfeNumero).text = entrega.nfeNumero ?: "Não informado"

            // Botão Ligar Dinâmico
            val layoutLigar = findViewById<View>(R.id.layoutLigarCarga)
            val btnLigar = layoutLigar.findViewById<TextView>(R.id.btnLigarAcao)

            btnLigar.text = "Ligar: ${entrega.telefone ?: "N/A"}"
            btnLigar.setOnClickListener {
                TelefoneHelper.fazerLigacao(this, entrega.telefone)
            }
        }

        // Navegação
        findViewById<Button>(R.id.btnEntrega).setOnClickListener {
            val intent = Intent(this, ConfirmacaoActivity::class.java)
            intent.putExtra("entrega", entrega)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOcorrencia).setOnClickListener {
            val intent = Intent(this, OcorrenciaActivity::class.java)
            intent.putExtra("entrega", entrega)
            startActivity(intent)
        }
    }
}