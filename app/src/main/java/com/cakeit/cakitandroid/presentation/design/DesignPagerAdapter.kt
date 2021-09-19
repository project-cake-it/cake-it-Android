package com.cakeit.cakitandroid.presentation.design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R

class DesignPagerAdapter(context: Context, imageList: ArrayList<String>) : PagerAdapter() {

    val context = context
    val imageList : ArrayList<String> = imageList

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_design_detail, container, false)
        val designImage = v!!.findViewById<ImageView>(R.id.iv_design_image)

        container.addView(v)
        Glide.with(v).load(imageList[position]).centerCrop().into(designImage)

        return v
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