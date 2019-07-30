package com.example.quizup.Homie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizup.R
import org.greenrobot.eventbus.EventBus

class Categories : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val temp : String = "{'name' : 'karan' }"

        EventBus.getDefault().post(
            dataBottle(temp)
        )

        for (i in 1..10){
            EventBus.getDefault().postSticky(
                dataBottle(temp+"sticky !!!")
            )
        }



    }
}
