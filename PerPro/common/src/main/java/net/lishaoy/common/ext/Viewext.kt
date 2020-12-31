package net.lishaoy.common.ext

import android.widget.Toast
import net.lishaoy.library.util.AppGlobals

fun <T> T.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(AppGlobals.get(), message, duration).show()