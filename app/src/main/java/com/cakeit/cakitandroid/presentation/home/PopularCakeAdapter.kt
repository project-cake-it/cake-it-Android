package com.cakeit.cakitandroid.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.di.api.responses.DesignListResponseData
import kotlinx.android.synthetic.main.item_design_list.view.*
import java.text.DecimalFormat

class PopularCakeAdapter(private var context: Context) : RecyclerView.Adapter<PopularCakeAdapter.DesignListViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener
    private var designListItems = ArrayList<DesignListResponseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_design_list, parent, false)

        view.setOnClickListener(onItemClick)

        return DesignListViewHolder(view)
    }

    override fun getItemCount(): Int = designListItems.size


    override fun onBindViewHolder(holder: DesignListViewHolder, position: Int) {
        holder.bind(designListItems[position], context, position)
    }

    inner class DesignListViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val designImg = view.iv_design_img_item_design_list
        val designLoc = view.tv_shop_loc_item_design_list
        val designSize = view.tv_size_item_design_list
        val designShop = view.tv_shop_name_item_design_list
        val designPrice = view.tv_design_price_item_design_list

        fun bind(data: DesignListResponseData,  context: Context, position: Int)
        {
            designLoc.text = data.shopAddress
            if(data.sizes.size > 0) designSize.text = data.sizes[0].name
            designShop.text = data.name

            if(data.sizes.size > 0) {
                val dec = DecimalFormat("#,###")
                var commaPrice = dec.format(data.sizes[0].price.toLong()) + "원"

                designPrice.text = commaPrice
            }

            if(data.designImages.size > 0) Glide.with(context).load(data.designImages[0].designImageUrl).override(300).centerCrop().into(designImg)

//            Glide.with(context).load(R.drawable.test).into(designImg)
        }
    }


    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    internal fun setRefresh(designListItems: ArrayList<DesignListResponseData>) {
        this.designListItems = designListItems
        notifyDataSetChanged()
    }

}