package com.cakeit.cakitandroid.presentation.shoplist

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(context: Context): DayViewDecorator {
    private var date = CalendarDay.today()
//    val drawable = context.resources.getDrawable(R.drawable.style_only_radius_10)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
    }
}