package com.example.quizup

import android.os.CountDownTimer

class Constants{

    class quizClass{
        companion object{
            val BASE_URL : String = "https://wscricquiz.bitbns.com/"
            val EVENT_CORRECT_ANSWER = "answer"
            val EVENT_QUESTION = "question";
            val EVEENT_SCORE = "score"
            val EVENT_USER_OPTIONS = "userOption"
            val EVENT_START = "start"
            val EVENT_MESSAGE = "message"
            val EVENT_END = "END"



            val JSON_TAG_OPTION = "option"
            val JSON_TAG_TIMESTAMP = "timestamp"
            val JSON_TAG_QUESTION  = "question"
            val JSON_TAG_OPTIONS = "options"
            val JSON_TAG_QUESTION_TIME_PERIOD = "T"


            var QUESTION_TIME_ALLOTTED : Long = 10000
            var CLOCK_TIME_REFRESH_RATE : Long = 1000





        }
    }
}