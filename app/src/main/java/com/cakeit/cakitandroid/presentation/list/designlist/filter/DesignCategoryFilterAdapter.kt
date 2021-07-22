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
    var checkedPosition = HashSet<Int>()
    private var checkCnt = 0

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
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designCategoryItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val designCategoryItem = designCategoryItems[position]
        val designCategoryFilterViewHolder = holder as DesignCategoryFilterViewHolder

        if(checkedPosition.contains(position)) designCategoryFilterViewHolder.designCategoryCheckBtn.visibility = View.VISIBLE
        else designCategoryFilterViewHolder.designCategoryCheckBtn.visibility = View.GONE

        designCategoryFilterViewHolder.designCateoryRl.setOnClickListener {
            // 전체 선택 시
            if(position == 0) {
                checkedPosition.clear()
                checkedPosition.add(0)
            }
            // 특정 카테고리 선택 시(중복 가능)
            else {
                if(designCategoryFilterViewHolder.designCategoryCheckBtn.visibility == View.GONE) {
                    designCategoryFilterViewHolder.designCategoryCheckBtn.visibility = View.VISIBLE
                    checkedPosition.add(position)
                    checkCnt += 1
                }
                else {
                    designCategoryFilterViewHolder.designCategoryCheckBtn.visibility = View.GONE
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

        designCategoryFilterViewHolder.bind(designCategoryItem)
    }

    fun setDesignCategoryItems(listItem: List<String>) {
        designCategoryItems = listItem
        notifyDataSetChanged()
    }

    class DesignCategoryFilterViewHolder(view : View, listener : OnDesignCategoryItemClickListener?) : RecyclerView.ViewHolder(view) {

        val designCateoryRl = view.rl_filter_item_list_filter
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

    fun getCheckedCnt() : Int {
        return checkedPosition.size
    }

    fun getChoiceTagIndex() : ArrayList<Int> {
        var list = ArrayList<Int>(checkedPosition)
        return list
    }

    fun getClickedItem() : String {
        var clickedData = ""
        var list = ArrayList(checkedPosition)

        clickedData = designCategoryItems[list.get(0)]
        return clickedData
    }
}