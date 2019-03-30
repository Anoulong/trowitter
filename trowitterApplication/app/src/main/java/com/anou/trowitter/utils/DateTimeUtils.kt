package com.anou.trowitter.utils

import android.text.format.DateUtils.FORMAT_SHOW_YEAR
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils;


object DateTimeUtils {
    /* Get date String with UTC format and return Date with millisecond format */
    fun parseDate(date: String?): Date {
        var pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        if (!date.isNullOrBlank()) {
            pattern = if (date!!.contains(".")) "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" else "yyyy-MM-dd'T'HH:mm:ss'Z'"
        }
        var newDate = Date()
        val sdf = SimpleDateFormat(pattern, Locale.US)
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
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
    fun getTimeAgo(updatedAt: String): String {
        // Convert timestamp to useful string description based on age
        val now = System.currentTimeMillis()
        val updated = parseDate(updatedAt).getTime().toLong()
        val ago =
            DateUtils.getRelativeTimeSpanString(updated, now, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_SHOW_YEAR)

        return ago.toString()
    }
}