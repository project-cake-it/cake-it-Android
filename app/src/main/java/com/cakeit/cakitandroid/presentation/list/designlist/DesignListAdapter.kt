package com.cakeit.cakitandroid.presentation.list.designlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import kotlinx.android.synthetic.main.item_design_list.view.*
import java.text.DecimalFormat

class DesignListAdapter(context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var designListItems : List<CakeDesignData> = listOf()
//    private val designListItems = mutableListOf<CakeDesignData>()
    private lateinit var onItemClick : View.OnClickListener

    private var context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list, parent, false)
        val viewHolder = DesignListViewHolder(view)
        view.setOnClickListener(onItemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cakeDesign = designListItems[position]

        val designListViewHolder = holder as DesignListViewHolder
        designListViewHolder.bind(cakeDesign)

        Glide.with(context).load(cakeDesign.designImagUrl).fallback(R.drawable.strawberry_cake_img).centerCrop().into(designListViewHolder.designImg)
    }

    fun setDesignListItems(designItems: List<CakeDesignData>) {

        designListItems = designItems
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    class DesignListViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val designImg = view.iv_design_img_item_design_list
        val designLoc = view.tv_shop_loc_item_design_list
        val designSize = view.tv_size_item_design_list
        val designShop = view.tv_shop_name_item_design_list
        val designPrice = view.tv_design_price_item_design_list

        fun bind(cakeDesign : CakeDesignData) {
            designLoc.text = cakeDesign.designShopLoc
            designSize.text = cakeDesign.designSize
            designShop.text = cakeDesign.designName

            val df = DecimalFormat("###,###")
            val designPriceComma: String = df.format(cakeDesign.designPrice) + "원"

            designPrice.text = designPriceComma
        }
    }
}