package com.movtrans.movtrackinglite.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.adapter.EntregaAdapter
import com.movtrans.movtrackinglite.model.Entrega

class RomaneioActivity : AppCompatActivity() {

    private lateinit var rvEntregas: RecyclerView
    private lateinit var btnAtualizar: ImageButton
    private var listaDeEntregas = mutableListOf<Entrega>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_romaneio)

        // 1. Inicializar componentes
        rvEntregas = findViewById(R.id.rvEntregas)
        btnAtualizar = findViewById(R.id.btnAtualizarRomaneio)

        // 2. Configurar RecyclerView
        rvEntregas.layoutManager = LinearLayoutManager(this)

        // 3. Carregar dados iniciais
        carregarDados()

        // 4. Configurar clique do botão atualizar
        btnAtualizar.setOnClickListener {
            // Efeito visual de rotação no ícone (opcional, mas profissional)
            val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh)
            btnAtualizar.startAnimation(anim)

            Toast.makeText(this, "Atualizando romaneio...", Toast.LENGTH_SHORT).show()

            // Recarrega a lógica
            carregarDados()
        }
    }

    private fun carregarDados() {
        // Simulação: Aqui você limparia a lista e buscaria da API/Banco
        listaDeEntregas.clear()

        // Criando dados de teste conforme o novo modelo da classe Entrega
        val entrega1 = Entrega(
            id = "1",
            nome = "Mercado Central",
            endereco = "Rua das Flores, 123",
            telefone = "13999999999",
            cteNumero = "12345",
            cteChave = "35230112345678901234567890123456789012345678",
            nfeNumero = "5501",
            status = "Pendente"
        )

        val entrega2 = Entrega(
            id = "2",
            nome = "Loja do José",
            endereco = "Av. Brasil, 500 - Santos",
            telefone = "13988887777",
            cteNumero = "12346",
            cteChave = "35230112345678901234567890123456789012345679",
            nfeNumero = "5502",
            status = "Atrasado"
        )

        listaDeEntregas.add(entrega1)
        listaDeEntregas.add(entrega2)

        // 5. Configurar/Atualizar o Adapter
        val adapter = EntregaAdapter(listaDeEntregas) { entrega ->
            val intent = Intent(this, DetalheEntregaActivity::class.java)
            intent.putExtra("entrega", entrega)
            startActivity(intent)
        }

        rvEntregas.adapter = adapter
    }
}