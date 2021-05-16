package com.cakeit.cakitandroid.presentation.list.shoplist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import kotlinx.android.synthetic.main.item_shop_list.view.*

class ShopListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var shopListItems : List<CakeShopData> = listOf()
//    private val shopListItems = mutableListOf<CakeShopData>()

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cakeShop = shopListItems[position]

        val shopListViewHolder = holder as ShopListViewHolder
        shopListViewHolder.bind(cakeShop)
    }

    fun setShopListItems(shopItems: List<CakeShopData>) {

        shopListItems = shopItems
        notifyDataSetChanged()

//        val diffCallback = ShopListDiffCallback(shopListItems, shopItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        shopListItems.clear()
//        shopListItems.addAll(shopItems)
//
//        diffResult.dispatchUpdatesTo(this)

//        Observable.just(shopItems)
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .observeOn(Schedulers.io())
//            .map { DiffUtil.calculateDiff(ShopListDiffCallback(shopListItems, shopItems)) }
//            .subscribe({
//                Log.d("songjem", "subscribe, old shopListItems size = " + shopListItems.size
//                 + "new shopListItems size = " + shopItems.size)
////                shopListItems = shopItems
//                // 리스트 변경한 후 rv 업데이트
//                shopListItems = shopItems
//                it.dispatchUpdatesTo(this)
//            }, {
//                Log.d("songjem", "Error = " + it.message)
//            })
    }

    class ShopListViewHolder(view : View, listener : OnShopItemClickListener?) : RecyclerView.ViewHolder(view) {

        val cakeShopName = view.tv_shop_name_item_shop
        val cakeShopAddress = view.tv_shop_address_item_shop

        init {
            view.setOnClickListener {
                listener?.onShopItemClick(adapterPosition)
            }
        }

        fun bind(cakeShop : CakeShopData) {
            Log.d("songjem", "onBind = " + cakeShop.shopName)
            cakeShopName.text = cakeShop.shopName
            cakeShopAddress.text = cakeShop.shopAddress
        }
    }
}