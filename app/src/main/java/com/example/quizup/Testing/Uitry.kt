package com.example.quizup.Testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import com.example.quizup.Constants
import com.example.quizup.R
import kotlinx.android.synthetic.main.activity_random_play_bot.*
import kotlinx.android.synthetic.main.activity_uitry.*

class Uitry : AppCompatActivity() {

    val TAG : String = "TAG"
    var x = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uitry)
        GLOBAL_CLOCK.start()
        //GLOBAL_CLOCK.cancel()
        GLOBAL_CLOCK.start()
        //GLOBAL_CLOCK.cancel()
    }

    public val GLOBAL_CLOCK = object : CountDownTimer(

        5000, 50
    ){
        override fun onFinish() {
            Log.e(TAG, "Time up");
        }

        override fun onTick(millisUntilFinished : Long) {
            seekerThreadUi.progress += 1
        }
    }
}
