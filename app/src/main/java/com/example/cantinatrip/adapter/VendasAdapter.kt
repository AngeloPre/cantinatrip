package com.example.cantinatrip.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cantinatrip.R
import com.example.cantinatrip.model.Venda

class VendasAdapter(
    private val vendas: List<Venda>,
    private val context: Context,
    private val click: (Venda) -> Unit,
    private val previewProvider: (Venda) -> String = { "" }
) : RecyclerView.Adapter<VendasAdapter.VendasViewHolder>() {

    inner class VendasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAvatar = itemView.findViewById<ImageView>(R.id.ivAvatarComprador)
        private val tvNome = itemView.findViewById<TextView>(R.id.tvNomeComprador)
        private val tvPreview = itemView.findViewById<TextView>(R.id.tvPreviewConsumo)
        private val tvValor = itemView.findViewById<TextView>(R.id.tvValorTotal)

        fun bind(venda: Venda) {
            tvNome.text = venda.nomeComprador
            tvPreview.text = previewProvider(venda)
            tvValor.text = "R$ %.2f".format(venda.valorTotal)
            ivAvatar.setImageResource(resolveAvatar(venda.nomeComprador))
            itemView.setOnClickListener { click(venda) }
        }

        private fun resolveAvatar(nome: String): Int {
            val inicial = nome.trim().firstOrNull()?.lowercaseChar() ?: return 0
            if (inicial !in 'a'..'z') return 0
            return context.resources.getIdentifier(
                inicial.toString(), "drawable", context.packageName
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendasViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.recycler_view_item, parent, false
        )
        return VendasViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendasViewHolder, position: Int) {
        holder.bind(vendas[position])
    }

    override fun getItemCount(): Int = vendas.size
}
