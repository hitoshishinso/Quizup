package com.example.quizup.FinalPackage.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizup.FinalPackage.RootActivity
import com.example.quizup.FinalPackage.fConstants

import com.example.quizup.R
import kotlinx.android.synthetic.main.fragment_timer.*
import java.lang.ClassCastException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TimerFragment : Fragment() {

    fun onDisconnect(){
        val restartIntent : Intent = Intent(context, RootActivity :: class.java)
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(restartIntent)
    }

    val threeSecondTimer = object : CountDownTimer(
        fConstants.essentials.MAX_TIMER_FRAG, fConstants.essentials.MAX_TIMER_FRAG_REFRESH
    ){
        override fun onFinish() {
            respose.timerFragmentKillMe("{}")
        }

        override fun onTick(milliSecondsLeft: Long) {
            activity!!.runOnUiThread{
                treeSecondTextView.text = ((milliSecondsLeft/1000)+1).toString()
            }
        }

    }

    lateinit var respose : timerFragmentRespose ;

    public interface timerFragmentRespose{
        public fun timerFragmentKillMe(jsonResponse : String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            respose = context as timerFragmentRespose
        }catch (e : ClassCastException){}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_timer, container, false)


        activity!!.runOnUiThread{
            threeSecondTimer.start()
        }

        return rootView
    }


}
