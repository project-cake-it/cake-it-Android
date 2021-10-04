package com.cakeit.cakitandroid.presentation.shop.calendar

import android.content.Context
import android.icu.util.LocaleData
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.ContextCompat
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class RangeDecorator(private val context: Context, val dates: ArrayList<String>): DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        val year = day?.year.toString()
        var month = day?.month!!.toString()
        if(day.month < 10) month = "0${day.month}"
        var date = day.day.toString()
        if(day.day < 10) date = "0${day.day}"

        return !dates.contains(year+month+date)
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