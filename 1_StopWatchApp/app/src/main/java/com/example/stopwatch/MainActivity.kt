package com.example.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var serviceIntent: Intent
    private var time = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    fun startTimer(view: View) {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time);
        startService(serviceIntent)
    }

    fun pauseTimer(view: View) {
        stopService(serviceIntent)
    }

    fun resetTimer(view: View) {
        stopService(serviceIntent)
        time = 0.0;
        binding.tvTimer.text = getTimeStringFromDouble(time)

        // In case you would like to start the timer after resetBtn has been clicked, then uncomment this section
        /*Timer("SettingUp", false).schedule(500) {
            serviceIntent.putExtra(TimerService.TIMER_EXTRA, time);
            startService(serviceIntent)
        }*/
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
            binding.tvTimer.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt();
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60;
        val seconds =  resultInt % 86400 % 3600 % 60;

        return makeTimeString(hours, minutes, seconds)

    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)
}