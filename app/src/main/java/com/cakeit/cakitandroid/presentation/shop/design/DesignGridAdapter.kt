package com.cakeit.cakitandroid.presentation.shop.design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R

// TODO : cakeShopDatas  >  emptyList 제네릭 타입 수정 필

class DesignGridAdapter(context: Context) : RecyclerView.Adapter<DesignGridAdapter.ItemViewHolder>() {

    private lateinit var onItemClick : View.OnClickListener
    private var designPhotoDatas = ArrayList<String>()
    private val context = context

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val designPhoto: ImageButton = itemView.findViewById(R.id.ib_design_grid_photo)

        fun bind(data: String,  context: Context)
        {
            if(!data.isNullOrEmpty())
            {
                Glide.with(context).load(data).into(designPhoto)
//                Glide.with(context).load(R.drawable.test).into(designPhoto)
            }
        }
    }

    internal fun setRefresh(designPhotoDatas: ArrayList<String>) {
        this.designPhotoDatas = designPhotoDatas
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignGridAdapter.ItemViewHolder {
        val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_grid, parent, false)

        view.setOnClickListener(onItemClick)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DesignGridAdapter.ItemViewHolder, position: Int) {
        holder.bind(designPhotoDatas[position], context)
    }

    override fun getItemCount(): Int = designPhotoDatas.size

}