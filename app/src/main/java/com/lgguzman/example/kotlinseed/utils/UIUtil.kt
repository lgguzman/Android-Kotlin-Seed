package com.lgguzman.example.kotlinseed.utils


import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lgguzman on 10/11/17.
 */
class UIUtil() {


    companion object {
        val FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER"
        fun openPicker(activity: AppCompatActivity, listener: (CalendarDatePickerDialogFragment?, Int, Int, Int) -> Unit) {

        }

        var flagOffset = 0x1F1E6
        var asciiOffset = 0x41

        fun ISOcountryToEmoji(country: String): String{
            var firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset
            var secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset

            return  String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
        }


        fun hideKeyBoard(activity: Activity, view: View) {
            try {
                val imm = activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (ex: Exception) {
            }

        }


        fun convertToEST(date: Date): Date {
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mm:ss z")
            formatter.setTimeZone(TimeZone.getTimeZone("EST"))
            return formatter.parse(formatter.format(date))
        }

        fun fromESTToDate(date: String): Date {
            val formatter = SimpleDateFormat("yyyyMMdd")
            formatter.setTimeZone(TimeZone.getTimeZone("EST"))
            val formatter2 = SimpleDateFormat("yyyyMMdd")
            return   formatter.parse(formatter2.format(formatter.parse(date)))
        }

    }
}