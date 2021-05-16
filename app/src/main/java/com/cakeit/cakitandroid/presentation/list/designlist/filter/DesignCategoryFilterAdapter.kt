package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_design_list_filter.view.*
import java.util.HashSet

class DesignCategoryFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var designCategoryItems : List<String> = listOf()

    interface OnDesignCategoryItemClickListener {
        fun onDesignCategoryFilterItemClick(position: Int)
    }

    var listener : OnDesignCategoryItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list_filter, parent, false)
        val viewHolder =
            DesignCategoryFilterViewHolder(
                view,
                listener
            )
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designCategoryItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val designCategoryItem = designCategoryItems[position]
        val designCategoryFilterViewHolder = holder as DesignCategoryFilterViewHolder

        designCategoryFilterViewHolder.bind(designCategoryItem)
    }

    fun setDesignCategoryItems(listItem: List<String>) {
        designCategoryItems = listItem
        notifyDataSetChanged()
    }

    class DesignCategoryFilterViewHolder(view : View, listener : OnDesignCategoryItemClickListener?) : RecyclerView.ViewHolder(view) {

        val designCategoryName = view.tv_filter_item_list_filter
        val designCategoryCheckBtn = view.btn_filter_check_list_filter

        init {
            view.setOnClickListener {
                listener?.onDesignCategoryFilterItemClick(adapterPosition)
            }
            designCategoryCheckBtn.visibility = View.GONE
        }

        fun bind(categoryItem : String) {
            designCategoryName.text = categoryItem
        }
    }
}