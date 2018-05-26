package com.lgguzman.example.kotlinseed.utils

import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lgguzman.example.kotlinseed.general.App
import com.tbruyelle.rxpermissions2.RxPermissions
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import io.reactivex.subjects.PublishSubject
import pl.aprilapps.easyphotopicker.DefaultCallback
import android.content.Intent
import android.net.Uri
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by lgguzman on 17/03/18.
 */
 class PhotoHelper (val activity: AppCompatActivity, val PHOTOS_KEY:  String,val ok: ()->Unit, val cancel: ()->Unit){


    private var photos = ArrayList<File>()
    private var subject = PublishSubject.create<File>()
    val rxPermissions = RxPermissions(activity)
    private var crop: Boolean = false
    private var current_id= 0
    private var aspectRatio = Pair(1, 1)
    private var scaleType = CropImageView.ScaleType.CENTER_CROP

    init {

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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                subject.onNext( File(result.uri.getPath()))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                subject.onError(result.error)
            }
        }else{
            EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
                override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                    //Some error handling
                    e!!.printStackTrace()
                    subject.onError(e)
                }

                override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                    if(crop){
                        CropImage.activity(Uri.fromFile(imageFiles[0]))
                                .setAspectRatio(aspectRatio.first,aspectRatio.second)
                                .start(activity);
                    }else{
                        subject.onNext(imageFiles[0])
                    }


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


    }

    fun onSaveInstance( outState: Bundle?){
        if (outState != null){
            outState.putSerializable(PHOTOS_KEY, photos);
        }

    }

    fun changeAspectRatio(pair:Pair<Int, Int> = Pair(4,3)){
        aspectRatio = pair;
    }

    fun isCrop(): Boolean{
        return crop;
    }

    fun getLastId(): Int{
        return current_id;
    }

    fun onDestroy(){
        EasyImage.clearConfiguration(activity)
        subject.unsubscribeOn(AndroidSchedulers.mainThread())
    }

    fun openCamera(crop: Boolean =false, current_id: Int = 0 ){
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        this.crop = crop;
                        this.current_id = current_id;
                        EasyImage.openCamera(activity, 0);
                    } else {
                        App.insecureUI(cancel)
                    }
                }

    }

    fun openGallery(crop: Boolean =false, current_id: Int = 0 ){
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        this.crop = crop;
                        this.current_id = current_id;
                        EasyImage.openGallery(activity, 0);
                    } else {
                        App.insecureUI(cancel)
                    }
                }
    }


}