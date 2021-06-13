package com.cakeit.cakitandroid.presentation.list.designlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import kotlinx.android.synthetic.main.item_design_list.view.*

class DesignListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var designListItems : List<CakeDesignData> = listOf()
//    private val designListItems = mutableListOf<CakeDesignData>()

    interface OnDesignItemClickListener {
        fun onDesignItemClick(position: Int)
    }

    var listener : OnDesignItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_list, parent, false)
        val viewHolder = DesignListViewHolder(view, listener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return designListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cakeDesign = designListItems[position]

        val designListViewHolder = holder as DesignListViewHolder
        designListViewHolder.bind(cakeDesign)
    }

    fun setDesignListItems(designItems: List<CakeDesignData>) {

        designListItems = designItems
        notifyDataSetChanged()

//        val diffCallback = ShopListDiffCallback(shopListItems, shopItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        shopListItems.clear()
//        shopListItems.addAll(shopItems)
//
//        diffResult.dispatchUpdatesTo(this)

//        Observable.just(shopItems)
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .observeOn(Schedulers.io())
//            .map { DiffUtil.calculateDiff(ShopListDiffCallback(shopListItems, shopItems)) }
//            .subscribe({
//                Log.d("songjem", "subscribe, old shopListItems size = " + shopListItems.size
//                 + "new shopListItems size = " + shopItems.size)
////                shopListItems = shopItems
//                // 리스트 변경한 후 rv 업데이트
//                shopListItems = shopItems
//                it.dispatchUpdatesTo(this)
//            }, {
//                Log.d("songjem", "Error = " + it.message)
//            })
    }

    class DesignListViewHolder(view : View, listener : OnDesignItemClickListener?) : RecyclerView.ViewHolder(view) {

        val designImg = view.iv_design_img_item_design_list
        val designLoc = view.tv_shop_loc_item_design_list
        val designSize = view.tv_size_item_design_list
        val designShop = view.tv_shop_name_item_design_list
        val designPrice = view.tv_design_price_item_design_list

        init {
            view.setOnClickListener {
                listener?.onDesignItemClick(adapterPosition)
            }
        }

        fun bind(cakeDesign : CakeDesignData) {
            designLoc.text = cakeDesign.designShopLoc
            designSize.text = cakeDesign.designSize
            designShop.text = cakeDesign.designShopName
            designPrice.text = cakeDesign.designPrice.toString()
        }
    }
}