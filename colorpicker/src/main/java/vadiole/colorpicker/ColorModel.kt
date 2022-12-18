package vadiole.colorpicker

import android.graphics.Color
import vadiole.colorpicker.ColorModel.GradientBackground.ALPHA
import vadiole.colorpicker.ColorModel.GradientBackground.BLUE
import vadiole.colorpicker.ColorModel.GradientBackground.GREEN
import vadiole.colorpicker.ColorModel.GradientBackground.HUE
import vadiole.colorpicker.ColorModel.GradientBackground.NONE
import vadiole.colorpicker.ColorModel.GradientBackground.RED
import vadiole.colorpicker.ColorModel.GradientBackground.SATURATION
import vadiole.colorpicker.ColorModel.GradientBackground.VALUE

enum class ColorModel {

    ARGB {
        override val channels: List<Channel> = listOf(
            Channel("A", 0, 255, Color::alpha, ALPHA),
            Channel("R", 0, 255, Color::red, RED),
            Channel("G", 0, 255, Color::green, GREEN),
            Channel("B", 0, 255, Color::blue, BLUE)
        )

        override fun evaluateColor(channels: List<Channel>): Int = Color.argb(
            channels[0].progress, channels[1].progress, channels[2].progress, channels[3].progress
        )
    },

    RGB {
        override val channels: List<Channel> = ARGB.channels.drop(1)

        override fun evaluateColor(channels: List<Channel>): Int = Color.rgb(
            channels[0].progress, channels[1].progress, channels[2].progress
        )
    },

    AHSV {
        override val channels: List<Channel> = listOf(
            Channel("A", 0, 255, Color::alpha, ALPHA),
            Channel("H", 0, 360, ::hue, HUE),
            Channel("S", 0, 100, ::saturation, SATURATION),
            Channel("V", 0, 100, ::value, VALUE)
        )

        override fun evaluateColor(channels: List<Channel>): Int = Color.HSVToColor(
            channels[0].progress,
            floatArrayOf(
                (channels[1].progress).toFloat(),
                (channels[2].progress / 100.0).toFloat(),
                (channels[3].progress / 100.0).toFloat()
            )
        )
    },

    HSV {
        override val channels: List<Channel> = listOf(
            Channel("H", 0, 360, ::hue, HUE),
            Channel("S", 0, 100, ::saturation, SATURATION),
            Channel("V", 0, 100, ::value, VALUE)
        )

        override fun evaluateColor(channels: List<Channel>): Int = Color.HSVToColor(
            floatArrayOf(
                (channels[0].progress).toFloat(),
                (channels[1].progress / 100.0).toFloat(),
                (channels[2].progress / 100.0).toFloat()
            )
        )
    };

    internal abstract val channels: List<Channel>

    internal abstract fun evaluateColor(channels: List<Channel>): Int

    internal data class Channel(
        val name: String,
        val min: Int, val max: Int,
        val extractor: (color: Int) -> Int,
        val background: GradientBackground = NONE,
        var progress: Int = 0
    )

    companion object {
        @JvmStatic
        fun fromName(name: String?) = values().find { it.name == name } ?: RGB
    }

    enum class GradientBackground {
        NONE,
        HUE,
        SATURATION,
        VALUE,
        RED,
        GREEN,
        BLUE,
        ALPHA
    }
}
