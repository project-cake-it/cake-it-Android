package com.cakeit.cakitandroid.presentation.list.shoplist.filter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListFragment
import kotlinx.android.synthetic.main.fragment_shop_list.*
import kotlinx.android.synthetic.main.item_filter_tag.view.*

class ShopChoiceTagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
        val shopChoiceTagViewHolder = holder as ShopChoiceTagAdapter.ShopChoiceTagViewHolder

        shopChoiceTagViewHolder.rlItemTag.setOnClickListener {

            // 1:장소, 2:날짜
            var filterCode = ShopListFragment.shopListFragment.choiceTagItems[position].filterCode
            var choiceCode = ShopListFragment.shopListFragment.choiceTagItems[position].choiceCode
            var choiceName = ShopListFragment.shopListFragment.choiceTagItems[position].choiceName
            Log.d("songjem", "filterCode = " + filterCode + ", choiceCode = " + choiceCode + ", choiceName = " + choiceName)

            // 기본 정렬
            if(filterCode == 0) {
                ShopListFragment.shopListFragment.listSelected[0] = false
                ShopListFragment.shopListFragment.clearDefault()
            }

            // 장소
            else if(filterCode == 1) {
                ShopListFragment.shopListFragment.shopRegionFilterAdapter.checkedPosition.remove(choiceCode)
                if(ShopListFragment.shopListFragment.shopRegionFilterAdapter.checkedPosition.size == 0) {
                    ShopListFragment.shopListFragment.listSelected[1] = false
                    ShopListFragment.shopListFragment.clearRegion()
                }
            }
            // 날짜
            else if(filterCode == 2) {
                ShopListFragment.shopListFragment.listSelected[2] = false
                ShopListFragment.shopListFragment.clearDate()
            }
            choiceItems.removeAt(position)

            ShopListFragment.shopListFragment.getShopListByNetwork(ShopListFragment.shopListFragment.choiceTagItems)
            notifyDataSetChanged()
        }
        shopChoiceTagViewHolder.bind(choiceItem)
    }

    fun setChoiceTagItem(listItem: ArrayList<ChoiceTag>) {
        choiceItems = listItem
        notifyDataSetChanged()
    }

    class ShopChoiceTagViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val rlItemTag = view.rl_name_item_tag
        val tagName = view.tv_name_item_tag
        val btnDeleteTag = view.btn_delete_item_tag

        fun bind(choiceTag : ChoiceTag) {
            tagName.text = choiceTag.choiceName
        }
    }
}