package com.cakeit.cakitandroid.presentation.search.searchlist.design.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.presentation.search.searchlist.design.SearchDesignFragment
import kotlinx.android.synthetic.main.item_filter_tag.view.*

class SearchDesignChoiceTagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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

        designChoiceTagViewHolder.btnDeleteTag.setOnClickListener {
            // 1:지역, 2:크기, 3:색깔, 4:카테고리
            var filterCode = SearchDesignFragment.searchDesignFragment.choiceTagItems[position].filterCode
            var choiceCode = SearchDesignFragment.searchDesignFragment.choiceTagItems[position].choiceCode

            // 지역
            if(filterCode == 1) {
                 SearchDesignFragment.searchDesignFragment.searchDesignRegionAdapter.checkedPosition.remove(choiceCode)
                if( SearchDesignFragment.searchDesignFragment.searchDesignRegionAdapter.checkedPosition.size == 0) {
                     SearchDesignFragment.searchDesignFragment.listSelected[1] = false
                     SearchDesignFragment.searchDesignFragment.clearRegion()
                }
            }
            // 크기
            else if(filterCode == 2) {
                 SearchDesignFragment.searchDesignFragment.searchDesignSizeAdapter.checkedPosition.remove(choiceCode)
                if( SearchDesignFragment.searchDesignFragment.searchDesignSizeAdapter.checkedPosition.size == 0) {
                     SearchDesignFragment.searchDesignFragment.listSelected[2] = false
                     SearchDesignFragment.searchDesignFragment.clearSize()
                }
            }
            // 색깔
            else if(filterCode == 3) {
                 SearchDesignFragment.searchDesignFragment.searchDesignColorAdapter.checkedPosition.remove(choiceCode)
                if( SearchDesignFragment.searchDesignFragment.searchDesignColorAdapter.checkedPosition.size == 0) {
                     SearchDesignFragment.searchDesignFragment.listSelected[3] = false
                     SearchDesignFragment.searchDesignFragment.clearColor()
                }
            }
            // 카테고리
            else if(filterCode == 4) {
                 SearchDesignFragment.searchDesignFragment.searchDesignCategoryAdapter.checkedPosition.remove(choiceCode)
                if( SearchDesignFragment.searchDesignFragment.searchDesignCategoryAdapter.checkedPosition.size == 0) {
                     SearchDesignFragment.searchDesignFragment.listSelected[4] = false
                     SearchDesignFragment.searchDesignFragment.clearCategory()
                }
            }

            choiceItems.removeAt(position)
            SearchDesignFragment.searchDesignFragment.getSearchDesignByNetwork(SearchDesignFragment.searchDesignFragment.keyword, SearchDesignFragment.searchDesignFragment.keyword, SearchDesignFragment.searchDesignFragment.choiceTagItems)
            notifyDataSetChanged()
        }
        designChoiceTagViewHolder.bind(choiceItem)
    }

    fun setChoiceTagItem(listItem: ArrayList<ChoiceTag>) {
        choiceItems = listItem
        notifyDataSetChanged()
    }

    class DesignChoiceTagViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val tagName = view.tv_name_item_tag
        val btnDeleteTag = view.btn_delete_item_tag

        fun bind(choiceTag : ChoiceTag) {
            tagName.text = choiceTag.choiceName
        }
    }
}