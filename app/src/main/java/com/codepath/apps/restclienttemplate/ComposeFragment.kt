package com.codepath.apps.restclienttemplate

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.Time


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [ComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComposeFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var client : TwixClient
    var content: String = ""
    lateinit var etContent : EditText
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("Fragment", "Dismissed")
        (activity as TimeLineActivity).currentContent = etContent.text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        client = (activity as TimeLineActivity).client
        etContent  = view.findViewById(R.id.etContent)
        val vtCounter : TextView = view.findViewById(R.id.vtCounter)
        val btClear : Button = view.findViewById(R.id.btClear)
        val btTweet : Button = view.findViewById(R.id.btTweet)
        var counter : Int = 0
        etContent.setText(param1)
        vtCounter.setText(etContent.text.toString().length.toString() +"/280")
        if (etContent.text.toString().length>280){
            vtCounter.setTextColor(Color.RED)
            btTweet.isEnabled=false
        }
        btClear.setOnClickListener {
            etContent.setText("")
        }
        etContent.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                counter = p0.toString().length
                if (counter > 280){
                    vtCounter.setTextColor(Color.RED)
                    btTweet.isEnabled=false
                }else{
                    vtCounter.setTextColor(Color.BLACK)
                }
                vtCounter.setText(counter.toString()+"/280")
            }
        })
        btTweet.setOnClickListener {
            client.publishTweet(etContent.text.toString(),object: JsonHttpResponseHandler(){
                override fun onFailure(
                    statusCode: Int,
                    headers: okhttp3.Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.d("Fragment","onFailure")
                }

                override fun onSuccess(statusCode: Int, headers: okhttp3.Headers?, json: JSON?) {
                    Log.d("Fragment", json.toString())
                    try {
                        val result: JSONObject = json?.jsonObject ?: JSONObject()
                        val tweet = Tweet.fromJson(result)
                        etContent.setText("")
                        (activity as TimeLineActivity).tweets.add(0,tweet)

                    }catch(e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            dismiss()
        }
        dialog?.setTitle("Compose New Tweet")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComposeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ComposeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}