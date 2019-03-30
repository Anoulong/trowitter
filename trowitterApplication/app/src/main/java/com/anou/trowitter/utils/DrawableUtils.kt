package com.anou.trowitter.utils

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


object DrawableUtils {
    fun getTintedDrawable(context: Context, @DrawableRes drawableResId: Int, @ColorRes colorResId: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, drawableResId)?.mutate()
        drawable?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(context, colorResId),
            PorterDuff.Mode.SRC_ATOP
        )
        return drawable
    }

    fun getTintedDrawable(context: Context, d: Drawable, @ColorRes colorResId: Int): Drawable {
        val drawable = d.mutate()
        drawable.colorFilter =
            PorterDuffColorFilter(ContextCompat.getColor(context, colorResId), PorterDuff.Mode.SRC_ATOP)
        return drawable
    }

}