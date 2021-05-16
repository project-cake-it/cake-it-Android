package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignSize
import kotlinx.android.synthetic.main.item_design_size_filter.view.*
import java.util.HashSet

class DesignSizeFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var designSizeItems : List<CakeDesignSize> = listOf()
    private var checkCnt = 0
    private var checkedPosition = HashSet<Int>()

    interface OnDesignSizeItemClickListener {
        fun onDesignSizeFilterItemClick(position: Int)
    }

    var listener : OnDesignSizeItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_size_filter, parent, false)
        val viewHolder =
            DesignSizeFilterViewHolder(
                view,
                listener
            )
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designSizeItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val designSizeItem = designSizeItems[position]
        val designSizeFilterViewHolder = holder as DesignSizeFilterViewHolder

        if(checkedPosition.contains(position)) designSizeFilterViewHolder.designSizeCheckBtn.visibility = View.VISIBLE
        else designSizeFilterViewHolder.designSizeCheckBtn.visibility = View.GONE

        if(position == 0) designSizeFilterViewHolder.designSizeHint.visibility = View.GONE

        designSizeFilterViewHolder.rlFilterListItem.setOnClickListener {
            // 전체 선택 시
            if(position == 0) {
                checkedPosition.clear()
                checkedPosition.add(0)
            }
            // 특정 크기 선택 시(중복 가능)
            else {
                if(designSizeFilterViewHolder.designSizeCheckBtn.visibility == View.GONE) {
                    designSizeFilterViewHolder.designSizeCheckBtn.visibility = View.VISIBLE
                    checkedPosition.add(position)
                    checkCnt += 1
                }
                else {
                    designSizeFilterViewHolder.designSizeCheckBtn.visibility = View.GONE
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

        designSizeFilterViewHolder.bind(designSizeItem)
    }

    fun setDesignSizeItems(listItem: List<CakeDesignSize>) {
        designSizeItems = listItem
        notifyDataSetChanged()
    }

    class DesignSizeFilterViewHolder(view : View, listener : OnDesignSizeItemClickListener?) : RecyclerView.ViewHolder(view) {

        val designSizeName = view.tv_size_name_size_filter
        val designSizeCheckBtn = view.btn_size_check_size_filter
        val designSizeHint = view.tv_size_hint_size_filter
        val rlFilterListItem = view.rl_filter_item_size_filter

        init {
            view.setOnClickListener {
                listener?.onDesignSizeFilterItemClick(adapterPosition)
            }
        }

        fun bind(sizeItem : CakeDesignSize) {
            designSizeName.text = sizeItem.sizeName
            designSizeHint.text = sizeItem.sizeHint
        }
    }
}