package com.lgguzman.example.kotlinseed.utils

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lgguzman.example.kotlinseed.general.App
import com.tbruyelle.rxpermissions2.RxPermissions
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import io.reactivex.subjects.PublishSubject
import pl.aprilapps.easyphotopicker.DefaultCallback
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by lgguzman on 17/03/18.
 */
 class PhotoHelper (val activity: AppCompatActivity, val PHOTOS_KEY:  String,val ok: ()->Unit, val cancel: ()->Unit){


    private var photos = ArrayList<File>()
    private var subject = PublishSubject.create<File>()
    val rxPermissions = RxPermissions(activity)

    init {

        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        App.insecureUI(ok)
                    } else {
                        App.insecureUI(cancel)
                    }
                }
        EasyImage.configuration(activity)
                .setImagesFolderName(PHOTOS_KEY)
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
        subject.unsubscribeOn(AndroidSchedulers.mainThread())

    }

    fun onGetPhoto(): Observable<File>{
        return subject
    }

    fun onCreateActivity(savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            photos = savedInstanceState.getSerializable(PHOTOS_KEY) as ArrayList<File>
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e!!.printStackTrace()
                subject.onError(e)
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                subject.onNext(imageFiles[0])
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity)
                    photoFile?.delete()
                }

            }
        })
    }

    fun onSaveInstance( outState: Bundle?){
        if (outState != null){
            outState.putSerializable(PHOTOS_KEY, photos);
        }

    }


    fun onDestroy(){
        EasyImage.clearConfiguration(activity)
        subject.unsubscribeOn(AndroidSchedulers.mainThread())
    }

    fun openCamera(){
        EasyImage.openCamera(activity, 0);
    }

    fun openGallery(){
        EasyImage.openGallery(activity, 0);
    }


}