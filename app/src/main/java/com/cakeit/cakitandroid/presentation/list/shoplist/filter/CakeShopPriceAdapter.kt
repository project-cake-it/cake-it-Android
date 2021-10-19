package com.cakeit.cakitandroid.presentation.list.shoplist.filter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListAdapter
import kotlinx.android.synthetic.main.item_shop_size.view.*

class CakeShopPriceAdapter(var sizeAndrPrices : ArrayList<CakeSizeAndrPrice>, cakeListIndex : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var cakeListIndex = cakeListIndex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_size, parent, false)
        val viewHolder = CakeShopPriceViewHolder(view, cakeListIndex)
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

    class CakeShopPriceViewHolder(view : View, cakeListIndex: Int) : RecyclerView.ViewHolder(view) {
        val sizeName = view.tv_shop_size_type
        val sizePrice = view.tv_shop_size_price
        
        init {
            view.setOnClickListener {
                ShopListAdapter.shopListAdapter.onItemClick.onShopItemClick(cakeListIndex)
            }
        }

        fun bind(sizeAndrPrice : CakeSizeAndrPrice) {
            sizeName.text = sizeAndrPrice.name
            var cakePriceDot = String.format("%,d", sizeAndrPrice.price) + "원"
            sizePrice.text = cakePriceDot
        }
    }
}