package com.awda.app.presentation.common.components

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.awda.app.R

/**
 * Created by Abdelrahman Rizq
 */

class LockerDialog(private val context: Context) {
    private var dialog: AlertDialog? = null
    private var currentLocker: Type? = null

    enum class Type {
        SCREEN_TIME,
        BLOCKED_APP
    }

    fun show(
        locker: Type,
        onActionButtonClick: () -> Unit = {}
    ) {
        if (dialog == null || dialog?.isShowing == false) {
            createDialog(onActionButtonClick, locker)
        }
    }

    fun dismiss(locker: Type) {
        if (locker == currentLocker) {
            dialog?.dismiss()
        }
    }

    private fun createDialog(onActionButtonClick: () -> Unit, locker: Type) {
        currentLocker = locker
        val resource = when (locker) {
            Type.SCREEN_TIME -> {
                R.layout.locker_dialog
            }

            Type.BLOCKED_APP -> {
                R.layout.blocked_app_dialog
            }
        }
        val view = LayoutInflater.from(context).inflate(resource, null)
        val builder = AlertDialog.Builder(context, R.style.BlurTheme)
        builder.setView(view)
        dialog = builder.create()
        val dialogWindow = dialog!!.window
        dialogWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setDialogAttributes(dialogWindow)
        if (resource == R.layout.locker_dialog) {
            setDialogButtonClickAction(view, onActionButtonClick)
        }
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun setDialogAttributes(dialogWindow: Window) {
        val dialogWindowAttributes = dialogWindow.attributes
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialogWindowAttributes)
        layoutParams.width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            getScreenWidthInDp() - 48,
            context.resources.displayMetrics
        ).toInt()
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow.attributes = layoutParams
        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
    }

    private fun setDialogButtonClickAction(view: View, onActionButtonClick: () -> Unit) {
        val dialogButton = view.findViewById<Button>(R.id.locker_button)
        dialogButton.setOnClickListener {
            onActionButtonClick()
            dialog!!.dismiss()
        }
    }

    private fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    private fun getScreenWidthInDp(): Float {
        val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        return pxToDp(screenWidthPx)
    }
}
