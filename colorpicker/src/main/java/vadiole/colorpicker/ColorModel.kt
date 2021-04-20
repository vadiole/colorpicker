package vadiole.colorpicker


import android.graphics.Color
import vadiole.colorpicker.ColorModel.GradientBackground.*

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
