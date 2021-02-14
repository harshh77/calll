package com.example.calll.helper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.calll.activity.MainActivity
import java.util.*

class methodClass {

    private lateinit var sr: SpeechRecognizer
    private lateinit var mContext: AppCompatActivity

    constructor(context: AppCompatActivity) {
        mContext = context
    }

    fun myMethod() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        intent.putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                mContext.packageName
        )

        // Add custom listeners.
        val listener = CustomRecognitionListener()
        sr = SpeechRecognizer.createSpeechRecognizer(mContext)
        sr.setRecognitionListener(listener)
        sr.startListening(intent)
    }

    inner class CustomRecognitionListener: RecognitionListener {
        private val s = "RecognitionListener"

        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(s, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(s, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(s, "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(s, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(s, "onEndOfSpeech")
        }

        override fun onError(error: Int) {
            Log.e(s, "error $error")
        }

        override fun onResults(results: Bundle?) {
            val data = results!!
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            val dataString = data!![0].toString().toLowerCase(Locale.ROOT)

            makeCall(dataString)
        }

        override fun onPartialResults(p0: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            TODO("Not yet implemented")
        }
    }

    fun makeCall(name: String) {
        try {
            Log.d("myCHECK NAME", name)
            val cursor: Cursor? = mContext.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE),
                    "DISPLAY_NAME = '" + name + "'", null, null)
            if(cursor == null) {
                return
            }
            cursor!!.moveToFirst()
            val number: String = cursor.getString(0)

            if (number.trim { it <= ' ' }.length > 0) {
                if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   // ActivityCompat.requestPermissions(mContext, arrayOf(Manifest.permission.CALL_PHONE), 100)
                } else {
                    val dial = "tel:$number"
                    mContext.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                }
            } else {
                Toast.makeText(mContext, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            editText2.setText("NA")
        }
    }
}