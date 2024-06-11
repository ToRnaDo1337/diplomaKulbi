package com.example.ctrlbee.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedTimerViewModel : ViewModel() {
    private val _secondsElapsed = MutableLiveData<Long>()
    val secondsElapsed: LiveData<Long>
        get() = _secondsElapsed

    private var timer: Handler? = null
    var isTimerRunning = false
    private var lastTickTimestamp: Long = 0

    init {
        _secondsElapsed.value = 0L
    }

    fun startTimer() {
        if (!isTimerRunning) {
            lastTickTimestamp = System.currentTimeMillis()
            timer = Handler(Looper.getMainLooper())
            timer?.post(object : Runnable {
                override fun run() {
                    val currentTimestamp = System.currentTimeMillis()
                    val elapsedMillis = currentTimestamp - lastTickTimestamp
                    lastTickTimestamp = currentTimestamp
                    _secondsElapsed.value = (_secondsElapsed.value ?: 0) + elapsedMillis / 1000
                    timer?.postDelayed(this, 1000)
                }
            })
            isTimerRunning = true
        }
    }

    fun pauseTimer() {
        if (isTimerRunning) {
            timer?.removeCallbacksAndMessages(null)
            isTimerRunning = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        pauseTimer()
    }
}
