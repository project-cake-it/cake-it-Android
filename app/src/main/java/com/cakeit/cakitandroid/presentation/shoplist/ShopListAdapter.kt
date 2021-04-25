package com.cakeit.cakitandroid.presentation.shoplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import kotlinx.android.synthetic.main.item_shop_list.view.*

class ShopListAdapter(val shopListItem : ArrayList<CakeShopData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var shopListItems : List<CakeShopData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_list, parent, false)
        val viewHolder = ShopListViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return shopListItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cakeShop = shopListItem[position]
        val shopListViewHolder = holder as ShopListViewHolder
        shopListViewHolder.bind(cakeShop)
    }

    fun addItem(shopItem: CakeShopData) {
        shopListItem.add(shopItem)
    }

    fun setShopListItems(shopItems: List<CakeShopData>) {
        this.shopListItems = shopItems
        notifyDataSetChanged()
    }

    class ShopListViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val cakeShopName = view.tv_shop_name_item_shop
        val cakeShopAddress = view.tv_shop_address_item_shop

        fun bind(cakeShop : CakeShopData) {
            cakeShopName.text = cakeShop.shopName
            cakeShopAddress.text = cakeShop.shopAddress
        }
    }
}