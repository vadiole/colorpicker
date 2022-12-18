package vadiole.colorpicker

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import kotlin.properties.Delegates

/**
 * A subclass of DialogFragment with color picker dialog.
 */
class ColorPickerDialog internal constructor() : DialogFragment() {

    companion object {
        private const val ACTION_OK_KEY = "key_action_ok"
        private const val ACTION_CANCEL_KEY = "key_action_cancel"
        private const val INITIAL_COLOR_KEY = "key_initial_color"
        private const val COLOR_MODEL_NAME_KEY = "key_color_model_name"
        private const val COLOR_MODEL_SWITCH_KEY = "key_color_model_switch"

        @JvmStatic
        private fun newInstance(
            @StringRes actionOk: Int,
            @StringRes actionCancel: Int,
            @ColorInt initialColor: Int,
            colorModel: ColorModel,
            colorModelSwitchEnabled: Boolean,
        ): ColorPickerDialog {
            val fragment = ColorPickerDialog()

            val initialColorCorrectAlpha = if (colorModel != ColorModel.ARGB) {
                ColorUtils.setAlphaComponent(initialColor, 255)
            } else {
                initialColor
            }

            fragment.arguments = makeArgs(
                actionOk,
                actionCancel,
                initialColorCorrectAlpha,
                colorModel,
                colorModelSwitchEnabled
            )
            return fragment
        }

        @JvmStatic
        private fun makeArgs(
            @StringRes actionOk: Int,
            @StringRes actionCancel: Int,
            @ColorInt initialColor: Int,
            colorModel: ColorModel,
            colorModelSwitchEnabled: Boolean,
        ) = bundleOf(
            ACTION_OK_KEY to actionOk,
            ACTION_CANCEL_KEY to actionCancel,
            INITIAL_COLOR_KEY to initialColor,
            COLOR_MODEL_NAME_KEY to colorModel.name,
            COLOR_MODEL_SWITCH_KEY to colorModelSwitchEnabled,
        )
    }

    /**
     * Builder for a color picker dialog
     */

    class Builder {
        @ColorInt
        private var initialColor: Int = ColorPickerView.defaultColor

        private var colorModel: ColorModel = ColorPickerView.defaultColorModel
        private var colorModelSwitchEnabled = true

        @StringRes
        private var actionOkStringRes: Int = android.R.string.ok

        @StringRes
        private var actionCancelStringRes: Int = android.R.string.cancel
        private var selectColorListener: OnSelectColorListener? = null
        private var switchColorModelListenerListener: OnSwitchColorModelListener? = null

        /**
         * Set initial color for a color picker dialog.
         * Default - Color.GRAY.
         * @param initialColor the color that will using as default in color picker dialog.
         */
        fun setInitialColor(@ColorInt initialColor: Int): Builder {
            this.initialColor = initialColor
            return this
        }

        /**
         * Set string resource for positive button.
         * Default - android.R.string.ok (OK).
         * @param stringId the String resource reference for positive button.
         */
        fun setButtonOkText(@StringRes stringId: Int): Builder {
            actionOkStringRes = stringId
            return this
        }

        /**
         * Set string resource for negative button.
         * Default - android.R.string.cancel (Cancel).
         * @param stringId the String resource reference for negative button.
         */
        fun setButtonCancelText(@StringRes stringId: Int): Builder {
            actionCancelStringRes = stringId
            return this
        }

        /**
         * Set color mode for a color picker dialog.
         * @param colorModel the colorMode for the color picker dialog
         *
         * Can be one of:
         * @see ColorModel.ARGB - alpha, red, green, blue.
         * @see ColorModel.RGB - red, green, blue
         * @see ColorModel.HSV - hue, saturation, value
         */
        fun setColorModel(colorModel: ColorModel): Builder {
            this.colorModel = colorModel
            return this
        }

        /**
         * Sets whether the color model can be switched.
         *
         * **Note:** it will work only if color model is ColorModel.RGB or ColorModel.HSV.
         * @param enabled is switching enabled.
         */

        fun setColorModelSwitchEnabled(enabled: Boolean): Builder {
            colorModelSwitchEnabled = enabled
            return this
        }

        /**
         * Set callback for a color picker dialog to return color, lambda edition.
         *
         * @param callback the callback to return color from color picker dialog.
         */
        fun onColorSelected(callback: (color: Int) -> Unit): Builder {
            this.selectColorListener = object : OnSelectColorListener {
                override fun onColorSelected(color: Int) {
                    callback(color)
                }
            }
            return this
        }

        /**
         * Set callback for a color picker dialog to return color.
         *
         * @param callback the callback to return color from color picker dialog.
         */
        fun onColorSelected(callback: OnSelectColorListener): Builder {
            this.selectColorListener = callback
            return this
        }

        /**
         * Set callback for a color picker dialog to return new color model, lambda edition.
         *
         * @param callback the callback to return new color model from color picker dialog.
         */
        fun onColorModelSwitched(callback: (colorModel: ColorModel) -> Unit): Builder {
            this.switchColorModelListenerListener = object : OnSwitchColorModelListener {
                override fun onColorModelSwitched(colorModel: ColorModel) {
                    callback(colorModel)
                }
            }
            return this
        }

        /**
         * Set callback for a color picker dialog to return new color model.
         *
         * @param callback the callback to return new color model from color picker dialog.
         */
        fun onColorModelSwitched(callback: OnSwitchColorModelListener): Builder {
            this.switchColorModelListenerListener = callback
            return this
        }

        /**
         * Creates an [ColorPickerDialog] with the arguments supplied to this builder.
         *
         * Calling this method does not display the dialog. If no additional
         * processing is needed, [show] may be called instead to both
         * create and display the dialog.
         */
        fun create(): ColorPickerDialog {
            requireNotNull(selectColorListener) { "You must call onColorSelected() before call create()" }
            val fragment = newInstance(
                actionOkStringRes,
                actionCancelStringRes,
                initialColor,
                colorModel,
                colorModelSwitchEnabled
            )
            fragment.onSelectColorListener = selectColorListener
            fragment.onSwitchColorModelListener = switchColorModelListenerListener
            return fragment
        }
    }

    private var onSelectColorListener: OnSelectColorListener? = null
    private var onSwitchColorModelListener: OnSwitchColorModelListener? = null
    var pickerView: ColorPickerView by Delegates.notNull()

    /**
     * Set callback for a color picker dialog to return color, lambda edition.
     * You should call this method in case your activity/fragment was restored.
     *
     * NOTE: find dialog instance by
     * ```
     * // In Activity
     * supportFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * // In Fragment
     * childFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * ```
     * @param callback the callback to return color from color picker dialog.
     */
    fun setOnSelectColorListener(callback: (color: Int) -> Unit) {
        this.onSelectColorListener = object : OnSelectColorListener {
            override fun onColorSelected(color: Int) {
                callback(color)
            }

        }
    }

    /**
     * Set callback for a color picker dialog to return color.
     * You should call this method in case your activity/fragment was restored.
     *
     * NOTE: find dialog instance by
     * ```
     * // In Activity
     * supportFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * // In Fragment
     * childFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * ```
     * @param callback the callback to return color from color picker dialog.
     */

    fun setOnSelectColorListener(callback: OnSelectColorListener) {
        this.onSelectColorListener = callback
    }

    /**
     * Set callback for a color picker dialog to return new color model, lambda edition.
     * You should call this method in case your activity/fragment was restored.
     *
     * NOTE: find dialog instance by
     * ```
     * // In Activity
     * supportFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * // In Fragment
     * childFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * ```
     * @param callback the callback to return new color model from color picker dialog.
     */
    fun setOnSwitchColorModelListener(callback: (colorModel: ColorModel) -> Unit) {
        this.onSwitchColorModelListener = object : OnSwitchColorModelListener {
            override fun onColorModelSwitched(colorModel: ColorModel) {
                callback(colorModel)
            }
        }
    }

    /**
     * Set callback for a color picker dialog to return new color model.
     * You should call this method in case your activity/fragment was restored.
     *
     * NOTE: find dialog instance by
     * ```
     * // In Activity
     * supportFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * // In Fragment
     * childFragmentManager.findFragmentByTag("your_tag_from_show()_method") as ColorPickerDialog?
     * ```
     * @param callback the callback to return new color model from color picker dialog.
     */
    fun setOnSwitchColorModelListener(callback: OnSwitchColorModelListener) {
        this.onSwitchColorModelListener = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = savedInstanceState ?: requireArguments()
        val actionOk = bundle.getInt(ACTION_OK_KEY)
        val actionCancel = bundle.getInt(ACTION_CANCEL_KEY)

        pickerView = ColorPickerView(
            requireContext(),
            actionOk,
            actionCancel,
            bundle.getInt(INITIAL_COLOR_KEY),
            ColorModel.fromName(bundle.getString(COLOR_MODEL_NAME_KEY)),
            bundle.getBoolean(COLOR_MODEL_SWITCH_KEY),
            onSwitchColorModelListener
        )

        pickerView.enableButtonBar(
            object : ColorPickerView.ButtonBarListener {
                override fun onNegativeButtonClick() = dismiss()
                override fun onPositiveButtonClick(color: Int) {
                    onSelectColorListener?.onColorSelected(color)
                    dismiss()
                }
            }
        )

        return AlertDialog.Builder(requireContext())
            .setView(pickerView)
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val bundle = makeArgs(
            pickerView.actionOkRes,
            pickerView.actionCancelRes,
            pickerView.currentColor,
            pickerView.colorModel,
            pickerView.colorModelSwitchEnabled
        )
        outState.putAll(bundle)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        onSelectColorListener = null
        onSwitchColorModelListener = null
        super.onDestroyView()
    }
}
