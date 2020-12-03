package net.lishaoy.library.util

import android.graphics.Color

object ColorUtil {

    fun getCurrentColor(startColor: Int, endColor: Int, fraction: Float) : Int {
        val redS = Color.red(startColor)
        val blueS = Color.blue(startColor)
        val greenS = Color.green(startColor)
        val alphaS = Color.alpha(startColor)

        val redE = Color.red(endColor)
        val blueE = Color.blue(endColor)
        val greenE = Color.green(endColor)
        val alphaE = Color.alpha(endColor)

        val redDiff = redE - redS
        val blueDiff = blueE - blueS
        val greenDiff = greenE - greenS
        val alphaDiff = alphaE - alphaS

        val redCurrent = (redS + fraction * redDiff).toInt()
        val blueCurrent = (blueS + fraction * blueDiff).toInt()
        val greenCurrent = (greenS + fraction * greenDiff).toInt()
        val alphaCurrent = (alphaS + fraction * alphaDiff).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }

}