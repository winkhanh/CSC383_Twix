package com.codepath.apps.restclienttemplate.adapters

import android.content.Context
import android.content.ReceiverCallNotAllowedException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetAdapter(val context: Context,val tweets: List<Tweet>): RecyclerView.Adapter<TweetAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivAvatar : ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName : TextView = itemView.findViewById(R.id.tvName)
        val tvTag : TextView = itemView.findViewById(R.id.tvTag)
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)
        val tvContent : TextView = itemView.findViewById(R.id.tvContent)
        fun bind(tweet: Tweet){
            tvContent.text = tweet.content
            tvName.text = tweet.user.name
            tvTag.text = "@"+tweet.user.tag
            tvTime.text = tweet.time
            Glide.with(context).load(tweet.user.imageAvatarUrl).into(ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = tweets[position]
        holder.bind(tweet)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }


}