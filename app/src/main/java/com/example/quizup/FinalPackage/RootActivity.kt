package com.example.quizup.FinalPackage

import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.quizup.Constants
import com.example.quizup.FinalPackage.Fragments.ConnectFragment
import com.example.quizup.FinalPackage.Fragments.OpponentFragment
import com.example.quizup.FinalPackage.Fragments.QuizFragment
import com.example.quizup.FinalPackage.Fragments.TimerFragment
import com.example.quizup.FinalPackage.bus.socketConnectionBus
import com.example.quizup.PvpConstants
import com.example.quizup.R
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.disConnectButton
import kotlinx.android.synthetic.main.activity_main.temp_textView
import kotlinx.android.synthetic.main.activity_random_play_bot.*
import kotlinx.android.synthetic.main.activity_root.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class RootActivity : AppCompatActivity(), ConnectFragment.connectFragmentRespone, OpponentFragment.opponentFragmentResponse

,TimerFragment.timerFragmentRespose{


    //values
    val rootFrameLayout  = R.id.root_frame_view
    var startFlag = false
    //variables
    var socket : Socket  ? = null
    var startMultiplayer = false
    lateinit var connectFragment: ConnectFragment
    lateinit var opponentFragment: OpponentFragment
    lateinit var timerFragment: TimerFragment
    lateinit var quizFragment: QuizFragment

    // listeners starts
    var onConnect  = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "connected !!")
        socket?.emit("check_login")

    }

    var getTOKEN = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "TOKEN : "+it[0].toString())
        if(it[0] as Boolean){
            socket?.emit("token", "a_3")
        }else{
            startFlag = true
        }
    }

    var getValidity = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "Validity  : "+it[0].toString())
        if(it[0] as Boolean){
            startFlag = true
        }else{
            socket?.disconnect()
            socket?.off()
        }
    }

    var onMultiplayerReady = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "other player ready")
        runOnUiThread{startMultiplayer = true}
    }
    var onDissconnect = Emitter.Listener {

        Log.e(fConstants.essentials.SOCKET_TESTING, "disconnected !!")
        runOnUiThread {
            var currFrag= supportFragmentManager.findFragmentById(rootFrameLayout)
                Log.e(fConstants.essentials.FRAGMENT_ERROR_TAG, currFrag.toString())
        }



    }
    var onEventConnectError = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "connection error !!")

    }
    var onEventTimeout = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "connection timeout !!")
    }

    var updateScores = Emitter.Listener {
        Log.e(fConstants.essentials.SOCKET_TESTING, "scores recieved : "+it[0].toString())
        runOnUiThread{

        }
    }

    var getsMessage = Emitter.Listener {
        runOnUiThread {
            Log.e(fConstants.essentials.SOCKET_TESTING, "server pushed a message !" + it[0].toString())
            //temp_textView.append(it[0].toString()+"\n")
        }
    }
    var onEnd = Emitter.Listener {
        runOnUiThread{
            socket?.emit("disconnect")
            socket?.disconnect()
            socket?.off()
            Log.e(fConstants.essentials.SOCKET_TESTING, "disConnected !!" )
            runOnUiThread {  }

        }
    }


    var pushQuestion = Emitter.Listener {
        runOnUiThread {
            Log.e(fConstants.essentials.SOCKET_TESTING, "question"+it[0].toString())
            quizFragment.testQuestion(it[0].toString())

        }
    }

    //listeners ends


    //fragments switching function starts
    override fun connectFragmentKillMe(jsonResponse : String) {
        Log.e(fConstants.essentials.FRAGMENT_ERROR_TAG, "killed  connected")
        if(supportFragmentManager.findFragmentById(rootFrameLayout) != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(supportFragmentManager.findFragmentById(rootFrameLayout)!!)
                .commit()

            supportFragmentManager
                .beginTransaction()
                .replace(rootFrameLayout,opponentFragment)
                .commit()
        }

        if(!startMultiplayer){
            // head to pvp fragment
            //socket?.emit(fConstants.multiplayer.EVENT_START)
            Log.e(fConstants.essentials.SOCKET_TESTING, "connection switched to bot")
            switchUrlOnSocket(fConstants.botplayer.BASE_URL)
        }




        Log.e(fConstants.essentials.SOCKET_TESTING, startMultiplayer.toString())
    }

    override fun opponentFragmentKillMe(jsonResponse: String) {
        Log.e(fConstants.essentials.FRAGMENT_ERROR_TAG, "killed opponent")
        supportFragmentManager
            .beginTransaction()
            .remove(opponentFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(rootFrameLayout, timerFragment)
            .commit()

    }

    override fun timerFragmentKillMe(jsonResponse: String) {

        supportFragmentManager
            .beginTransaction()
            .remove(timerFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(rootFrameLayout, quizFragment)
            .commit()


       /* val temp = supportFragmentManager.findFragmentById(R.id.root_frame_view) as QuizFragment
        temp.testQuestion()
*/

        if(startFlag){
            Log.e(fConstants.essentials.SOCKET_TESTING, "start called !")
            socket?.emit(fConstants.multiplayer.EVENT_START)
        }

        //socket?.emit(fConstants.multiplayer.EVENT_START)

    }

    //fragments switching function ends


    public fun init(){
        connectFragment = ConnectFragment()
        opponentFragment= OpponentFragment()
        timerFragment = TimerFragment()
        quizFragment = QuizFragment()

    }


    // socket swithcing function
    public fun switchUrlOnSocket(url : String ){

        socket?.disconnect()
        socket?.off()
        socket?.close()
        Log.e(fConstants.essentials.SOCKET_TESTING, "manter : " + socket?.connected().toString())


        socket = IO.socket(url, IO.Options())
        if(!socket!!.connected()){
            socket?.connect()
            socket?.once(Socket.EVENT_CONNECT, onConnect)
            socket?.on(Socket.EVENT_DISCONNECT, onDissconnect)
            socket?.on(Socket.EVENT_CONNECT_ERROR, onEventConnectError)
            socket?.on(Socket.EVENT_CONNECT_TIMEOUT, onEventTimeout)

            socket?.on(fConstants.multiplayer.EVENT_MESSAGE, getsMessage)
            socket?.on(fConstants.multiplayer.EVENT_QUESTION, pushQuestion)
            socket?.on(fConstants.multiplayer.EVEENT_SCORE, updateScores)
            socket?.on(fConstants.multiplayer.EVENT_END, onEnd)
            socket?.on(fConstants.multiplayer.EVENT_READY, onMultiplayerReady)
            socket?.on("TOKEN", getTOKEN)
            socket?.on("validity", getValidity)


        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        switchUrlOnSocket(fConstants.multiplayer.BASE_URL)


        runOnUiThread {



            //#init everything
            init()

            //#start the opponent search
            supportFragmentManager
                .beginTransaction()
                .replace(rootFrameLayout, connectFragment)
                .commit()


            //#connect to multiplayer

        }
    }



    override fun onDestroy() {


        val temp = supportFragmentManager.findFragmentById(rootFrameLayout)
        Log.e("Current Fragment :  " , temp.toString())

        super.onDestroy()
    }

    override fun onStop() {
        socket?.off()
        socket?.disconnect()
        socket?.close()



        super.onStop()


    }





}
