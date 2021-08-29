package com.cakeit.cakitandroid.presentation.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.shop.design.DesignGridAdapter
import kotlinx.android.synthetic.main.item_home_promotion.view.*

class PromotionPagerAdapter(context: Context, imageList: ArrayList<String>) : PagerAdapter() {

    lateinit var onItemClick : OnItemClickListener
    val context = context
    val imageList : ArrayList<String> = imageList

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_home_promotion, container, false)
        val promotionImg = v!!.findViewById<ImageView>(R.id.iv_home_promotion_img)

        container.addView(v)
        Glide.with(v).load(imageList[position]).centerCrop().into(promotionImg)

        promotionImg.setOnClickListener{
            onItemClick.OnClick(v, position)
        }

        return v
    }

    interface OnItemClickListener{
        fun OnClick(view: View, position: Int)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        onItemClick = l
    }
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}