package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject
import org.parceler.Parcel

@Parcel
public class User constructor(jsonObj: JSONObject)  {
    constructor():this(JSONObject())

    public var tag: String = ""
    public var name : String = ""
    public var imageAvatarUrl : String =""
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