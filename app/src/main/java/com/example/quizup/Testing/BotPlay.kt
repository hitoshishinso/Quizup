package com.example.quizup.Testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.quizup.Constants
import com.example.quizup.PvpConstants
import com.example.quizup.R

import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.connectButton
import kotlinx.android.synthetic.main.activity_main.disConnectButton
import kotlinx.android.synthetic.main.activity_main.temp_textView
import kotlinx.android.synthetic.main.activity_pvp_play.*
import kotlinx.android.synthetic.main.activity_random_play_bot.*
import kotlinx.android.synthetic.main.activity_random_play_bot.lin3TextView
import kotlinx.android.synthetic.main.activity_random_play_bot.optionA
import kotlinx.android.synthetic.main.activity_random_play_bot.optionB
import kotlinx.android.synthetic.main.activity_random_play_bot.optionC
import kotlinx.android.synthetic.main.activity_random_play_bot.optionD
import kotlinx.android.synthetic.main.activity_random_play_bot.questionTextView
import org.json.JSONObject
import java.net.URISyntaxException
import java.sql.Timestamp
import java.util.*

class BotPlay: AppCompatActivity() {

    var socket : Socket ? = null
    val TAG : String = "SocketExceptions"
    var hasAnswered : Boolean = true





    var onConnect  = Emitter.Listener {
        Log.e(TAG, "connected !!")
        //socket?.emit(Constants.quizClass.EVENT_START)

        //push questions
        //render questions
        // look if question is END
        // then disconnect and got to scores


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
            temp_textView.append(questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString()+"\n")
            /*if(questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString().equals("END")){
                socket?.disconnect()
                socket?.off()
                Log.e(TAG, "disConnected !!" )
                runOnUiThread { temp_textView.setText("") }
            }*/

            //temp_textView.append(it[0].toString()+"\n")
            hasAnswered = false
            questionTextView.text = questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString();
            optionA.text = questionJson.getJSONObject("options")["0"].toString()
            optionB.text = questionJson.getJSONObject("options")["1"].toString()
            optionC.text = questionJson.getJSONObject("options")["2"].toString()
            optionD.text = questionJson.getJSONObject("options")["3"].toString()

            //var currMil = System.currentTimeMillis()

            var currTimeSystem = System.currentTimeMillis()

            optionA.setOnClickListener{
                if(!hasAnswered){
                    Toast.makeText(this, optionA.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 0)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)


                }
            }
            optionB.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionB.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 1)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }
            optionC.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionC.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 2)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }
            optionD.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(this, optionD.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 3)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot_play)

        //progressbar.visibility = View.VISIBLE

        try {





            connectButton.setOnClickListener{

                // http://d8309053.ngrok.io/
                socket = IO.socket(PvpConstants.quizClass.BASE_URL_2, IO.Options())
                if(!socket!!.connected()){
                    socket?.connect()
                    socket?.on(Socket.EVENT_CONNECT, onConnect)
                    socket?.on(Socket.EVENT_DISCONNECT, onDissconnect)
                    socket?.on(Socket.EVENT_CONNECT_ERROR, onEventConnectError)
                    socket?.on(Socket.EVENT_CONNECT_TIMEOUT, onEventTimeout)



                    socket?.on(PvpConstants.quizClass.EVENT_MESSAGE, getsMessage)
                    socket?.on(PvpConstants.quizClass.EVENT_QUESTION, pushQuestion)
                    socket?.on(PvpConstants.quizClass.EVEENT_SCORE, updateScores)
                    socket?.on("END", onEnd)

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
}
