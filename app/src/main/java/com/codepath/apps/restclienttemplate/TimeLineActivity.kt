package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException

class TimeLineActivity : AppCompatActivity() {
    var tweets : MutableList<Tweet> = mutableListOf()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var swipeContainer : SwipeRefreshLayout
    lateinit var client : TwixClient
    private lateinit var adapter : TweetAdapter
    var currentContent : String = ""
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_compose ->{
                //do something here
                val fm: FragmentManager = supportFragmentManager
                val composeFragment = ComposeFragment.newInstance(currentContent)
                composeFragment.show(fm, "compose")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        client = TwixClient(this)

        setSupportActionBar(findViewById(R.id.attoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val rvTweets : RecyclerView = findViewById(R.id.rvTweets)

        adapter = TweetAdapter(this, tweets)
        rvTweets.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager = layoutManager
        val fabCompose : FloatingActionButton = findViewById(R.id.fabCompose)
        fabCompose.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            val composeFragment = ComposeFragment.newInstance(currentContent)
            composeFragment.show(fm, "compose")
        }
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
            android.R.color.holo_red_light)

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
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }catch(e: JSONException) {
                    Log.d("Main Activity","hit json exception")

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
                    adapter.notifyDataSetChanged()
                }catch(e: JSONException) {
                    Log.d("Main Activity", json.toString())
                    Log.d("Main Activity","hit json exception")

                }
            }
        },tweets[tweets.size-1].id as Long)
    }
}