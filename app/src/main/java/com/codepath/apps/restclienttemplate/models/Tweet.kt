package com.codepath.apps.restclienttemplate.models

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.parceler.Parcel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
@Parcel
public class Tweet constructor(jsonObj: JSONObject) {
    constructor():this(JSONObject())
    @JvmField public var content: String = ""
    @JvmField public var id: Number = 0
    @JvmField public var photos: MutableList<String> = mutableListOf<String>()
    @JvmField var time : String = ""
    public fun getTime():String{
        var xtime = ""
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val format = SimpleDateFormat(twitterFormat, Locale.US)
        format.isLenient=true
        try{
            val diff = (System.currentTimeMillis() - format.parse(time).time) / 1000
            if (diff < 5)
                xtime = "Just now"
            else if (diff < 60)
                xtime = String.format(Locale.ENGLISH, "%ds",diff)
            else if (diff < 60 * 60)
                xtime = String.format(Locale.ENGLISH, "%dm", diff / 60)
            else if (diff < 60 * 60 * 24)
                xtime = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60))
            else if (diff < 60 * 60 * 24 * 30)
                xtime = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24))
            else {
                val now = Calendar.getInstance();
                val then = Calendar.getInstance();

                then.time = format.parse(time)
                if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                    time = (then.get(Calendar.DAY_OF_MONTH)).toString() + " " + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                } else {
                    time = (then.get(Calendar.DAY_OF_MONTH)).toString() + " " + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + (then.get(Calendar.YEAR) - 2000).toString();
                }
            }
        }catch(e: ParseException){
            e.printStackTrace()
        }
        return xtime
    }
    @JvmField public var user : User = User()
    companion object{
        fun fromJson(jsonObj: JSONObject) : Tweet{
            var tweet : Tweet = Tweet()
            tweet.content = jsonObj.getString("text")
            tweet.time = jsonObj.getString("created_at")
            tweet.user = User.fromJson(jsonObj.getJSONObject("user"))
            tweet.id = jsonObj.getLong("id")
            try{
                val entities = jsonObj.getJSONObject("entities")
                val media = entities?.getJSONArray("media")?:JSONArray()
                for (i in 0 until (media.length()-1)){
                    if (media.getJSONObject(i).getString("type") == "photo")
                        tweet.photos.add(media.getJSONObject(i).getString("media_url_https"))
                }
            }catch (e: JSONException){
                Log.d("Tweet", "Cant get media")
            }

            return tweet
        }
        fun fromJsonArray(jsonArr: JSONArray) : MutableList<Tweet>{
            val tweets: MutableList<Tweet> = ArrayList<Tweet>()
            for (i in 0 until (jsonArr.length()-1)){
                tweets.add(Tweet.fromJson(jsonArr.getJSONObject(i)))
            }
            return tweets
        }
    }

}