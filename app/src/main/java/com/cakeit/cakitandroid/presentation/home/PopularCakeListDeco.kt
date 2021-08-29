package com.cakeit.cakitandroid.presentation.home

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

class PopularCakeListDeco(context : Context) : RecyclerView.ItemDecoration() {

    private var splitSpace : Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position : Int = parent.getChildAdapterPosition(view)

        // 왼쪽 아이템만 여백
        if(position % 2 == 0){
            outRect.right = splitSpace
        }
    }

    // 간격 2
    init{
        this.splitSpace = DpToPx(context, 2)
    }

    fun DpToPx(context : Context, dp : Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}