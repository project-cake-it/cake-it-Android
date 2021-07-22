package com.cakeit.cakitandroid.presentation.shop.design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cakeit.cakitandroid.R

class DesignGridAdapter(context: Context) : RecyclerView.Adapter<DesignGridAdapter.ItemViewHolder>() {

    private lateinit var onItemClick : OnItemClickListener
    private var designPhotoDatas = ArrayList<String>()
    private val context = context

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val designPhoto: ImageButton = itemView.findViewById(R.id.ib_design_grid_photo)

        fun bind(data: String,  context: Context, position: Int)
        {
            if(!data.isNullOrEmpty())
            {
                Glide.with(context).load(data).into(designPhoto)
//                Glide.with(context).load(R.drawable.test).into(designPhoto)
                designPhoto.setOnClickListener{
                    onItemClick.OnClick(itemView, position)
                }
            }
        }
    }

    internal fun setRefresh(designPhotoDatas: ArrayList<String>) {
        this.designPhotoDatas = designPhotoDatas
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun OnClick(view: View, position: Int)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignGridAdapter.ItemViewHolder {
        val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_grid, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DesignGridAdapter.ItemViewHolder, position: Int) {
        holder.bind(designPhotoDatas[position], context, position)
    }

    override fun getItemCount(): Int = designPhotoDatas.size

}