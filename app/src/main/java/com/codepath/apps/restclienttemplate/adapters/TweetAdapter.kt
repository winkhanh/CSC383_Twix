package com.codepath.apps.restclienttemplate.adapters

import android.content.Context
import android.content.Intent
import android.content.ReceiverCallNotAllowedException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.DetailActivity
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.models.Tweet
import org.parceler.Parcels

class TweetAdapter(val context: Context,val tweets: List<Tweet>): RecyclerView.Adapter<TweetAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivAvatar : ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName : TextView = itemView.findViewById(R.id.tvName)
        val tvTag : TextView = itemView.findViewById(R.id.tvTag)
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)
        val tvContent : TextView = itemView.findViewById(R.id.tvContent)
        val rlContainer : RelativeLayout = itemView.findViewById(R.id.rlItemContainer)
        fun bind(tweet: Tweet){
            tvContent.text = tweet.content
            tvName.text = tweet.user.name
            tvTag.text = "@"+tweet.user.tag
            tvTime.text = tweet.getTime()
            Glide.with(context).load(tweet.user.imageAvatarUrl).into(ivAvatar)
            rlContainer.setOnClickListener {
                val i : Intent = Intent(context, DetailActivity::class.java).apply{
                    putExtra("tweet", Parcels.wrap(tweet))
                }
                context.startActivity(i)
            }
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