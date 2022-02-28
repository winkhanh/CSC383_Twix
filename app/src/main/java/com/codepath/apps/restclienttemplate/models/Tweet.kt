package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Tweet {
    var content: String = ""
    var id: Number = 0
    var time : String = ""
        get(){
            var time = ""
            val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
            val format = SimpleDateFormat(twitterFormat, Locale.US)
            format.isLenient=true
            try{
                val diff = (System.currentTimeMillis() - format.parse(field).time) / 1000
                if (diff < 5)
                    time = "Just now"
                else if (diff < 60)
                    time = String.format(Locale.ENGLISH, "%ds",diff)
                else if (diff < 60 * 60)
                    time = String.format(Locale.ENGLISH, "%dm", diff / 60)
                else if (diff < 60 * 60 * 24)
                    time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60))
                else if (diff < 60 * 60 * 24 * 30)
                    time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24))
                else {
                    val now = Calendar.getInstance();
                    val then = Calendar.getInstance();

                    then.time = format.parse(field)
                    if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                        time = (then.get(Calendar.DAY_OF_MONTH)).toString() + " " + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                    } else {
                        time = (then.get(Calendar.DAY_OF_MONTH)).toString() + " " + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + (then.get(Calendar.YEAR) - 2000).toString();
                    }
                }
            }catch(e: ParseException){
                e.printStackTrace()
            }
            return time
        }
    var user : User = User()
    companion object{
        fun fromJson(jsonObj: JSONObject) : Tweet{
            var tweet : Tweet = Tweet()
            tweet.content = jsonObj.getString("text")
            tweet.time = jsonObj.getString("created_at")
            tweet.user = User.fromJson(jsonObj.getJSONObject("user"))
            tweet.id = jsonObj.getLong("id")
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