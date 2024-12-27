package com.example.urban_module13_alarmclock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

class ListAdapter(context: Context, alarmClocksList: MutableList<AlarmClock>):
    ArrayAdapter<AlarmClock>(context, R.layout.list_item, alarmClocksList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val alarmClock = getItem(position)
        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val itemTimeTV = view?.findViewById<TextView>(R.id.itemTimeTV)

        itemTimeTV?.text = alarmClock?.time
        return view!!
    }
}