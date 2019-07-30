package com.example.quizup.Testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.example.quizup.Constants
import com.example.quizup.R

import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.connectButton
import kotlinx.android.synthetic.main.activity_main.disConnectButton
import kotlinx.android.synthetic.main.activity_main.temp_textView
import kotlinx.android.synthetic.main.activity_random_play_bot.*
import org.json.JSONObject
import java.net.URISyntaxException
import java.sql.Timestamp
import java.util.*

class RandomPlayBot : AppCompatActivity() {

    var socket : Socket ? = null
    val TAG : String = "Socket"
    var hasAnswered : Boolean = true

    var onConnect  = Emitter.Listener {
        Log.e(TAG, "connected !!")
        //socket?.emit(Constants.quizClass.EVENT_START)
        socket?.emit("check_login")
    }
    var getsTOKEN = Emitter.Listener {
        Log.e(TAG, "token : " + it[0])

        if(it[0] as Boolean){
            //Log.e(TAG, "it is true")
            socket?.emit("token", "a_1")

        }else{
            socket?.emit(Constants.quizClass.EVENT_START)
        }

    }
    var getValidity = Emitter.Listener {
        Log.e(TAG, "validity : " + it[0].toString())
        if(it[0] as Boolean){
            socket?.emit(Constants.quizClass.EVENT_START)
        }else{
            Log.e(TAG, "fdiconnect")
            socket?.disconnect()
            socket?.off()
        }
    }
    var onDissconnect = Emitter.Listener {
        Log.e(TAG, "disconnected !!")
    }
    var onEventConnectError = Emitter.Listener {
        Log.e(TAG, "connection error !!")

    }
    var onEventTimeout = Emitter.Listener {
        Log.e(TAG, "connection timeout !!")
    }

    var updateScores = Emitter.Listener {
        Log.e(TAG, "scores recieved : "+it[0].toString())
        //lin3TextView.text = "Score : " + it[0].toString()
        runOnUiThread{
            lin3TextView.text = "Score : " + it[0].toString()
        }
    }

    var correctAnswer = Emitter.Listener{
        Log.e(TAG, "Answer : "+ it[0].toString())
        runOnUiThread{
            temp_textView.append(it[0].toString()+"\n")
        }
    }


    var getsMessage = Emitter.Listener {
        runOnUiThread {
            Log.e(TAG, "server pushed a message !" + it[0].toString())
            temp_textView.append(it[0].toString()+"\n")
        }
    }
    var onEnd = Emitter.Listener {
        runOnUiThread{
            socket?.emit("disconnect")
            socket?.disconnect()
            socket?.off()
            Log.e(TAG, "disConnected !!" )
            runOnUiThread { temp_textView.setText("") }

        }
    }


    var pushQuestion = Emitter.Listener {
        runOnUiThread {
            Log.e(TAG, "question"+it[0].toString())

            var questionJson : JSONObject =  JSONObject(it[0].toString())
            temp_textView.append(questionJson[Constants.quizClass.JSON_TAG_QUESTION].toString()+"\n")

            hasAnswered = false
            questionTextView.text = questionJson[Constants.quizClass.JSON_TAG_QUESTION].toString();
            optionA.text = questionJson.getJSONObject(Constants.quizClass.JSON_TAG_OPTIONS)["0"].toString()
            optionB.text = questionJson.getJSONObject(Constants.quizClass.JSON_TAG_OPTIONS)["1"].toString()
            optionC.text = questionJson.getJSONObject(Constants.quizClass.JSON_TAG_OPTIONS)["2"].toString()
            optionD.text = questionJson.getJSONObject(Constants.quizClass.JSON_TAG_OPTIONS)["3"].toString()

            var currTimeSystem = System.currentTimeMillis()
            GLOBAL_CLOCK.cancel()
            Constants.quizClass.QUESTION_TIME_ALLOTTED =
                ( questionJson[Constants.quizClass.JSON_TAG_QUESTION_TIME_PERIOD].toString()).toLong()

            GLOBAL_CLOCK.start()

            optionA.setOnClickListener{
                if(!hasAnswered){
                    Toast.makeText(this, optionA.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(Constants.quizClass.JSON_TAG_OPTION, 0)
                    currResponse.put(Constants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(Constants.quizClass.EVENT_USER_OPTIONS, currResponse)
                    GLOBAL_CLOCK.cancel()

                }
            }
            optionB.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionB.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(Constants.quizClass.JSON_TAG_OPTION, 1)
                    currResponse.put(Constants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(Constants.quizClass.EVENT_USER_OPTIONS, currResponse)
                    GLOBAL_CLOCK.cancel()
                }
            }
            optionC.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionC.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(Constants.quizClass.JSON_TAG_OPTION, 2)
                    currResponse.put(Constants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(Constants.quizClass.EVENT_USER_OPTIONS, currResponse)
                    GLOBAL_CLOCK.cancel()

                }
            }
            optionD.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionD.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(Constants.quizClass.JSON_TAG_OPTION, 3)
                    currResponse.put(Constants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem  )
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(Constants.quizClass.EVENT_USER_OPTIONS, currResponse)
                    GLOBAL_CLOCK.cancel()

                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_play_bot)

        try {


            connectButton.setOnClickListener{

                // http://d8309053.ngrok.io/
                socket = IO.socket(Constants.quizClass.BASE_URL, IO.Options())
                if(!socket!!.connected()){
                    socket?.connect()
                    socket?.on(Socket.EVENT_CONNECT, onConnect)
                    socket?.on(Socket.EVENT_DISCONNECT, onDissconnect)
                    socket?.on(Socket.EVENT_CONNECT_ERROR, onEventConnectError)
                    socket?.on(Socket.EVENT_CONNECT_TIMEOUT, onEventTimeout)



                    socket?.on(Constants.quizClass.EVENT_MESSAGE, getsMessage)
                    socket?.on(Constants.quizClass.EVENT_QUESTION, pushQuestion)
                    socket?.on(Constants.quizClass.EVEENT_SCORE, updateScores)
                    socket?.on(Constants.quizClass.EVENT_END, onEnd)
                    socket?.on(Constants.quizClass.EVENT_CORRECT_ANSWER, correctAnswer)


                    socket?.on("TOKEN", getsTOKEN);
                    socket?.on("validity", getValidity)

                    disConnectButton.setOnClickListener{
                        socket?.disconnect()
                        socket?.off()
                        Log.e(TAG, "disConnected !!" )
                        runOnUiThread { temp_textView.setText("") }
                    }

                }
            }



        } catch (e : URISyntaxException) {
            Log.e(TAG, e.toString())
            Toast.makeText(this, "< Exception : >"+e.toString(), Toast.LENGTH_LONG).show()
        }





    }


    public val GLOBAL_CLOCK = object : CountDownTimer(

        Constants.quizClass.QUESTION_TIME_ALLOTTED, Constants.quizClass.CLOCK_TIME_REFRESH_RATE

    ){
        override fun onFinish() {
            Log.e(TAG, "Time up");
        }

        override fun onTick(millisUntilFinished : Long) {
            global_timer.text = (millisUntilFinished/1000).toString()
            //Log.e(TAG, (millisUntilFinished/1000).toString())
        }
    }





}
