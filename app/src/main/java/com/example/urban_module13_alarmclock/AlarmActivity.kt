package com.example.urban_module13_alarmclock

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.system.exitProcess

class AlarmActivity : AppCompatActivity() {

    private lateinit var stopAlarmBTN: Button
    private lateinit var alarmToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarmToolbar = findViewById(R.id.alarmToolbar)
        setSupportActionBar(alarmToolbar)
        title = getString(R.string.toolbar_title)

        stopAlarmBTN = findViewById(R.id.stopAlarmBTN)
        stopAlarmBTN.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuExit -> finishAffinity()
        }
        return true
    }
}