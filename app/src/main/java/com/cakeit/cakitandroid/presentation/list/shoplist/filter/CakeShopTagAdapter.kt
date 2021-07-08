package com.cakeit.cakitandroid.presentation.list.shoplist.filter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.domain.model.CakeShopHashTag
import kotlinx.android.synthetic.main.item_shop_tag.view.*

class CakeShopTagAdapter(var shopHashTags : ArrayList<CakeShopHashTag>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

//    private var shopHashTags = ArrayList<CakeShopHashTag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_tag, parent, false)
        val viewHolder = CakeShopTagViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return shopHashTags.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val shopHashTag = shopHashTags[position]
        val cakeShopTagViewHolder = holder as CakeShopTagViewHolder

        Log.d("songjem", "shopHashTag = " + shopHashTag)
        cakeShopTagViewHolder.bind(shopHashTag)
    }

//    fun setTagItem(listItem: ArrayList<CakeShopHashTag>) {
//        shopHashTags = listItem
//        Log.d("songjem", "shopHashTag Size = " + shopHashTags.size)
//        notifyDataSetChanged()
//    }

    class CakeShopTagViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tagName = view.tv_shop_tag_name

        fun bind(cakeShopHashTag : CakeShopHashTag) {
            Log.d("songjem", "cakeShopHashTag name = " + cakeShopHashTag.name)
            tagName.text = cakeShopHashTag.name
        }
    }
}