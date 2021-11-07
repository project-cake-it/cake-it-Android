package com.cakeit.cakitandroid.presentation.list.designlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cakeit.cakitandroid.R
import kotlinx.android.synthetic.main.activity_design_list.*
import kotlinx.android.synthetic.main.item_design_theme.view.*
import java.util.HashSet

class DesignThemeListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var themeItems : List<String> = listOf()
    var checkedPosition = HashSet<Int>()

    interface OnDesignThemeItemClickListener {
        fun onDesignThemeItemClick(position: Int)
    }

    var listener : OnDesignThemeItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_theme, parent, false)
        val viewHolder =
            DesignThemeListViewHolder(
                view,
                listener
            )
        if(checkedPosition.size == 0) checkedPosition.add(0)
        return viewHolder
    }

    fun addCheckedPosition(position: Int) {
        checkedPosition.add(position)
    }

    fun getClickedItem() : String {
        var clickedData = ""
        var list = ArrayList(checkedPosition)

        clickedData = themeItems[list.get(0)]
        return clickedData
    }

    override fun getItemCount(): Int {
        return themeItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val themeItem = themeItems[position]

        val designThemeListViewHolder = holder as DesignThemeListViewHolder
        if(checkedPosition.contains(position)) designThemeListViewHolder.themeName.setTextColor(Color.parseColor("#df7373"))
        else designThemeListViewHolder.themeName.setTextColor(Color.parseColor("#242424"))
        designThemeListViewHolder.bind(themeItem)

        designThemeListViewHolder.rlTheme.setOnClickListener {

            checkedPosition.clear()
            checkedPosition.add(position)

            DesignListActivity.designListActivity.designListBinding.tvDesignTitleDesignList.text = DesignListActivity.designListActivity.designThemeList[position]
            DesignListActivity.designListActivity.designListBinding.btnDesignNameDesignList.isSelected = false
            DesignListActivity.designListActivity.designListBinding.rlThemeContentDesignList.visibility = View.INVISIBLE
            DesignListActivity.designListActivity.designListBinding.viewBackgroundDesignList.visibility = View.INVISIBLE
            DesignListActivity.designListActivity.getDesignListByNetwork(
                DesignListActivity.designListActivity.choiceTagItems,
                DesignListActivity.designListActivity.designThemeList[position]
            )
        }
    }

    fun setDeisgnThemeItems(listItem: List<String>) {
        themeItems = listItem
        notifyDataSetChanged()
    }

    class DesignThemeListViewHolder(view : View, listener : OnDesignThemeItemClickListener?) : RecyclerView.ViewHolder(view) {

        val rlTheme = view.rl_theme_name_item_design
        val themeName = view.tv_theme_name_item_design

        init {
            view.setOnClickListener {
                listener?.onDesignThemeItemClick(adapterPosition)
            }
        }

        fun bind(themeItem : String) {
            themeName.text = themeItem
        }
    }
}