package com.example.pomodorotimer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val textViewRemainMinutes: TextView by lazy {
        findViewById(R.id.textViewRemainMinutes)
    }

    private val textViewRemainSeconds: TextView by lazy {
        findViewById(R.id.textViewRemainSeconds)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private var currentCountDownTimer: CountDownTimer? = null

    private val soundPool = SoundPool.Builder().build()

    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSound()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            updateRemainTime(progress * 60 * 1000L)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        currentCountDownTimer?.cancel()
                        currentCountDownTimer = null
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        seekBar ?: return
                        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L).start()
                        currentCountDownTimer?.start()
                    }

                }
        )
    }

    private fun createCountDownTimer(initialMillis: Long): CountDownTimer =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemainTime(0)
                updateSeekBar(0)
            }

        }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        textViewRemainSeconds.text = "%02d".format(remainSeconds / 60)
        textViewRemainMinutes.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 60 / 1000).toInt()
    }

    private fun initSound() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }
}