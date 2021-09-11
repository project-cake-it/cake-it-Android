package com.cakeit.cakitandroid.presentation.list.shoplist.filter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import kotlinx.android.synthetic.main.item_shop_size.view.*

class CakeShopPriceAdapter(var sizeAndrPrices : ArrayList<CakeSizeAndrPrice>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_size, parent, false)
        val viewHolder = CakeShopPriceViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return sizeAndrPrices.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sizeAndrPrice = sizeAndrPrices[position]
        val cakeShopTagViewHolder = holder as CakeShopPriceViewHolder

        cakeShopTagViewHolder.bind(sizeAndrPrice)
    }

    class CakeShopPriceViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val sizeName = view.tv_shop_size_type
        val sizePrice = view.tv_shop_size_price

        fun bind(sizeAndrPrice : CakeSizeAndrPrice) {
            sizeName.text = sizeAndrPrice.name
            var cakePriceDot = String.format("%,d", sizeAndrPrice.price) + "원"
            sizePrice.text = cakePriceDot
        }
    }
}