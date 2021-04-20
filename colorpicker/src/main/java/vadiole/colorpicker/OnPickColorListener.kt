package vadiole.colorpicker

import androidx.annotation.ColorInt


interface OnPickColorListener {
    fun onColorSelected(@ColorInt color: Int)
}

