package com.anou.trowitter.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtils {
    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    /* Get date String with UTC format and return Date with millisecond format */
    fun parseDate(date: String?): Date {
        var pattern = DATE_FORMAT
        if (!date.isNullOrBlank()) {
            pattern = if (date!!.contains(".")) "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" else "yyyy-MM-dd'T'HH:mm:ss'Z'"
        }
        var newDate = Date()
        val sdf = SimpleDateFormat(pattern, Locale.US)
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
        if (date != null) {
            try {
                newDate = sdf.parse(date)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Helper parseDate$e")
            }

        }
        return newDate
    }

    fun getTimeAgo(createdAt: String): String {
        // Convert timestamp to useful string description based on age
        val now = Date().time
        val updated = parseDate(createdAt).getTime()
        val ago =
            DateUtils.getRelativeTimeSpanString(updated, now, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_SHOW_YEAR)

        return ago.toString()
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val currentDateAndTime = sdf.format(Date())
        return currentDateAndTime
    }
}