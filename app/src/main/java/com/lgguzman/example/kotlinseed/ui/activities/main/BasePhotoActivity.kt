package com.lgguzman.example.kotlinseed.ui.activities.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.lgguzman.example.kotlinseed.utils.PhotoHelper
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.ui.activities.BaseActivity

/**
 * Created by lgguzman on 17/03/18.
 */
open class BasePhotoActivity: BaseActivity() {

    var photoHelper: PhotoHelper? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoHelper = PhotoHelper(this, "lgguzman_photos",{},{
            val dialog = AlertDialog.Builder(this)
                    .setMessage(R.string.message_user_photo_permission)
                    .setPositiveButton(App.getString(R.string.ok), {  _,_ ->
                        finish()
                    }
                    ).create()
            dialog.show()
        })
        photoHelper!!.onCreateActivity(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoHelper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        photoHelper!!.onSaveInstance(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        photoHelper!!.onDestroy()
    }
}