package com.example.quizup.FinalPackage.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizup.FinalPackage.RootActivity
import com.example.quizup.FinalPackage.fConstants
import com.example.quizup.PvpConstants

import com.example.quizup.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.temp_textView
import kotlinx.android.synthetic.main.activity_random_play_bot.*
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class QuizFragment : Fragment() {

    val TESTING_URI = "https://robohash.org/asdfjoa?set=set2"
    val TESTING_URI_2 = "https://robohash.org/tadsjlfj?set=set2"

    fun onDisconnect(){
        val restartIntent : Intent = Intent(context, RootActivity :: class.java)
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(restartIntent)
    }

    val TAG :String = fConstants.essentials.SOCKET_TESTING


    public fun testQuestion(s : String){

        activity!!.runOnUiThread {
            Log.e(fConstants.essentials.FRAGMENT_ERROR_TAG, "everything : "+s)
            temp_textView.append(s)
            var hasAnswered = false

            var questionJson : JSONObject =  JSONObject(s.toString())
            temp_textView.append(questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString()+"\n")
            /*if(questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString().equals("END")){
                socket?.disconnect()
                socket?.off()
                Log.e(TAG, "disConnected !!" )
                runOnUiThread { temp_textView.setText("") }
            }*/

            //temp_textView.append(it[0].toString()+"\n")
            questionTextView.text = questionJson[PvpConstants.quizClass.JSON_TAG_QUESTION].toString();
            optionA.text = questionJson.getJSONObject("options")["0"].toString()
            optionB.text = questionJson.getJSONObject("options")["1"].toString()
            optionC.text = questionJson.getJSONObject("options")["2"].toString()
            optionD.text = questionJson.getJSONObject("options")["3"].toString()

            //var currMil = System.currentTimeMillis()

            var currTimeSystem = System.currentTimeMillis()

            optionA.setOnClickListener{
                if(!hasAnswered){
                    Toast.makeText(context, optionA.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 0)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    //socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)


                }
            }
            optionB.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(context, optionB.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 1)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    //socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }
            optionC.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(context, optionC.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 2)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    //socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }
            optionD.setOnClickListener{
                if(!hasAnswered){
                    hasAnswered = true
                    Toast.makeText(context, optionD.text, Toast.LENGTH_LONG).show()
                    hasAnswered = true
                    var currResponse : JSONObject = JSONObject()
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_OPTION, 3)
                    currResponse.put(PvpConstants.quizClass.JSON_TAG_TIMESTAMP, System.currentTimeMillis()-currTimeSystem)
                    Log.e(TAG, currResponse.toString())
                    //socket?.emit(PvpConstants.quizClass.EVENT_USER_OPTIONS, currResponse)

                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_quiz, container, false)


        Picasso
            .with(context)
            .load(TESTING_URI)
            .into(rootView.myProfilePicture)


        Picasso
            .with(context)
            .load(TESTING_URI_2)
            .into(rootView.oppProfilePicture)

        return rootView
    }


}
