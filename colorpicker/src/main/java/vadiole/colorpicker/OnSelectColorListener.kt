package vadiole.colorpicker

import androidx.annotation.ColorInt

interface OnSelectColorListener {
    fun onColorSelected(@ColorInt color: Int)
}
