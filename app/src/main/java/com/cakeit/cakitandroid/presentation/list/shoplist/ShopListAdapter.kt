package com.cakeit.cakitandroid.presentation.list.shoplist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.remote.entity.CakeShopData
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.CakeShopPriceAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.CakeShopTagAdapter
import kotlinx.android.synthetic.main.item_shop_list.view.*

class ShopListAdapter(context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var shopListItems : List<CakeShopData> = listOf()
    private var context = context
    private lateinit var cakeShopTagAdapter : CakeShopTagAdapter
    private lateinit var cakeShopPriceAdapter : CakeShopPriceAdapter
    private lateinit var onItemClick : OnShopItemClickListener

    interface OnShopItemClickListener {
        fun onShopItemClick(position: Int)
    }

    var listener : OnShopItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_list, parent, false)
        val viewHolder = ShopListViewHolder(view, listener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return shopListItems.size
    }

    fun setOnItemClickListener(l: OnShopItemClickListener) {
        onItemClick = l
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cakeShop = shopListItems[position]
        Log.d("songjem", "cakeShop = " + cakeShop)

        val shopListViewHolder = holder as ShopListViewHolder
        cakeShopTagAdapter = CakeShopTagAdapter(cakeShop.hashTag!!)
        cakeShopPriceAdapter = CakeShopPriceAdapter(cakeShop.prices!!)
        shopListViewHolder.shopTagRv.adapter = cakeShopTagAdapter
        shopListViewHolder.shopTagRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        shopListViewHolder.shopPriceRv.adapter = cakeShopPriceAdapter
        shopListViewHolder.shopPriceRv.layoutManager = LinearLayoutManager(context)
        Log.d("songjem", "cakeShop.shopImages.size = " + cakeShop.shopImages!!.size)
        if(cakeShop.shopImages!!.size > 0) {
            Log.d("songjem", "imageUrl = " + cakeShop.shopImages!![0].shopImageUrl)
            Glide.with(context).load(cakeShop.shopImages!![0].shopImageUrl).fallback(R.drawable.strawberry_cake_img).centerCrop().into(shopListViewHolder.shopImg)
        } else {
            Glide.with(context).load(R.drawable.strawberry_cake_img).centerCrop().into(shopListViewHolder.shopImg)
        }

        shopListViewHolder.bind(cakeShop)
    }

    fun setShopListItems(shopItems: List<CakeShopData>) {

        shopListItems = shopItems
        notifyDataSetChanged()
    }

    inner class ShopListViewHolder(view : View, listener : OnShopItemClickListener?) : RecyclerView.ViewHolder(view) {

        val shopListItem = view.cl_shop_list_item
        val shopTagRv = view.rv_shop_tag_item_shop
        val shopPriceRv = view.rv_cake_size_item_shop
        val shopImg = view.iv_shop_list_major_img
        val cakeShopName = view.tv_shop_name_item_shop
        val cakeShopAddress = view.tv_shop_address_item_shop

        fun bind(cakeShop : CakeShopData) {
            cakeShopName.text = cakeShop.shopName
            cakeShopAddress.text = cakeShop.shopAddress

            shopListItem.setOnClickListener {
                Log.d("songjem", "adapter position = " + adapterPosition)
                onItemClick.onShopItemClick(adapterPosition)
            }
        }
    }
}