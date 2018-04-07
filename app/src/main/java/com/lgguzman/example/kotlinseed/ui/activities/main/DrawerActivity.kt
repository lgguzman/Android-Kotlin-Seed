package com.lgguzman.example.kotlinseed.ui.activities.main

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.ui.activities.BaseActivity
import com.lgguzman.example.kotlinseed.utils.UIUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

/**
 * Created by lgguzman on 7/11/17.
 */
open class DrawerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }


    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun addFragmentContainer(fragment: Fragment) {
        addFragment(fragment, R.id.fragment_container)
    }

    fun replaceFragmentContainer(fragment: Fragment) {
        replaceFragment(fragment, R.id.fragment_container)
    }


    fun hideSearchView() {
        search_layout.visibility = View.GONE
    }

    fun showSearchView(hint: String, filter: (String) -> Unit, resume: ()->Unit) {
        search_layout.visibility = View.VISIBLE
        search_toolbar.setHint(hint)
        search_toolbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
              App.insecureUI {
                  if(editable.isEmpty()){
                      search_close.visibility = View.GONE
                  }else{
                      search_close.visibility = View.VISIBLE
                  }
              }
                App.insecureUINow {   filter(editable.toString())}
            }
        })
        search_close.setOnClickListener({

            search_toolbar.setText("")
        })
        search_toolbar.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                App.log("ACTIONID", "${actionId}")
                if (event==null) {
                    if (actionId==EditorInfo.IME_ACTION_DONE)
                        resume()
                    // Capture soft enters in a singleLine EditText that is the last EditText.
                    else if (actionId==EditorInfo.IME_ACTION_NEXT) resume()
                    // Capture soft enters in other singleLine EditTexts
                    else return false;  // Let system handle all other null KeyEvents
                }
                else if (actionId==EditorInfo.IME_NULL) {
                    // Capture most soft enters in multi-line EditTexts and all hard enters.
                    // They supply a zero actionId and a valid KeyEvent rather than
                    // a non-zero actionId and a null event like the previous cases.
                    if (event.getAction()==KeyEvent.ACTION_DOWN) resume()
                    // We capture the event when key is first pressed.
                    else  return true;   // We consume the event when the key is released.
                }
                else  return false;
                // We let the system handle it when the listener
                // is triggered by something that wasn't an enter.


                // Code from this point on will execute whenever the user
                // presses enter in an attached view, regardless of position,
                // keyboard, or singleLine status.


                return true;   // Consume the event
            } })

    }


    fun initNavigationDrawer(vararg pairs: Pair<Int, () -> Unit>) {
        navigation_view.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            for (par in pairs) {
                if (par.first == id) {
                    par.second(); drawer!!.closeDrawers()
                }
            }
            true
        }
        val header = navigation_view.getHeaderView(0)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(v: View) {
                App.insecureUI { UIUtil.hideKeyBoard(this@DrawerActivity, this@DrawerActivity.search_toolbar) }
                super.onDrawerClosed(v)
            }

            override fun onDrawerOpened(v: View) {
                App.insecureUI { UIUtil.hideKeyBoard(this@DrawerActivity, this@DrawerActivity.search_toolbar) }
                super.onDrawerOpened(v)
            }
        }
        drawer!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    fun setImageHeader(url: String?) {
        if (url != null) {
            val header = navigation_view.getHeaderView(0)
            header.image_header.setImageURI(Uri.parse(url).toString())
        }

    }

    fun selectItemMenu(index: Int){
        App.insecureUI {
            for (i in 0..navigation_view.menu.size() - 1) {
                navigation_view.menu.getItem(i).isChecked = false
            }
            navigation_view.menu.getItem(index).isChecked = true
        }
    }


    fun setTextHeader(name: String) {
        val header = navigation_view.getHeaderView(0)
        header.text_header.setText(name)
    }
}