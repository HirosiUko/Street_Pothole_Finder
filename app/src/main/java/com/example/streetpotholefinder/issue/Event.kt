package com.example.streetpotholefinder.issue

import com.example.streetpotholefinder.accident.Accident

class Event private constructor() {

    // 파라메터를 받는 싱글톤 클래스를 만들려면 companion object를 이용한다.
    companion object {
        private var instance: Event? = null

        fun getInstance(): Event {
            return instance ?: synchronized(this) {
                instance ?: Event().also {
                    instance = it
                }
            }
        }
    }

    public var accident : Accident = Accident()
}