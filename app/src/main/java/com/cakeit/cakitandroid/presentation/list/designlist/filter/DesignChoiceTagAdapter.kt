package com.cakeit.cakitandroid.presentation.list.designlist.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.presentation.list.designlist.DesignListActivity
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListFragment
import kotlinx.android.synthetic.main.activity_design_list.*
import kotlinx.android.synthetic.main.item_filter_tag.view.*

class DesignChoiceTagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var choiceItems = ArrayList<ChoiceTag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_tag, parent, false)
        val viewHolder = DesignChoiceTagViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return choiceItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val choiceItem = choiceItems[position]
        val designChoiceTagViewHolder = holder as DesignChoiceTagViewHolder

        designChoiceTagViewHolder.rlNameTag.setOnClickListener {
            // 0:기본 정렬, 1:지역, 2:크기, 3:색깔, 4:카테고리
            var filterCode = DesignListActivity.designListActivity.choiceTagItems[position].filterCode
            var choiceCode = DesignListActivity.designListActivity.choiceTagItems[position].choiceCode

            // 기본 정렬
            if(filterCode == 0) {
                DesignListActivity.designListActivity.listSelected[0] = false
                DesignListActivity.designListActivity.clearDefault()
            }
            // 지역
            else if(filterCode == 1) {
                DesignListActivity.designListActivity.designRegionFilterAdapter.checkedPosition.remove(choiceCode)
                if(DesignListActivity.designListActivity.designRegionFilterAdapter.checkedPosition.size == 0) {
                    DesignListActivity.designListActivity.listSelected[1] = false
                    DesignListActivity.designListActivity.clearRegion()
                }
            }
            // 크기
            else if(filterCode == 2) {
                DesignListActivity.designListActivity.designSizeFilterAdapter.checkedPosition.remove(choiceCode)
                if(DesignListActivity.designListActivity.designSizeFilterAdapter.checkedPosition.size == 0) {
                    DesignListActivity.designListActivity.listSelected[2] = false
                    DesignListActivity.designListActivity.clearSize()
                }
            }
            // 색깔
            else if(filterCode == 3) {
                DesignListActivity.designListActivity.designColorFilterAdapter.checkedPosition.remove(choiceCode)
                if(DesignListActivity.designListActivity.designColorFilterAdapter.checkedPosition.size == 0) {
                    DesignListActivity.designListActivity.listSelected[3] = false
                    DesignListActivity.designListActivity.clearColor()
                }
            }
            // 카테고리
            else if(filterCode == 4) {
                DesignListActivity.designListActivity.designCategoryFilterAdapter.checkedPosition.remove(choiceCode)
                if(DesignListActivity.designListActivity.designCategoryFilterAdapter.checkedPosition.size == 0) {
                    DesignListActivity.designListActivity.listSelected[4] = false
                    DesignListActivity.designListActivity.clearCategory()
                }
            }

            choiceItems.removeAt(position)
            DesignListActivity.designListActivity.getDesignListByNetwork(DesignListActivity.designListActivity.choiceTagItems)
            notifyDataSetChanged()
        }
        designChoiceTagViewHolder.bind(choiceItem)
    }

    fun setChoiceTagItem(listItem: ArrayList<ChoiceTag>) {
        choiceItems = listItem
        notifyDataSetChanged()
    }

    class DesignChoiceTagViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val rlNameTag = view.rl_name_item_tag
        val tagName = view.tv_name_item_tag

        fun bind(choiceTag : ChoiceTag) {
            tagName.text = choiceTag.choiceName
        }
    }
}