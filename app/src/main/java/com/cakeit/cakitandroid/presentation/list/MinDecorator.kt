package com.cakeit.cakitandroid.presentation.list

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MinDecorator(context: Context): DayViewDecorator {
    val toDay = CalendarDay.today()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return (day?.month == toDay.month && day.day < toDay.day)
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: ForegroundColorSpan(Color.parseColor("#d2d2d2")){})
        view?.setDaysDisabled(true)
    }
}