package com.cakeit.cakitandroid.presentation.search.searchlist.design.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_design_list_filter.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.listOf

class SearchDesignRegionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var regionItems : List<String> = listOf()
    private var checkCnt = 0
    var checkedPosition = HashSet<Int>()

    interface OnDesignFilterItemClickListener {
        fun onDesignFilterItemClick(position: Int)
    }

    var listener : OnDesignFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list_filter, parent, false)
        val viewHolder =
            DesignRegionFilterViewHolder(
                view,
                listener
            )
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    fun getChoiceTagIndex() : ArrayList<Int> {
        var list = ArrayList<Int>(checkedPosition)
        return list
    }

    override fun getItemCount(): Int {
        return regionItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val regionListItem = regionItems[position]
        val designRegionFilterViewHolder = holder as DesignRegionFilterViewHolder

        if(checkedPosition.contains(position)) designRegionFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
        else designRegionFilterViewHolder.btnChekedFilterItem.visibility = View.GONE

        designRegionFilterViewHolder.rlFilterListItem.setOnClickListener {
            // 전체 선택 시
            if(position == 0) {
                checkedPosition.clear()
                checkedPosition.add(0)
            }
            // 특정 구 선택 시(중복 가능)
            else {
                if(designRegionFilterViewHolder.btnChekedFilterItem.visibility == View.GONE) {
                    designRegionFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
                    checkedPosition.add(position)
                    checkCnt += 1
                }
                else {
                    designRegionFilterViewHolder.btnChekedFilterItem.visibility = View.GONE
                    checkedPosition.remove(position)
                    checkCnt -= 1
                }
                // 전체 선택 체크 OFF
                if(checkCnt > 0) checkedPosition.remove(0)
                // 전체 선택 체크 ON
                else checkedPosition.add(0)
            }
            notifyDataSetChanged()
        }
        designRegionFilterViewHolder.bind(regionListItem)
    }

    fun setRegionListItems(listItem: List<String>) {
        regionItems = listItem
        notifyDataSetChanged()
    }

    class DesignRegionFilterViewHolder(view : View, listener : OnDesignFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter
        val btnChekedFilterItem = view.btn_filter_check_list_filter
        val rlFilterListItem = view.rl_filter_item_list_filter

        init {
            view.setOnClickListener {
                listener?.onDesignFilterItemClick(adapterPosition)
            }
        }

        fun bind(filterItem : String) {
            filterListItem.text = filterItem
        }
    }
}