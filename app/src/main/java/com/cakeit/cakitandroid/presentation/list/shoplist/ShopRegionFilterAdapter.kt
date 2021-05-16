package com.cakeit.cakitandroid.presentation.list.shoplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_shop_list_filter.view.*
import java.util.HashSet


class ShopRegionFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var regionItems : List<String> = listOf()
    private var checkCnt = 0
    private var checkedPosition = HashSet<Int>()

    interface OnShopFilterItemClickListener {
        fun onShopFilterItemClick(position: Int)
    }

    var listener : OnShopFilterItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_list_filter, parent, false)
        val viewHolder = ShopRegionFilterViewHolder(view, listener)
        checkedPosition.add(0)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return regionItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val regionListItem = regionItems[position]
        val shopRegionFilterViewHolder = holder as ShopRegionFilterViewHolder

        if(checkedPosition.contains(position)) shopRegionFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
        else shopRegionFilterViewHolder.btnChekedFilterItem.visibility = View.GONE

        shopRegionFilterViewHolder.rlFilterListItem.setOnClickListener {
            // 전체 선택 시
            if(position == 0) {
                checkedPosition.clear()
                checkedPosition.add(0)

            }
            // 특정 구 선택 시(중복 가능)
            else {
                if(shopRegionFilterViewHolder.btnChekedFilterItem.visibility == View.GONE) {
                    shopRegionFilterViewHolder.btnChekedFilterItem.visibility = View.VISIBLE
                    checkedPosition.add(position)
                    checkCnt += 1
                }
                else {
                    shopRegionFilterViewHolder.btnChekedFilterItem.visibility = View.GONE
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
        shopRegionFilterViewHolder.bind(regionListItem)
    }

    fun setRegionListItems(listItem: List<String>) {
        regionItems = listItem
        notifyDataSetChanged()
    }

    class ShopRegionFilterViewHolder(view : View, listener : OnShopFilterItemClickListener?) : RecyclerView.ViewHolder(view) {

        val filterListItem = view.tv_filter_item_list_filter
        val btnChekedFilterItem = view.btn_filter_check_list_filter
        val rlFilterListItem = view.rl_filter_item_list_filter

        init {
            view.setOnClickListener {
                listener?.onShopFilterItemClick(adapterPosition)
            }
        }

        fun bind(filterItem : String) {
            filterListItem.text = filterItem
        }
    }
}