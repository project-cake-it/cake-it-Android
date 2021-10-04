package com.cakeit.cakitandroid.presentation.shop.calendar


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.cakeit.cakitandroid.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var orderDates = ArrayList<String>()
        orderDates = intent.getStringArrayListExtra("dates")!!

        val calendarView = cv_calendar as MaterialCalendarView

        calendarView.addDecorator(TodayDecorator(applicationContext))
        calendarView.addDecorator(RangeDecorator(applicationContext, orderDates))


        ib_calendar_close.setOnClickListener{
            finish()
        }
    }
}
