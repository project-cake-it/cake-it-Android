package com.cakeit.cakitandroid.presentation.list.shoplist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_shop_list_filter.view.*
import java.util.HashSet

class ShopDefaultFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterItems : List<String> = listOf()
    var checkedPosition = HashSet<Int>()

    interface OnShopFilterItemClickListener {
        fun onShopFilterItemClick(position: Int)
    }

    var listener : OnShopFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_list_filter, parent, false)
        val viewHolder =
                ShopDefaultFilterViewHolder(
                view,
                listener
            )
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    fun getClickedItem() : String {
        var clickedData = ""
        var list = ArrayList(checkedPosition)

        clickedData = filterItems[list.get(0)]
        return clickedData
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val filterListItem = filterItems[position]

        val shopListFilterViewHolder = holder as ShopDefaultFilterViewHolder
        if(checkedPosition.contains(position)) shopListFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
        else shopListFilterViewHolder.btnChekedFilterItem.visibility = View.GONE
        shopListFilterViewHolder.bind(filterListItem)

        shopListFilterViewHolder.rlFilterListItem.setOnClickListener {

            if(shopListFilterViewHolder.btnChekedFilterItem.visibility == View.GONE) shopListFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
            else shopListFilterViewHolder.btnChekedFilterItem.visibility = View.GONE
            checkedPosition.clear()
            checkedPosition.add(position)
            notifyDataSetChanged()
        }
    }

    fun setDefaultListItems(listItem: List<String>) {
        filterItems = listItem
        notifyDataSetChanged()
    }

    class ShopDefaultFilterViewHolder(view : View, listener : OnShopFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter
        val btnChekedFilterItem = view.btn_filter_check_list_filter
        val rlFilterListItem = view.rl_filter_item_list_filter

        init {
            view.setOnClickListener {
                listener?.onShopFilterItemClick(adapterPosition)
            }
            btnChekedFilterItem.visibility = View.GONE
        }

        fun bind(filterItem : String) {
            filterListItem.text = filterItem
        }
    }
}