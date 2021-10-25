package com.cakeit.cakitandroid.presentation.list.designlist

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

class DesignRvItemDeco(context : Context, itemPosition : Int) : RecyclerView.ItemDecoration() {

    private var splitSpace : Int = 0
    var itemPosition : Int = itemPosition

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
//        var position : Int = parent.getChildAdapterPosition(view)

        // 왼쪽 칸만
        if((itemPosition % 2) == 0){
            outRect.right = splitSpace
        }
    }

    // 간격 1dp
    init{
        this.splitSpace = fromDpToPx(context, 1)
    }

    fun fromDpToPx(context : Context, dp : Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}