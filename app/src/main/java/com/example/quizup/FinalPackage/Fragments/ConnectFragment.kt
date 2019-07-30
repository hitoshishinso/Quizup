package com.example.quizup.FinalPackage.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizup.FinalPackage.RootActivity
import com.example.quizup.FinalPackage.fConstants

import com.example.quizup.R
import kotlinx.android.synthetic.main.activity_bot_play.*
import kotlinx.android.synthetic.main.fragment_connect.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.ClassCastException
import org.greenrobot.eventbus.ThreadMode
import com.example.quizup.FinalPackage.bus.socketConnectionBus
import com.github.nkzawa.socketio.client.Socket


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ConnectFragment : Fragment() {

    fun onDisconnect(){
        val restartIntent : Intent = Intent(context, RootActivity :: class.java)
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(restartIntent)
    }



    //fragment activity connection

    //response starts
    public interface connectFragmentRespone{
        public fun connectFragmentKillMe(jsonResponse : String){}

    }



    lateinit var response: connectFragmentRespone;
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            response = context as connectFragmentRespone
        }catch (e : ClassCastException){}
    }
    //response ends




    //timer progressbar
    val PROGRESS_BAR_TIMER = object : CountDownTimer(
        fConstants.essentials.MAX_PLAYER_SEARCH_TIMEOUT, fConstants.essentials.PROGRESS_BAR_REFRESH_RATE){
        override fun onFinish() {
            Log.e(fConstants.essentials.TIMER_CHECK_TAG, "Search Progress Bar")
            response.connectFragmentKillMe("{}")
        }

        override fun onTick(remainingMilliSeconds: Long) {
           // Log.e(fConstants.essentials.TIMER_CHECK_TAG, (remainingMilliSeconds/1000).toString())
           activity!!.runOnUiThread{
               match_progress_bar.progress+= 2
           }
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val thisRootView = inflater.inflate(R.layout.fragment_connect, container, false);
        //respose.someEvent("i  am a fragment")
        activity!!.runOnUiThread{
            PROGRESS_BAR_TIMER.start()
        }


        EventBus.getDefault().register(this)

        return thisRootView


    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: socketConnectionBus) {
        Log.e("TESTING", event.socketInstance+ " Socket instance recieved in the fragment !!")

    }


}
