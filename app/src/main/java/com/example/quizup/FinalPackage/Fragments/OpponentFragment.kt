package com.example.quizup.FinalPackage.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.quizup.FinalPackage.RootActivity
import com.example.quizup.FinalPackage.fConstants

import com.example.quizup.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_opponent.*
import kotlinx.android.synthetic.main.fragment_opponent.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OpponentFragment : Fragment() {

    fun onDisconnect(){
        val restartIntent : Intent = Intent(context, RootActivity :: class.java)
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(restartIntent)
    }


    val TESTING_URI = "https://robohash.org/2?set=set2"

    lateinit var response : opponentFragmentResponse

    public interface opponentFragmentResponse{
        public fun opponentFragmentKillMe(jsonResponse : String){}
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            response = context as opponentFragmentResponse
        }catch (e : ClassCastException){}
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView :View = inflater.inflate(R.layout.fragment_opponent, container, false)
        //Picasso.with(context).load(TESTING_URI).into(opponentProfilePicture)
        Log.e(fConstants.essentials.FRAGMENT_ERROR_TAG, context.toString())
        activity!!.runOnUiThread{
            Picasso
                .with(context)
                .load(TESTING_URI)
                .into(rootView.findViewById(R.id.opponentProfilePicture) as ImageView)


            rootView.startMatchButton.setOnClickListener{
                response.opponentFragmentKillMe("{}")
            }

        }

        return rootView
    }


}
