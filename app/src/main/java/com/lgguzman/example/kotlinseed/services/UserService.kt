package com.lgguzman.example.kotlinseed.services

import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.models.User
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers


class UserService {
    companion object {
        fun isLogging():Boolean{
            return !App.prefs?.token.equals("")
        }

        fun logout():Single<Boolean>{
            App.prefs?.token = ""
            return true.toSingle()
        }



        fun login(): Single<Boolean>{
            App.prefs?.token = "logged"
            return  true.toSingle()
        }

        fun getUsers(): Single<List<User>>{
            val list =  ArrayList<User>()
            for (i  in 0..30){
                list.add(User().apply { name= "nombre${i}" ; email = "email${i}@email.com" })
            }
            return list.toSingle()
        }
    }
}