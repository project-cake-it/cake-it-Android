package com.cakeit.cakitandroid.presentation.mypage.announcement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.domain.model.TextboardModel
import kotlinx.android.synthetic.main.item_announcement_list.view.*

class AnnouncementListAdapter(context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var announcementListItems : List<TextboardModel> = listOf()
    private lateinit var onItemClick : View.OnClickListener

    private var context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement_list, parent, false)
        val viewHolder = AnnouncementViewHolder(view)
        view.setOnClickListener(onItemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return announcementListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val announcementItem = announcementListItems[position]

        val announcementViewHolder = holder as AnnouncementViewHolder
        announcementViewHolder.bind(announcementItem)
    }

    fun setAnnouncementListItems(announcementItems: List<TextboardModel>) {

        announcementListItems = announcementItems
        notifyDataSetChanged()

    }

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    class AnnouncementViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val announcementTitle: TextView = view.tv_announcement_list_item_title
        val announcementDate: TextView = view.tv_announcement_list_item_date

        fun bind(announcementItem : TextboardModel) {
            announcementTitle.text = announcementItem.title
            announcementDate.text = announcementItem.date

            // String foramt if needed
        }
    }
}