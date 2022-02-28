package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import org.json.JSONArray
import org.json.JSONException

class TimeLineActivity : AppCompatActivity() {
    var tweets : MutableList<Tweet> = mutableListOf<Tweet>()
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var swipeContainer : SwipeRefreshLayout
    private lateinit var client : TwixClient
    lateinit var adapater : TweetAdapter
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        client = TwixClient(this)

        setSupportActionBar(findViewById(R.id.attoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rvTweets : RecyclerView = findViewById(R.id.rvTweets)

        adapater = TweetAdapter(this, tweets)
        rvTweets.adapter = adapater
        val layoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager = layoutManager

        scrollListener = object:EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.d("Main Activity","onLoadMore")
                loadMoreHomTimeLine()
            }
        }
        rvTweets.addOnScrollListener(scrollListener)

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            populateHomTimeLine()
        }
        populateHomTimeLine()
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

    }

    private fun populateHomTimeLine(){

        client.getHomeTimeLine(object:JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: okhttp3.Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d("Main Activity","onFailure")
            }

            override fun onSuccess(statusCode: Int, headers: okhttp3.Headers?, json: JSON?) {
                Log.d("Main Activity", json.toString())

                try {
                    tweets.clear()
                    val result: JSONArray = json?.jsonArray ?: JSONArray()
                    tweets.addAll(Tweet.fromJsonArray(result))
                    adapater.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }catch(e: JSONException) {
                    Log.d("Main Activity","hitttt json exception")
                    adapater.notifyDataSetChanged()


                }
            }
        })
    }

    private fun loadMoreHomTimeLine(){
        client.getHomeTimeLineFrom(object:JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: okhttp3.Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d("Main Activity","onFailure")
            }

            override fun onSuccess(statusCode: Int, headers: okhttp3.Headers?, json: JSON?) {

                try {
                    val result: JSONArray = json?.jsonArray ?: JSONArray()
                    tweets.addAll(Tweet.fromJsonArray(result))
                    adapater.notifyDataSetChanged()
                }catch(e: JSONException) {
                    Log.d("Main Activity", json.toString())
                    Log.d("Main Activity","hit json exception")

                }
            }
        },tweets[tweets.size-1].id as Long)
    }
}