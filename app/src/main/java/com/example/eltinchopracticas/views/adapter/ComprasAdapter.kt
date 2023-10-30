package com.example.eltinchopracticas.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.models.compras
import com.squareup.picasso.Picasso

class ComprasAdapter(private val context: Context, var clickListenerComprasAdapter: OnCompraItemClickLitener):RecyclerView.Adapter<ComprasAdapter.ViewHolder>() {
    private var compraslist= mutableListOf<compras>()
    //funcion que actualiza la base de datos
    fun setListData(data:MutableList<compras>){
        compraslist=data
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i:Int): ViewHolder {
        val v= LayoutInflater.from(viewGroup.context).inflate(R.layout.card_view_compras, viewGroup, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var compra=compraslist[i]
        viewHolder.binWew(compra,clickListenerComprasAdapter)
    }

    override fun getItemCount(): Int {
        return compraslist.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun binWew(compra: compras, action:OnCompraItemClickLitener){
            Picasso.with(context).load(compra.imagen).into(itemView.findViewById<ImageView>(R.id.image))
            itemView.findViewById<TextView>(R.id.title).text=compra.titulo
            itemView.findViewById<TextView>(R.id.precio).text= compra.precio
            val btneliminar=itemView.findViewById<ImageButton>(R.id.eliminarShop)
            btneliminar.setOnClickListener{
                action.onItemClick(compra,adapterPosition)
            }
        }

    }
    interface OnCompraItemClickLitener {
        fun onItemClick(compra: compras, position: Int)
    }
}