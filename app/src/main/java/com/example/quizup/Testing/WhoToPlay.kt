package com.example.quizup.Testing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizup.R
import kotlinx.android.synthetic.main.activity_who_to_play.*


class WhoToPlay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_who_to_play)


        playRandomButton.setOnClickListener{

            startActivity(Intent(
             this, RandomPlayBot :: class.java
            ))

            finish()



        }

        playFirendButton.setOnClickListener{
            startActivity(Intent(
                this, PvpPlay :: class.java
            ))

            finish()

        }

        playBotButton.setOnClickListener{
            startActivity(Intent(
                this, BotPlay :: class.java
            ))

            finish()

        }


    }
}
