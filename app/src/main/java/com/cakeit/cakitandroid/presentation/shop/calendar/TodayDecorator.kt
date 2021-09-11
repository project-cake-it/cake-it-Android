package com.cakeit.cakitandroid.presentation.shop.calendar

import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(private val context: Context): DayViewDecorator {

    private var date: CalendarDay = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day!!.equals(date)
    }

    override fun decorate(view: DayViewFacade?) {
        if(view !=  null) {
            val color = ContextCompat.getColor(context, R.color.colorPrimary)
            view.addSpan(ForegroundColorSpan(color))
            view.addSpan(StyleSpan(BOLD))
        }
    }
}