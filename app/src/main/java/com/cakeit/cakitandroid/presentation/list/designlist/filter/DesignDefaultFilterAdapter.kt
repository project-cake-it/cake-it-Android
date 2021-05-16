package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_shop_list_filter.view.*

class DesignDefaultFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterItems : List<String> = listOf()

    interface OnDesignFilterItemClickListener {
        fun onDesignFilterItemClick(position: Int)
    }

    var listener : OnDesignFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list_filter, parent, false)
        val viewHolder =
            ShopDefaultFilterViewHolder(
                view,
                listener
            )
        return viewHolder
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val filterListItem = filterItems[position]

        val shopListFilterViewHolder = holder as ShopDefaultFilterViewHolder
        shopListFilterViewHolder.bind(filterListItem)
    }

    fun setDefaultListItems(listItem: List<String>) {
        filterItems = listItem
        notifyDataSetChanged()
    }

    class ShopDefaultFilterViewHolder(view : View, listener : OnDesignFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter
        val btnChekedFilterItem = view.btn_filter_check_list_filter

        init {
            view.setOnClickListener {
                listener?.onDesignFilterItemClick(adapterPosition)
            }
            btnChekedFilterItem.visibility = View.GONE
        }

        fun bind(filterItem : String) {
            Log.d("songjem", "onBind = " + filterItem)
            filterListItem.text = filterItem
        }
    }
}