package com.cakeit.cakitandroid.presentation.shoplist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_shop_list_filter.view.*

class ShopListFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterItems : List<String> = listOf()

    interface OnShopFilterItemClickListener {
        fun onShopFilterItemClick(position: Int)
    }

    var listener : OnShopFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_list_filter, parent, false)
        val viewHolder = ShopListFilterViewHolder(view, listener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val filterListItem = filterItems[position]

        val shopListFilterViewHolder = holder as ShopListFilterViewHolder
        shopListFilterViewHolder.bind(filterListItem)
    }

    fun setShopListItems(listItem: List<String>) {
        filterItems = listItem
        notifyDataSetChanged()
    }

    class ShopListFilterViewHolder(view : View, listener : OnShopFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter

        init {
            view.setOnClickListener {
                listener?.onShopFilterItemClick(adapterPosition)
            }
        }

        fun bind(filterItem : String) {
            Log.d("songjem", "onBind = " + filterItem)
            filterListItem.text = filterItem
        }
    }
}