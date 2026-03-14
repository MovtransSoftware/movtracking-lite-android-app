package com.movtrans.movtrackinglite.activities

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.adapter.RomaneioAdapter
import com.movtrans.movtrackinglite.model.EntregaColetaResponse
import com.movtrans.movtrackinglite.model.Movimentacao
import com.movtrans.movtrackinglite.service.MovtransClient
import com.movtrans.movtrackinglite.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RomaneioActivity : AppCompatActivity() {

    private lateinit var adapter: RomaneioAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var btnToggleEntrega: Button
    private lateinit var btnToggleColeta: Button
    private lateinit var btnAtualizar: ImageButton
    private lateinit var layoutVazio: LinearLayout
    private lateinit var tvMensagemVazia: TextView
    private lateinit var btnReset: Button
    private lateinit var rvRomaneio: RecyclerView

    private var listaCompleta = mutableListOf<Movimentacao>()
    private var entregasAtivas = true
    private var coletasAtivas = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_romaneio)

        setupViews()
        buscarDados()
    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)
        btnToggleEntrega = findViewById(R.id.btnToggleEntrega)
        btnToggleColeta = findViewById(R.id.btnToggleColeta)
        btnAtualizar = findViewById(R.id.btnAtualizarRomaneio)
        layoutVazio = findViewById(R.id.layoutVazio)
        tvMensagemVazia = findViewById(R.id.tvMensagemVazia)
        btnReset = findViewById(R.id.btnResetarFiltros)
        rvRomaneio = findViewById(R.id.rvRomaneio)

        adapter = RomaneioAdapter(mutableListOf()) { item ->
            val intent = Intent(this, DetalheEntregaActivity::class.java)
            intent.putExtra("movimentacao", item)
            startActivity(intent)
        }

        rvRomaneio.layoutManager = LinearLayoutManager(this)
        rvRomaneio.adapter = adapter

        btnAtualizar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh)
            btnAtualizar.startAnimation(anim)
            buscarDados()
        }

        btnToggleEntrega.setOnClickListener { entregasAtivas = !entregasAtivas; aplicarFiltros() }
        btnToggleColeta.setOnClickListener { coletasAtivas = !coletasAtivas; aplicarFiltros() }
        btnReset.setOnClickListener { entregasAtivas = true; coletasAtivas = true; aplicarFiltros() }
    }

    private fun aplicarFiltros() {
        updateButtonUI(btnToggleEntrega, entregasAtivas)
        updateButtonUI(btnToggleColeta, coletasAtivas)

        val qtdE = listaCompleta.count { it.tipo == 1 }
        val qtdC = listaCompleta.count { it.tipo != 1 }

        btnToggleEntrega.setCompoundDrawablesWithIntrinsicBounds(null, null, createCounterDrawable(qtdE, if (entregasAtivas) "#1976D2" else "#757575"), null)
        btnToggleColeta.setCompoundDrawablesWithIntrinsicBounds(null, null, createCounterDrawable(qtdC, if (coletasAtivas) "#F57C00" else "#757575"), null)

        // LÓGICA DE EXIBIÇÃO
        if (listaCompleta.isEmpty()) {
            // Caso 1: API não retornou nada
            rvRomaneio.visibility = View.GONE
            layoutVazio.visibility = View.VISIBLE
            tvMensagemVazia.text = "Não existem entregas ou coletas para exibir"
            btnReset.visibility = View.GONE
        } else if (!entregasAtivas && !coletasAtivas) {
            // Caso 2: Existem dados, mas os filtros estão desligados
            rvRomaneio.visibility = View.GONE
            layoutVazio.visibility = View.VISIBLE
            tvMensagemVazia.text = "Nenhum filtro selecionado"
            btnReset.visibility = View.VISIBLE
        } else {
            // Caso 3: Filtrar e mostrar lista
            val listaFiltrada = listaCompleta.filter { item ->
                val ehEntrega = item.tipo == 1
                (ehEntrega && entregasAtivas) || (!ehEntrega && coletasAtivas)
            }

            if (listaFiltrada.isEmpty()) {
                rvRomaneio.visibility = View.GONE
                layoutVazio.visibility = View.VISIBLE
                tvMensagemVazia.text = "Não há itens para este filtro"
                btnReset.visibility = View.VISIBLE
            } else {
                rvRomaneio.visibility = View.VISIBLE
                layoutVazio.visibility = View.GONE
                adapter.atualizarLista(listaFiltrada)
            }
        }
    }

    private fun createCounterDrawable(count: Int, textColor: String): Drawable {
        val size = (24 * resources.displayMetrics.density).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawOval(0f, 0f, size.toFloat(), size.toFloat(), paint)
        paint.color = Color.parseColor(textColor)
        paint.textSize = (12 * resources.displayMetrics.density)
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.CENTER
        val yPos = (canvas.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)
        canvas.drawText(count.toString(), (canvas.width / 2f), yPos, paint)
        return BitmapDrawable(resources, bitmap)
    }

    private fun updateButtonUI(button: Button, isActive: Boolean) {
        button.setPadding(30, 0, 30, 0)
        if (isActive) {
            button.setBackgroundResource(R.drawable.bg_toggle_active)
            button.setTextColor(Color.WHITE)
        } else {
            button.setBackgroundResource(R.drawable.bg_toggle_inactive)
            button.setTextColor(Color.parseColor("#757575"))
        }
    }

    private fun buscarDados() {
        val token = PreferencesHelper.getToken(this) ?: return
        progressBar.visibility = View.VISIBLE

        MovtransClient.instance.buscarEntregasColetas("Bearer $token").enqueue(object : Callback<EntregaColetaResponse> {
            override fun onResponse(call: Call<EntregaColetaResponse>, response: Response<EntregaColetaResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.success == true) {
                    val dados = response.body()?.dados
                    listaCompleta.clear()
                    dados?.entrega?.let { listaCompleta.addAll(it) }
                    dados?.coleta?.let { listaCompleta.addAll(it) }
                    aplicarFiltros()
                } else {
                    listaCompleta.clear()
                    aplicarFiltros() // Vai cair no "Não há dados"
                }
            }
            override fun onFailure(call: Call<EntregaColetaResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RomaneioActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }
}