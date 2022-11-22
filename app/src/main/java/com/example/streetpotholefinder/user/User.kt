package com.example.streetpotholefinder.user

import android.net.Uri

class User private constructor() {

    // 파라메터를 받는 싱글톤 클래스를 만들려면 companion object를 이용한다.
    companion object {
        private var instance: User? = null

        fun getInstance(): User {
            return instance ?: synchronized(this) {
                instance ?: User().also {
                    instance = it
                }
            }
        }
    }

    public var userName : String? = null
    public var userEmail : String? = null
    public var userPhotoUrl : Uri? = null

    override fun toString(): String {
        return "User(userName=$userName, userEmail=$userEmail, userPhotoUrl=$userPhotoUrl)"
    }
}