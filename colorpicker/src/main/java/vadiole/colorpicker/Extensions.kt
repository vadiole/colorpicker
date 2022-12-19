package vadiole.colorpicker

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

internal fun hue(color: Int): Int = hsvComponent(color, 0)
internal fun saturation(color: Int): Int = hsvComponent(color, 1, 100)
internal fun value(color: Int): Int = hsvComponent(color, 2, 100)

internal fun hsvComponent(color: Int, index: Int, multiplier: Int = 1): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    return (hsv[index] * multiplier).toInt()
}

@ColorInt
internal fun hsv(hue: Float, saturation: Float, value: Float): Int {
    return Color.HSVToColor(floatArrayOf(hue, saturation, value))
}

internal fun View.onClick(intervalMillis: Long = 200, onClick: ((View) -> Unit)) {
    setOnClickListener(DebouncingOnClickListener(intervalMillis, onClick))
}

internal fun View?.removeSelf() {
    this ?: return
    val parent = parent as? ViewGroup ?: return
    parent.removeView(this)
}

@ColorInt
internal fun Context.themeColor(
    @AttrRes themeAttrId: Int,
    @ColorInt default: Int = Color.GRAY
): Int {
    return obtainStyledAttributes(intArrayOf(themeAttrId)).use {
        it.getColor(0, default)
    }
}
