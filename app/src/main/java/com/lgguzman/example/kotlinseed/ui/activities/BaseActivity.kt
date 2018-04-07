package com.lgguzman.example.kotlinseed.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.lgguzman.example.kotlinseed.general.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by lgguzman on 27/03/18.
 */
open class BaseActivity : AppCompatActivity() {

    var progressBar: ProgressBar? = null
    var hasBackButton = false
    var isAddToBackStackAllowed = false
    var TAG_FRAGMENT = "TAG_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.prefs!!.isLoading = false
    }

    fun showToast(id: Int) {
        Toast.makeText(this@BaseActivity, resources.getString(id), Toast.LENGTH_LONG).show()
    }

    fun showToast(message: String?) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_LONG).show()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment, TAG_FRAGMENT) }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        if (isAddToBackStackAllowed) {
            fragmentTransaction.addToBackStack("tag")
        }
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { replace(frameId, fragment, TAG_FRAGMENT) }
    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentByTag(TAG_FRAGMENT)
    }

    fun setHomeIndicator() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            hasBackButton = true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (hasBackButton) {
            if (item.itemId == android.R.id.home) {
                onBackPressed() // close this activity and return to preview activity (if there is any)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {

        App.prefs!!.isLoading = false
        super.onResume()
    }

}