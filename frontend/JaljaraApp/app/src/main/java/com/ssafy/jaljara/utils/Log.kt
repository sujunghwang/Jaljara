package com.ssafy.jaljara.utils

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

class Log {
    companion object {
        lateinit var view: View

        fun v(tag: String, msg: String, tr: Throwable? = null): Int {
            return android.util.Log.v(tag, msg, tr)
        }
        fun d(tag: String?, msg: String, tr: Throwable? = null): Int {
            return android.util.Log.i(tag, msg, tr)
        }
        fun i(tag: String, msg: String, tr: Throwable? = null): Int {
            showSnackbar(if (tr != null) "$msg\n$tr" else msg)
            return android.util.Log.i(tag, msg, tr)
        }
        fun w(tag: String?, msg: String, tr: Throwable? = null): Int {
            showSnackbar(if (tr != null) "$msg\n$tr" else msg)
            return android.util.Log.i(tag, msg, tr)
        }
        fun e(tag: String?, msg: String, tr: Throwable? = null): Int {
            showSnackbar(if (tr != null) "$msg\n$tr" else msg)
            return android.util.Log.i(tag, msg, tr)
        }

        private fun showSnackbar(any: Any) {
            val snackbar = Snackbar.make(view, any.toString(), BaseTransientBottomBar.LENGTH_LONG)
            val layout = snackbar.view as Snackbar.SnackbarLayout
            layout.minimumHeight = 100
            val textView = (layout.children.first() as SnackbarContentLayout).children.filter { it is TextView }.first() as TextView
            textView.maxLines = 50
            snackbar.show()
        }
    }
}