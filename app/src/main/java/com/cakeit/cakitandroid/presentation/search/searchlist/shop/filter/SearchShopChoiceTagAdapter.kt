package com.cakeit.cakitandroid.presentation.search.searchlist.shop.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListFragment
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

        shopChoiceTagViewHolder.rlNameTag.setOnClickListener {
            // 0:기본 정렬, 1:지역, 2:날짜
            var filterCode = SearchShopFragment.searchShopFragment.choiceTagItems[position].filterCode
            var choiceCode = SearchShopFragment.searchShopFragment.choiceTagItems[position].choiceCode

            // 기본 정렬
            if(filterCode == 0) {
                SearchShopFragment.searchShopFragment.listSelected[0] = false
                SearchShopFragment.searchShopFragment.clearDefault()
            }
            // 지역
            else if(filterCode == 1) {
                SearchShopFragment.searchShopFragment.searchShopRegionAdapter.checkedPosition.remove(choiceCode)
                if(SearchShopFragment.searchShopFragment.searchShopRegionAdapter.checkedPosition.size == 0) {
                    SearchShopFragment.searchShopFragment.listSelected[1] = false
                    SearchShopFragment.searchShopFragment.clearRegion()
                }
            }
            // 날짜
            else if(filterCode == 2) {
                SearchShopFragment.searchShopFragment.listSelected[2] = false
                SearchShopFragment.searchShopFragment.clearDate()
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

        val rlNameTag = view.rl_name_item_tag
        val tagName = view.tv_name_item_tag

        fun bind(choiceTag : ChoiceTag) {
            tagName.text = choiceTag.choiceName
        }
    }
}