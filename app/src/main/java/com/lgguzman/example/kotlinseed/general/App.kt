package com.lgguzman.example.kotlinseed.general

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.lgguzman.example.kotlinseed.BuildConfig
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch




/**
 * Created by lgguzman on 27/03/18.
 */
class App: Application() {
    companion object {
        var prefs: Prefs? = null
        var instance: App? = null

        fun log(tag: String, message: String) {
            instance!!.log(tag, message)
        }

        fun getString(id: Int): String {
            return instance!!.getString(id)
        }

        fun getText(id: Int): CharSequence {
            return instance!!.getText(id)
        }

        fun clickUI(f:()->Unit){
            if(!prefs!!.isLoading){
                insecureBlock {
                    try {
                        f()
                    } catch (e: Exception) {
                        log("ISEGUREUI", if (e.message == null) "ERROR" else e.message!!);
                        e.printStackTrace()
                      //  if (!BuildConfig.DEBUG_MODE) {  Crashlytics.logException(e)  }
                        prefs!!.isLoading = false
                    }
                }
            }
        }


        fun insecureUI(f:()->Unit)  {
            launch (UI) {
                try{
                    f()
                }catch (e:Exception){
                    log("ISEGUREUI", if (e.message == null) "ERROR" else e.message!!)
                    e.printStackTrace()
                  //  if (!BuildConfig.DEBUG_MODE){ Crashlytics.logException(e)}
                    prefs!!.isLoading = false
                }
            }
        }

        fun insecureUINow(f:()->Unit)  {
            launch (UI, CoroutineStart.UNDISPATCHED) {
                try{
                    f()
                }catch (e:Exception){
                    log("ISEGUREUI", if (e.message == null) "ERROR" else e.message!!)
                    e.printStackTrace()
                 //   if (!BuildConfig.DEBUG_MODE){ Crashlytics.logException(e)}
                    prefs!!.isLoading = false
                }
            }
        }

        fun insecureThread(f:()->Unit){
            launch (CommonPool){
                try{
                    f()
                }catch (e:Exception){
                    log("ISEGUReThread", if (e.message == null) "ERROR" else e.message!!)
                 //   if (!BuildConfig.DEBUG_MODE){ Crashlytics.logException(e)}
                    e.printStackTrace()
                    prefs!!.isLoading = false
                }
            }
        }

        fun insecureBlock(f:()->Unit){
            try{
                f()
            }catch (e:Exception){
                log("ISEGUREBLOCK", if (e.message == null) "ERROR" else e.message!!)
                e.printStackTrace()
             //   if (!BuildConfig.DEBUG_MODE){Crashlytics.logException(e)}
                prefs!!.isLoading = false
            }
        }

    }

    fun log(tag: String, message: String) {
         if (BuildConfig.DEBUG_MODE) {
             Log.d(tag, message)
         }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        instance = this
        Fresco.initialize(this)
    }




}