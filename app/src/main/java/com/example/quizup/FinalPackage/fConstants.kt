package com.example.quizup.FinalPackage

class fConstants{

    class multiplayer{
        companion object{
            val BASE_URL : String = "https://wscricquizpvp.bitbns.com/"
            val EVENT_CORRECT_ANSWER :String = "correct"
            val EVENT_QUESTION :String= "question";
            val EVEENT_SCORE :String= "score"
            val EVENT_USER_OPTIONS :String= "userOption"
            val EVENT_START :String= "start"
            val EVENT_MESSAGE :String= "message"
            val EVENT_READY = "ready"
            val EVENT_END = "END"

            val JSON_TAG_OPTION :String= "option"
            val JSON_TAG_TIMESTAMP :String= "timestamp"
            val JSON_TAG_QUESTION  :String= "question"

        }
    }

    class botplayer{
        companion object{
            val BASE_URL :String = "https://wscricch.bitbns.com/"
            val EVENT_CORRECT_ANSWER :String= "correct"
            val EVENT_QUESTION :String= "question";
            val EVEENT_SCORE :String= "score"
            val EVENT_USER_OPTIONS :String= "userOption"
            val EVENT_START :String= "start"
            val EVENT_MESSAGE :String= "message"
            val EVENT_END = "END"


            val JSON_TAG_OPTION :String= "option"
            val JSON_TAG_TIMESTAMP :String= "timestamp"
            val JSON_TAG_QUESTION  :String= "question"
        }
    }

    class essentials{
        companion object{
            val MAX_PLAYER_SEARCH_TIMEOUT : Long = 10000
            val PROGRESS_BAR_REFRESH_RATE : Long = 18
            val TIMER_CHECK_TAG : String = "TimerCheckTag"
            val FRAGMENT_ERROR_TAG : String = "FragmentErrorTag"
            val SOCKET_TESTING : String = "socketTesting"
            val MAX_TIMER_FRAG : Long = 3000
            val MAX_TIMER_FRAG_REFRESH : Long = 1000
        }
    }


}