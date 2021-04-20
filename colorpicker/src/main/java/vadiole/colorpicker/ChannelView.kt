package vadiole.colorpicker


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style
import android.graphics.Shader.TileMode.REPEAT
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import vadiole.colorpicker.ColorModel.GradientBackground.*


@SuppressLint("ViewConstructor")
internal class ChannelView(
    context: Context,
    val channel: ColorModel.Channel,
    @ColorInt color: Int,
    @ColorInt private val textColor: Int,
    @ColorInt private val thumbColor: Int,
    @ColorInt private val rippleColor: Int,
) : RelativeLayout(context) {

    var listener: (() -> Unit)? = null

    private var lastHue = floatArrayOf(0f, 0f, 0f).also { Color.colorToHSV(color, it) }[0]
    private var lastSaturation = floatArrayOf(0f, 0f, 0f).also { Color.colorToHSV(color, it) }[1]
    private var lastValue = floatArrayOf(0f, 0f, 0f).also { Color.colorToHSV(color, it) }[2]
    private var lastRed = color.red
    private var lastGreen = color.green
    private var lastBlue = color.blue
    private var seekbar: SeekBar? = null

    private val alphaTexture = BitmapFactory.decodeResource(resources, R.drawable.alpha_src)
    private val gradientColors: IntArray
        get() = when (channel.background) {
            NONE -> intArrayOf(Color.WHITE, Color.WHITE)
            HUE -> IntArray(360) {
                val colors = floatArrayOf(it.toFloat(), 1f, 1f)
                Color.HSVToColor(colors)
            }
            SATURATION -> {
                val colorStart = hsv(lastHue, 0f, lastValue)
                val colorEnd = hsv(lastHue, 100f, lastValue)
                intArrayOf(colorStart, colorEnd)
            }
            VALUE -> {
                val colorStart = hsv(lastHue, lastSaturation, 0f)
                val colorEnd = hsv(lastHue, lastSaturation, 1f)
                intArrayOf(colorStart, colorEnd)
            }
            RED -> {
                val colorStart = Color.rgb(0, lastGreen, lastBlue)
                val colorEnd = Color.rgb(255, lastGreen, lastBlue)
                intArrayOf(colorStart, colorEnd)
            }
            GREEN -> {
                val colorStart = Color.rgb(lastRed, 0, lastBlue)
                val colorEnd = Color.rgb(lastRed, 255, lastBlue)
                intArrayOf(colorStart, colorEnd)
            }
            BLUE -> {
                val colorStart = Color.rgb(lastRed, lastGreen, 0)
                val colorEnd = Color.rgb(lastRed, lastGreen, 255)
                intArrayOf(colorStart, colorEnd)
            }
            ColorModel.GradientBackground.ALPHA -> {
                val colorStart = (Color.argb(0, lastRed, lastGreen, lastBlue))
                val colorEnd = (Color.argb(255, lastRed, lastGreen, lastBlue))
                intArrayOf(colorStart, colorEnd)
            }
        }

    private var gradientDrawable = GradientDrawable().apply {
        colors = gradientColors
        orientation = LEFT_RIGHT
        gradientType = GradientDrawable.LINEAR_GRADIENT
        cornerRadius = 100f
    }


    private var alphaGradient = object : LayerDrawable(arrayOf(gradientDrawable)) {
        private var bounds = RectF(getDrawable(0).bounds)
        private val textureShader = BitmapShader(alphaTexture, REPEAT, REPEAT)
        private val texturePaint = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            shader = textureShader
        }

        override fun onBoundsChange(newBounds: Rect?) {
            super.onBoundsChange(newBounds)
            bounds = RectF(newBounds)
        }

        override fun draw(canvas: Canvas) {
            val gradient = getDrawable(0)

            //  draw rounded corners rect with shader
            canvas.drawRoundRect(
                bounds,
                100f,
                100f,
                texturePaint
            )

            //  draw gradient
            gradient.draw(canvas)
        }
    }


    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        channel.progress = channel.extractor.invoke(color)

        if (channel.progress < channel.min || channel.progress > channel.max) {
            throw IllegalArgumentException(
                "Initial progress for channel: ${channel.javaClass.simpleName}"
                        + " must be between ${channel.min} and ${channel.max}."
            )
        }

        val rootView = inflate(context, R.layout.color_picker_channel_row, this)
        bindViews(rootView)
    }

    private fun bindViews(root: View) {
        //  channel name
        val label = root.findViewById(R.id.label) as TextView
        label.text = channel.name
        label.setTextColor(textColor)

        //  channel progress
        val progressView = root.findViewById(R.id.progress_text) as TextView
        progressView.text = channel.progress.toString()
        progressView.setTextColor(textColor)

        //  channel seekbar
        seekbar = root.findViewById<SeekBar>(R.id.seekbar).apply {
            max = channel.max
            progress = channel.progress

            background = RippleDrawable(
                ColorStateList.valueOf(rippleColor),
                null,
                ShapeDrawable(RectShape()).apply {
                    paint.color = Color.BLACK
                }
            )
            thumbTintList = ColorStateList.valueOf(thumbColor)

            when (channel.background) {
                NONE -> Unit
                ColorModel.GradientBackground.ALPHA -> {
                    progressDrawable = alphaGradient
                }
                else -> progressDrawable = gradientDrawable
            }

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekbar: SeekBar?) {}

                override fun onStopTrackingTouch(seekbar: SeekBar?) {}

                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.i("Seekbar", "onProgressChanged: $progress ")
                    channel.progress = progress

                    progressView.text = progress.toString()
                    listener?.invoke()
                }
            })
        }
    }

    fun setTintHSV(hue: Int, saturation: Int, value: Int) {
        if (channel.background == SATURATION || channel.background == VALUE) {

            if (saturation > 0) lastSaturation = saturation / 100f
            lastHue = hue.toFloat()
            lastValue = value / 100f

            gradientDrawable.colors = gradientColors
        }
    }

    fun setTintRGB(red: Int, green: Int, blue: Int) = when (channel.background) {
        RED, GREEN, BLUE, ColorModel.GradientBackground.ALPHA -> {

            lastRed = red
            lastGreen = green
            lastBlue = blue

            gradientDrawable.colors = gradientColors
        }
        else -> Unit
    }

    fun registerListener(listener: () -> Unit): Unit {
        this.listener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.listener = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }


}
