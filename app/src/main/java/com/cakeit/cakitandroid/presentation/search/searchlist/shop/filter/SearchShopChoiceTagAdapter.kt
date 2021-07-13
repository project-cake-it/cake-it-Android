package com.cakeit.cakitandroid.presentation.search.searchlist.shop.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.SearchShopFragment
import kotlinx.android.synthetic.main.item_filter_tag.view.*

class SearchShopChoiceTagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var choiceItems = ArrayList<ChoiceTag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_tag, parent, false)
        val viewHolder = ShopChoiceTagViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return choiceItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val choiceItem = choiceItems[position]
        val shopChoiceTagViewHolder = holder as SearchShopChoiceTagAdapter.ShopChoiceTagViewHolder

        shopChoiceTagViewHolder.btnDeleteTag.setOnClickListener {
            // 1:지역
            var filterCode = SearchShopFragment.searchShopFragment.choiceTagItems[position].filterCode
            var choiceCode = SearchShopFragment.searchShopFragment.choiceTagItems[position].choiceCode

            // 지역
            if(filterCode == 1) {
                SearchShopFragment.searchShopFragment.searchShopRegionAdapter.checkedPosition.remove(choiceCode)
                if(SearchShopFragment.searchShopFragment.searchShopRegionAdapter.checkedPosition.size == 0) {
                    SearchShopFragment.searchShopFragment.listSelected[1] = false
                    SearchShopFragment.searchShopFragment.clearRegion()
                }
            }
            choiceItems.removeAt(position)
            SearchShopFragment.searchShopFragment.getShopListByNetwork(SearchShopFragment.searchShopFragment.choiceTagItems)
            notifyDataSetChanged()
        }
        shopChoiceTagViewHolder.bind(choiceItem)
    }

    fun setChoiceTagItem(listItem: ArrayList<ChoiceTag>) {
        choiceItems = listItem
        notifyDataSetChanged()
    }

    class ShopChoiceTagViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val tagName = view.tv_name_item_tag
        val btnDeleteTag = view.btn_delete_item_tag

        fun bind(choiceTag : ChoiceTag) {
            tagName.text = choiceTag.choiceName
        }
    }
}