package com.movtrans.movtrackinglite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.movtrans.movtrackinglite.R
import com.movtrans.movtrackinglite.model.Movimentacao

class RomaneioAdapter(
    private var lista: List<Movimentacao>,
    private val clique: (Movimentacao) -> Unit
) : RecyclerView.Adapter<RomaneioAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tag: TextView = v.findViewById(R.id.tvTagTipo)
        val razao: TextView = v.findViewById(R.id.tvRazaoSocial)
        val endereco: TextView = v.findViewById(R.id.tvEnderecoCompleto)
        val doc: TextView = v.findViewById(R.id.tvDocumento)
        val notas: TextView = v.findViewById(R.id.tvInfoNotas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_romaneio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.razao.text = item.razao.trim().uppercase()
        holder.doc.text = "Doc: #${item.ndoc}"
        holder.endereco.text = "${item.endereco}, ${item.numero} - ${item.cidade}"

        // Quantidade de notas
        holder.notas.text = "${item.notasFiscais.size} Nota(s) Fiscal(is)"

        // Diferenciação visual Entrega (Tipo 1) vs Coleta (Outros)
        if (item.tipo == 1) {
            holder.tag.text = "ENTREGA"
            holder.tag.setBackgroundResource(R.drawable.bg_tag_entrega)
        } else {
            holder.tag.text = "COLETA"
            holder.tag.setBackgroundResource(R.drawable.bg_tag_coleta)
        }

        holder.itemView.setOnClickListener { clique(item) }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Movimentacao>) {
        this.lista = novaLista
        notifyDataSetChanged()
    }
}