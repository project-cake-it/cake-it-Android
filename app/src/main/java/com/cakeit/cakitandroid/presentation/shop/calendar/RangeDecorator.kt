package com.cakeit.cakitandroid.presentation.shop.calendar

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class RangeDecorator(private val context: Context, val start: CalendarDay, val  end: CalendarDay): DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return ((day?.month!! == start.month && day.day < start.day)
                || (day?.month == end.month && day.day > end.day))
    }

    override fun decorate(view: DayViewFacade?) {
        if (view != null)
        {
            val color = ContextCompat.getColor(context, R.color.lightText)
            view.addSpan(ForegroundColorSpan(color))
            view.setDaysDisabled(true)
        }
    }

}