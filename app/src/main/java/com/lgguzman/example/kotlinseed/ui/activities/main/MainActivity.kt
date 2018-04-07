package com.lgguzman.example.kotlinseed.ui.activities.main


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lgguzman.example.kotlinseed.R
import android.widget.Toast
import com.lgguzman.example.kotlinseed.BuildConfig
import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.models.User
import com.lgguzman.example.kotlinseed.services.UserService
import com.lgguzman.example.kotlinseed.ui.activities.onboarding.SplashActivity
import com.lgguzman.example.kotlinseed.ui.fragments.HomeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by lgguzman on 7/11/17.
 */
open class MainActivity : DrawerActivity() {


    var message = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationDrawer(
                Pair(R.id.home, { App.clickUI {  if (!(supportFragmentManager.findFragmentById(R.id.fragment_container) is HomeFragment)) replaceFragmentContainer((HomeFragment())) }}),
                Pair(R.id.profile, { App.clickUI {    Intent(this@MainActivity, ProfileActivity::class.java).let { startActivity(it) }}}),

                Pair(R.id.logout, {
                    UserService.logout()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                Intent(this@MainActivity, SplashActivity::class.java).let { startActivity(it);finish() }
                    },{

                    })
                App.log("LOGOUT", "LOGOUT")

                })
        )
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            replaceFragmentContainer(HomeFragment() )
        }

        search_toolbar.setText("")
        version.setText("V${BuildConfig.VERSION_NAME}")
        selectItemMenu(1)
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            val cfragment =  supportFragmentManager.findFragmentById(R.id.fragment_container)
            when(requestCode){
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    fun getFragment(): Fragment?{
        return supportFragmentManager.findFragmentById(R.id.fragment_container)
    }
}