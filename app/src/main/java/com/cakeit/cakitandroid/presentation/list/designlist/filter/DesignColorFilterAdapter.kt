package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.item_design_color_filter.view.*
import java.util.HashSet

class DesignColorFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var designColorItems : List<String> = listOf()
    private var checkCnt = 0
    private var checkedPosition = HashSet<Int>()

    interface OnDesignColorItemClickListener {
        fun onDesignColorFilterItemClick(position: Int)
    }

    var listener : OnDesignColorItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_color_filter, parent, false)
        val viewHolder =
            DesignColorFilterViewHolder(
                view,
                listener
            )
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designColorItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val designColorItem = designColorItems[position]
        val designColorFilterViewHolder = holder as DesignColorFilterViewHolder

        if(checkedPosition.contains(position)) designColorFilterViewHolder.designColorCheckBtn.visibility = View.VISIBLE
        else designColorFilterViewHolder.designColorCheckBtn.visibility = View.GONE

        designColorFilterViewHolder.rlFilterListItem.setOnClickListener {
            // 전체 선택 시
            if(position == 0) {
                checkedPosition.clear()
                checkedPosition.add(0)
            }
            // 특정 구 선택 시(중복 가능)
            else {
                if(designColorFilterViewHolder.designColorCheckBtn.visibility == View.GONE) {
                    designColorFilterViewHolder.designColorCheckBtn.visibility = View.VISIBLE
                    checkedPosition.add(position)
                    checkCnt += 1
                }
                else {
                    designColorFilterViewHolder.designColorCheckBtn.visibility = View.GONE
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
        designColorFilterViewHolder.bind(designColorItem)
    }

    fun setDesignColorItems(listItem: List<String>) {
        designColorItems = listItem
        notifyDataSetChanged()
    }

    class DesignColorFilterViewHolder(view : View, listener : OnDesignColorItemClickListener?) : RecyclerView.ViewHolder(view) {

        val designColorItem = view.tv_filter_item_color_filter
        val designColorBackground = view.iv_filter_color_color_filter
        val designColorCheckBtn = view.btn_filter_check_color_filter
        val rlFilterListItem = view.rl_filter_item_color_filter

        init {
            view.setOnClickListener {
                listener?.onDesignColorFilterItemClick(adapterPosition)
            }
        }

        fun bind(colorItem : String) {
            designColorItem.text = colorItem
        }
    }
}