package com.example.quizup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.quizup.FinalPackage.ConnectActivity
import com.example.quizup.FinalPackage.RootActivity
import com.example.quizup.Homie.Categories
import com.example.quizup.Homie.QuizMain
import com.example.quizup.Testing.Uitry
import com.example.quizup.Testing.WhoToPlay
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URISyntaxException
import java.util.*

class MainActivity : AppCompatActivity() {

    var socket : Socket ? = null
    val TAG : String = "SocketExceptions"

    var onConnect  = Emitter.Listener {
        Log.e(TAG, "connected !!")
        socket?.emit(Constants.quizClass.EVENT_START)

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



    var getsMessage = Emitter.Listener {
        runOnUiThread {
            Log.e(TAG, "server pushed a message !" + it[0].toString())
            temp_textView.append(it[0].toString()+"\n")
        }
    }

    var pushQuestion = Emitter.Listener {
        runOnUiThread {
            Log.e(TAG, "question"+it[0].toString())
            temp_textView.append(it[0].toString()+"\n")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       /*val tempUIIntent : Intent = Intent(this, Categories::class.java)
        startActivity(tempUIIntent)
        finish()*/

        /*val tempWhoToPlay : Intent = Intent(this, WhoToPlay::class.java)
        startActivity(tempWhoToPlay)
        finish()
*/
        /*val uitryIntent : Intent = Intent(this, Uitry::class.java)
        startActivity(uitryIntent)
        finish()*/
        val connectIntent : Intent= Intent(this, RootActivity :: class.java)
        startActivity(connectIntent)
        finish()


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
