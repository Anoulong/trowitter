package com.anou.trowitter.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.anou.trowitter.lifecycle.CoroutineLifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi


abstract class BaseFragment : Fragment() {
    protected val fragmentLifecycle = CoroutineLifecycleObserver()
    protected val fragmentScope: CoroutineScope = CoroutineScope(Dispatchers.Main + fragmentLifecycle.job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add observer so any jobs are automatically cancelled
        lifecycle.addObserver(fragmentLifecycle)
        Log.d(BaseFragment::class.java.simpleName, "onCreate")
    }

    // Allow to close keyboard when touch outside
    fun setupCloseKeyboardOnTouchOutside(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                view.let {
                     context?.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager
                         it.hideSoftInputFromWindow(view.windowToken, 0)
                     }
                }
                false
            }
        }

        // If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupCloseKeyboardOnTouchOutside(innerView)
            }
        }
    }
}