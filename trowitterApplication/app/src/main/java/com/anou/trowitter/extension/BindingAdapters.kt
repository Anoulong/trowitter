package com.anou.trowitter.extension

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.anou.trowitter.R
import com.anou.trowitter.utils.DateTimeUtils
import com.bumptech.glide.Glide

object BindingAdapters {

    const val TAG = "BindingAdapters"

    @JvmStatic
    @BindingAdapter("visibility")
    fun visibility(view: View, visible: Boolean?) {
        visible?.let {
            view.visibility = if (it) VISIBLE else GONE
        }
    }


    @JvmStatic
    @BindingAdapter("selectedIf")
    fun selectedIf(view: View, selected: Boolean?) {
        selected?.let {
            view.isSelected = it
        }
    }

    @JvmStatic
    @BindingAdapter("goneIf")
    fun goneIf(view: View, value: Boolean?) {
        value?.let {
            view.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("invisibleIf")
    fun invisibleIf(view: View, value: Boolean?) {
        value?.let {
            view.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("enableIf")
    fun enableIf(view: View, value: Boolean?) {
        value?.let {
            view.isEnabled = it
        }
    }

    @JvmStatic
    @BindingAdapter("checkedIf")
    fun checkedIf(view: CompoundButton, value: Boolean?) {
        checked(view, value)
    }

    @JvmStatic
    @BindingAdapter("checked")
    fun checked(view: CompoundButton, value: Boolean?) {
        value?.let {
            view.isChecked = it
        }
    }


    @JvmStatic
    @BindingAdapter("formatTimeAgo")
    fun formatTimeAgo(textView: TextView, timestamp: Long) {
        textView.text = DateUtils.getRelativeTimeSpanString(textView.context, timestamp)
    }

    @JvmStatic
    @BindingAdapter("html")
    fun html(textView: TextView, html: String?) {
        html?.let {
            textView.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS)
        }
    }

    @JvmStatic
    @BindingAdapter("formatTimeAgo")
    fun formatTimeAgo(textView: TextView, dateString: String?) {
        dateString?.let {
            textView.text = DateTimeUtils.getTimeAgo(it)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["highlighted", "textColor"], requireAll = true)
    fun highlighted(textView: TextView, highlighted: Boolean = false, @ColorInt colorId: Int = Color.BLACK) {
        val context = textView.context
        val selectedColorId = if (highlighted) {
            ContextCompat.getColor(context, R.color.white)
        } else {
            colorId
        }
        textView.setTextColor(selectedColorId)
        textView.compoundDrawables.forEach {
            it?.let {
                it.colorFilter = PorterDuffColorFilter(selectedColorId, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("srcNetwork")
    fun srcNetwork(imageView: ImageView, url: String? = null) {
        url?.let { url ->
            if (url.isNotEmpty()) {

                Glide.with(imageView.context).load(url).into(imageView)
            } else {
                Log.w(TAG, "srcNetwork: url is EMPTY")
            }
        } ?: run {
            Log.w(TAG, "srcNetwork: url is NULL")
        }
    }

    @JvmStatic
    @BindingAdapter("htmlText")
    fun htmlText(webView: WebView, text: String?) {
        text?.let {
            webView.loadDataWithBaseURL(null, text, "text/html", "utf-8", null)
        }
    }
}