package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_design_list_filter.view.*
import java.util.HashSet

class DesignDefaultFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterItems : List<String> = listOf()
    var checkedPosition = HashSet<Int>()

    interface OnDesignFilterItemClickListener {
        fun onDesignFilterItemClick(position: Int)
    }

    var listener : OnDesignFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list_filter, parent, false)
        val viewHolder =
                DesignDefaultFilterViewHolder(
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

        val shopListFilterViewHolder = holder as DesignDefaultFilterViewHolder
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

    class DesignDefaultFilterViewHolder(view : View, listener : OnDesignFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter
        val btnChekedFilterItem = view.btn_filter_check_list_filter
        val rlFilterListItem = view.rl_filter_item_list_filter

        init {
            view.setOnClickListener {
                listener?.onDesignFilterItemClick(adapterPosition)
            }
            btnChekedFilterItem.visibility = View.GONE
        }

        fun bind(filterItem : String) {
            filterListItem.text = filterItem
        }
    }
}