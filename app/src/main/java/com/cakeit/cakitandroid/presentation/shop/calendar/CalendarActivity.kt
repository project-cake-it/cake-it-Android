package com.cakeit.cakitandroid.presentation.shop.calendar

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_design_detail.*

class CalendarActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView = cv_calendar as MaterialCalendarView

        val currentDay = Calendar.getInstance()

        val currentYear =  currentDay.get(Calendar.YEAR)
        val currentMonth =  currentDay.get(Calendar.MONTH)+1
        var currentDate =  currentDay.get(Calendar.DATE)

        if(currentDay.get(Calendar.HOUR_OF_DAY) >= 14) currentDate += 1

        val lastDay = Calendar.getInstance()
        lastDay.set(Calendar.MONTH, currentMonth+1)

        val lastYear =  lastDay.get(Calendar.YEAR)
        val lastMonth =  lastDay.get(Calendar.MONTH)
        val lastDate =  lastDay.get(Calendar.DATE)

        val startDay = CalendarDay.from(currentYear, currentMonth, currentDate)
        val endDay = CalendarDay.from(lastYear, lastMonth, lastDate)

        calendarView.addDecorator(TodayDecorator(applicationContext))
        calendarView.addDecorator(RangeDecorator(applicationContext, startDay, endDay))


        ib_calendar_close.setOnClickListener{
            finish()
        }
    }
}
