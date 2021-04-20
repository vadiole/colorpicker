package vadiole.colorpicker

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import kotlin.properties.Delegates


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
            fragment.arguments =
                makeArgs(
                    actionOk,
                    actionCancel,
                    initialColor,
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
        private var actionOkStringRes: Int = R.string.action_ok

        @StringRes
        private var actionCancelStringRes: Int = R.string.action_cancel
        private var listener: OnPickColorListener? = null


        /**
         * Set initial color for a color picker dialog.
         * Default - Color.GRAY
         * <p>
         * @param initialColor the color that will using as default in color picker dialog
         */
        fun setInitialColor(@ColorInt initialColor: Int): Builder {
            this.initialColor = initialColor
            return this
        }


        /**
         * Set string resource for positive button.
         * Default - R.string.action_ok (OK)
         * <p>
         * @param stringId the String resource reference for positive button
         */
        fun setButtonOkText(@StringRes stringId: Int): Builder {
            actionOkStringRes = stringId
            return this
        }

        /**
         * Set string resource for negative button.
         * Default - R.string.action_cancel (Cancel)
         * <p>
         * @param stringId the String resource reference for negative button
         */
        fun setButtonCancelText(@StringRes stringId: Int): Builder {
            actionCancelStringRes = stringId
            return this
        }

        /**
         * Set color mode for a color picker dialog.
         * <p>
         * @param colorModel the colorMode for the color picker dialog.
         *
         * Can be one of:
         * @see ColorModel.ARGB - alpha, red, green, blue.
         * @see ColorModel.RGB - red, green, blue
         * @see ColorModel.HSV - hue, saturation, value
         */
        fun setColorMode(colorModel: ColorModel): Builder {
            this.colorModel = colorModel
            return this
        }

        /**
         * Sets whether the color model can be switched.
         * <strong>Note:</strong> it will work only if color model is ColorModel.RGB or ColorModel.HSV .
         * <p>
         * @param enabled is switching enabled.
         */

        fun setColorModelSwitchEnabled(enabled: Boolean): Builder {
            colorModelSwitchEnabled = enabled
            return this
        }

        /**
         * Set callback for a color picker dialog to return color.
         * <p>
         * @param callback thc callback to return color from color picker dialog.
         */
        fun onColorSelected(callback: OnPickColorListener): Builder {
            this.listener = callback
            return this
        }

        /**
         * Set callback for a color picker dialog to return color, lambda edition.
         * <p>
         * @param callback thc callback to return color from color picker dialog.
         */
        fun onColorSelected(callback: (color: Int) -> Unit): Builder {
            this.listener = object : OnPickColorListener {
                override fun onColorSelected(color: Int) {
                    callback(color)
                }

            }
            return this
        }

        /**
         * Creates an ColorPickerDialog with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        fun create(): ColorPickerDialog {
            requireNotNull(
                listener,
                { "You must call onColorSelected() before call create()" }
            )
            val fragment = newInstance(
                actionOkStringRes,
                actionCancelStringRes,
                initialColor,
                colorModel,
                colorModelSwitchEnabled
            )
            fragment.listener = listener
            return fragment
        }
    }

    private var listener: OnPickColorListener? = null
    var pickerView: ColorPickerView by Delegates.notNull()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = savedInstanceState ?: arguments!!
        val actionOk = bundle.getInt(ACTION_OK_KEY)
        val actionCancel = bundle.getInt(ACTION_CANCEL_KEY)

        pickerView = ColorPickerView(
            requireContext(),
            actionOk,
            actionCancel,
            bundle.getInt(INITIAL_COLOR_KEY),
            ColorModel.fromName(bundle.getString(COLOR_MODEL_NAME_KEY)),
            bundle.getBoolean(COLOR_MODEL_SWITCH_KEY),
        )

        pickerView.enableButtonBar(object : ColorPickerView.ButtonBarListener {
            override fun onNegativeButtonClick() = dismiss()
            override fun onPositiveButtonClick(color: Int) {
                listener?.onColorSelected(color)
                dismiss()
            }
        })

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
        listener = null
        super.onDestroyView()
    }
}
