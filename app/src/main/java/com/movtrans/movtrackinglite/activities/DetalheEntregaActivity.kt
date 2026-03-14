package com.movtrans.movtrackinglite.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.model.Movimentacao
import com.movtrans.movtrackinglite.utils.TelefoneHelper

class DetalheEntregaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_entrega)

        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener { finish() }

        // Recebe o objeto Movimentacao (que serve para os dois tipos)
        val mov = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("movimentacao", Movimentacao::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("movimentacao") as? Movimentacao
        }

        if (mov != null) {
            val ehEntrega = mov.tipo == 1

            // 1. Ajustar Títulos e Labels dinamicamente
            val tvTituloTela = findViewById<TextView>(R.id.tvTituloTela)
            val tvLabelCarga = findViewById<TextView>(R.id.tvLabelCarga)
            val btnConfirmar = findViewById<Button>(R.id.btnEntrega)

            if (ehEntrega) {
                tvTituloTela.text = "Detalhes da Entrega"
                tvLabelCarga.text = "DADOS DO DESTINATÁRIO"
                btnConfirmar.text = "FINALIZAR ENTREGA"
            } else {
                tvTituloTela.text = "Detalhes da Coleta"
                tvLabelCarga.text = "DADOS DO REMETENTE"
                btnConfirmar.text = "FINALIZAR COLETA"
                // Usa cor direta via String Hexadecimal
                btnConfirmar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F57C00"))
            }

            // 2. Preencher Dados Principais
            findViewById<TextView>(R.id.tvDestinatario).text = mov.razao
            findViewById<TextView>(R.id.tvEndereco).text = "${mov.endereco}, ${mov.numero} - ${mov.bairro}\n${mov.cidade}"

            // Dados do Documento (ndoc)
            findViewById<TextView>(R.id.tvCteNumero).text = mov.ndoc

            // 3. Lógica para Notas Fiscais (Pega a primeira nota para exibir no resumo)
            if (mov.notasFiscais.isNotEmpty()) {
                val primeiraNota = mov.notasFiscais[0]
                findViewById<TextView>(R.id.tvNfeNumero).text = "Nº ${primeiraNota.numeroNota}"
                findViewById<TextView>(R.id.tvCteChave).text = primeiraNota.chaveNota
            } else {
                findViewById<TextView>(R.id.tvNfeNumero).text = "Sem Notas Vinculadas"
                findViewById<TextView>(R.id.tvCteChave).text = "Chave não disponível"
            }

            // 4. Botão Ligar
            val btnLigar = findViewById<TextView>(R.id.btnLigarAcao)
            btnLigar.text = "Ligar: ${mov.telefone}"
            btnLigar.setOnClickListener {
                TelefoneHelper.fazerLigacao(this, mov.telefone)
            }

            // 5. Navegação dos Botões de Ação
            btnConfirmar.setOnClickListener {
                val intent = Intent(this, ConfirmacaoActivity::class.java)
                intent.putExtra("movimentacao", mov)
                startActivity(intent)
            }

            findViewById<Button>(R.id.btnOcorrencia).setOnClickListener {
                val intent = Intent(this, OcorrenciaActivity::class.java)
                intent.putExtra("movimentacao", mov)
                startActivity(intent)
            }
        }
    }
}