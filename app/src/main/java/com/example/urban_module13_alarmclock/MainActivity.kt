package com.example.urban_module13_alarmclock

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.size
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {

    private var materialTimePicker: MaterialTimePicker? = null
    private var calendar: Calendar? = null
    private lateinit var mainToolbar: Toolbar

    private lateinit var setAlarmClockBTN: Button
    private lateinit var alarmClocksLV: ListView

    private val alarmClocksList = mutableListOf<AlarmClock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainToolbar = findViewById(R.id.mainToolbar)
        setAlarmClockBTN = findViewById(R.id.setAlarmClockBTN)
        alarmClocksLV = findViewById(R.id.alarmClocksLV)

        setSupportActionBar(mainToolbar)
        title = getString(R.string.toolbar_title)

        val listAdapter = ListAdapter(this, alarmClocksList)

        setAlarmClockBTN.setOnClickListener {
            materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Установите время будильника")
                .build()
            materialTimePicker!!.addOnPositiveButtonClickListener {
                calendar = Calendar.getInstance()
                calendar?.set(Calendar.MILLISECOND, 0)
                calendar?.set(Calendar.SECOND, 0)
                calendar?.set(Calendar.MINUTE, materialTimePicker!!.minute)
                calendar?.set(Calendar.HOUR_OF_DAY, materialTimePicker!!.hour)

                val id = (Int.MIN_VALUE..Int.MAX_VALUE).random()
                alarmClocksList.add(AlarmClock(formatTime(calendar!!), id))
                alarmClocksLV.adapter = listAdapter
                listAdapter.notifyDataSetChanged()
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(
                    RTC_WAKEUP,
                    calendar?.timeInMillis!!,
                    getAlarmPendingIntent(id)!!
                )
            }
            materialTimePicker!!.show(supportFragmentManager, "tag_picker")
        }

        alarmClocksLV.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(getAlarmPendingIntent(alarmClocksList[position].id)!!)
                alarmClocksLV.adapter = listAdapter
                alarmClocksList.removeAt(position)
                listAdapter.notifyDataSetChanged()
            }
    }

    private fun getAlarmPendingIntent(id: Int): PendingIntent? {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getBroadcast(
            this,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun formatTime(calendar: Calendar): String {
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        return if (minutes > 10) "$hours:$minutes" else "$hours:0$minutes"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val timeArray = arrayListOf<String>()
        var idArray = intArrayOf()
        for (i in alarmClocksList) {
            timeArray += i.time
            idArray += i.id
        }
        outState.putStringArrayList("timeArray", timeArray)
        outState.putIntArray("idArray", idArray)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val timeArray = savedInstanceState.getStringArrayList("timeArray")
        val idArray = savedInstanceState.getIntArray("idArray")
        timeArray!!.forEachIndexed { index, s ->
            alarmClocksList.add(AlarmClock(s, idArray!![index]))
        }
        val listAdapter = ListAdapter(this, alarmClocksList)
        alarmClocksLV.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }
}