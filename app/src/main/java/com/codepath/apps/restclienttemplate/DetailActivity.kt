package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import org.parceler.Parcels

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.bttoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val ivAvatar : ImageView = findViewById(R.id.ivDetailAvatar)
        val tvName : TextView = findViewById(R.id.tvDetailName)
        val tvTag : TextView = findViewById(R.id.tvDetailTag)
        val tvTime : TextView = findViewById(R.id.tvDetailTime)
        val tvContent : TextView = findViewById(R.id.tvDetailContent)
        val ivMedia : ImageView = findViewById(R.id.ivMedia)
        val tweet : Tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"))

        tvContent.text = tweet.content
        tvName.text = tweet.user.name
        tvTag.text = "@"+tweet.user.tag
        tvTime.text = tweet.time
        Glide.with(this).load(tweet.user.imageAvatarUrl).into(ivAvatar)
        if (tweet.photos.size > 0)
            Glide.with(this).load(tweet.photos[0]).into(ivMedia)
    }
}