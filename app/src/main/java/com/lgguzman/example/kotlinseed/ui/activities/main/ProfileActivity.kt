package com.lgguzman.example.kotlinseed.ui.activities.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.databinding.ActivityProfileBinding
import com.lgguzman.example.kotlinseed.models.User
import kotlinx.android.synthetic.main.activity_profile.*
import me.echodev.resizer.Resizer

/**
 * Created by lgguzman on 18/12/17.
 */
class ProfileActivity : BasePhotoActivity() {
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setHomeIndicator()
        photoHelper!!.onGetPhoto()
                .map {
                    val filere=  Resizer(this)
                            .setTargetLength(640)
                            .setQuality(80)
                            .setOutputFormat("JPEG")
                            .setOutputFilename(it.nameWithoutExtension+ 0)
                            .setOutputDirPath(it.path.replace(it.name,""))
                            .setSourceImage(it)
                            .resizedFile
                    it.delete()
                    return@map filere
                }
                .subscribe ({
                   profile.setImageURI(Uri.fromFile(it))
                }, {
                    it.printStackTrace()
                })
        binding.manager = User().apply { name="user1"; email= "user1@suer.com"  }
        profile.setOnClickListener {
            openPhotoIntent()
        }
    }

    private fun openPhotoIntent(){
        val adb = AlertDialog.Builder(this)
        val items = resources.getStringArray(R.array.photo_chooser)
        adb.setSingleChoiceItems(items, 0, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    0 -> { photoHelper!!.openCamera()}
                    else -> { photoHelper!!.openGallery()}
                }
                if (dialog!=null) dialog.dismiss()

            }

        })
        adb.setTitle("Which one?")
        adb.show()
    }




}