package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject

class User {
    var tag: String = ""
    var name : String = ""
    var imageAvatarUrl : String =""
    companion object{
        fun  fromJson(jsonObj: JSONObject) : User{
            var user: User = User()
            user.tag = jsonObj.getString("screen_name")
            user.name = jsonObj.getString("name")
            user.imageAvatarUrl = jsonObj.getString("profile_image_url_https")
            return user
        }
    }
}