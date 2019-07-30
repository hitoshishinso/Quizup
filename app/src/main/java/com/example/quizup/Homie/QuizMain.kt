package com.example.quizup.Homie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.quizup.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class QuizMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_main)


        startActivity(Intent(
            this, Categories :: class.java
        ))




    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(event : dataBottle){
        Log.e("TESTING", event.jsonInfo)
        Toast.makeText(this, event.jsonInfo, Toast.LENGTH_LONG).show()

        //EventBus.getDefault().post(dataBottle("dun dun dun !"))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }





}
