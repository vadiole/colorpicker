package vadiole.colorpicker

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

fun screenDimensions(context: Context): DisplayMetrics {
    val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(metrics)
    return metrics
}

internal val Context.orientation
    get() = resources.configuration.orientation

internal infix fun Int.percentOf(n: Int): Int = (n * (this / 100.0)).toInt()

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

internal inline val View.absX get() = IntArray(2).apply(::getLocationOnScreen)[0]

internal inline val View.absY get() = IntArray(2).apply(::getLocationOnScreen)[1]


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

internal inline val @receiver:ColorInt Int.hue get() = hsvComponent(this, 0)
internal inline val @receiver:ColorInt Int.saturation get() = hsvComponent(this, 1, 100)
internal inline val @receiver:ColorInt Int.value get() = hsvComponent(this, 2, 100)
