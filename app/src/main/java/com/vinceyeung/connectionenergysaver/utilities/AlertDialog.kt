package com.vinceyeung.connectionenergysaver.utilities

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.vinceyeung.connectionenergysaver.R

fun showAlertDialog(context: Context, titleRes: Int, messageRes: Int): AlertDialog {
    return AlertDialog.Builder(context)
        .setTitle(titleRes)
        .setMessage(messageRes)
        .setPositiveButton(R.string.common_ok, null)
        .setCancelable(false)
        .create()
}