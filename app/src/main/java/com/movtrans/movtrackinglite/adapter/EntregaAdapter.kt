package com.movtrans.movtrackinglite.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.model.Entrega

class EntregaAdapter(private val lista: List<Entrega>, private val clique: (Entrega) -> Unit) :
    RecyclerView.Adapter<EntregaAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val nome: TextView = v.findViewById(R.id.tvNome)
        val docs: TextView = v.findViewById(R.id.tvDocumentos)
        val end: TextView = v.findViewById(R.id.tvEndereco)
        val status: TextView = v.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entrega, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.nome.text = item.nome.uppercase()
        holder.docs.text = "CT-e: ${item.cteNumero ?: "---"} | NF: ${item.nfeNumero ?: "---"}"
        holder.end.text = item.endereco

        // Status Texto
        val statusTexto = item.status.uppercase()
        holder.status.text = statusTexto

        // Lógica de Cores Dinâmicas
        when {
            statusTexto.contains("PENDENTE") -> {
                holder.status.setTextColor(Color.parseColor("#E65100")) // Laranja Escuro
                holder.status.setBackgroundResource(R.drawable.bg_status_laranja)
            }
            statusTexto.contains("ENTREGUE") -> {
                holder.status.setTextColor(Color.parseColor("#2E7D32")) // Verde Escuro
                holder.status.setBackgroundResource(R.drawable.bg_status_verde)
            }
            statusTexto.contains("ATRASADO") -> {
                holder.status.setTextColor(Color.parseColor("#C62828")) // Vermelho Escuro
                holder.status.setBackgroundResource(R.drawable.bg_status_vermelho)
            }
//            else -> {
//                holder.status.setTextColor(Color.parseColor("#616161")) // Cinza padrão
//                holder.status.setBackgroundResource(R.drawable.bg_status_pendente)
//            }
        }

        holder.itemView.setOnClickListener { clique(item) }
    }

    override fun getItemCount() = lista.size
}