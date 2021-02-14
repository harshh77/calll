package com.example.calll.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AndroidPermissions {

    private var mActivity: AppCompatActivity

    constructor(mActivity: AppCompatActivity) {
        this.mActivity = mActivity
    }

    fun checkPermissionForMicrophone(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.RECORD_AUDIO
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }



}