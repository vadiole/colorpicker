package vadiole.colorpickerdemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class MainActivity : AppCompatActivity() {

    private val colorKey = "KEY_COLOR"

    @ColorInt
    var currentColor: Int = Color.DKGRAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colorView = findViewById<View>(R.id.color_view)
        val pickColor = findViewById<Button>(R.id.pick_color_button)
        val useAlpha = findViewById<SwitchCompat>(R.id.alpha_channel_toggle)
        val colorModelSwitchEnabled = findViewById<SwitchCompat>(R.id.color_model_switching_toggle)

        // Restore color and listeners after activity recreate
        if (savedInstanceState != null) {
            currentColor = savedInstanceState.getInt(colorKey)

            val colorPicker = supportFragmentManager.findFragmentByTag("color_picker") as ColorPickerDialog?
            colorPicker?.setOnSelectColorListener { color ->
                // Save color to variable
                currentColor = color

                // Set background color to view result
                colorView.setBackgroundColor(color)
            }

            colorPicker?.setOnSwitchColorModelListener { colorModel ->
                Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set current color as background
        colorView.setBackgroundColor(currentColor)

        // When button click -> pick color
        pickColor.setOnClickListener {

            // Create Builder
            val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()
                // Set initial (default) color
                .setInitialColor(currentColor)

                // Set Color Model. If use alpha - ARGB, else RGB. Use what your want
                .setColorModel(if (useAlpha.isChecked) ColorModel.ARGB else ColorModel.RGB)

                // Set is user be able to switch color model. If ARGB - switch not available
                .setColorModelSwitchEnabled(colorModelSwitchEnabled.isChecked)

                // Set your localized string resource for OK button
                .setButtonOkText(R.string.action_ok)

                // Set your localized string resource for Cancel button
                .setButtonCancelText(R.string.action_cancel)

                // Callback for switched color model
                .onColorModelSwitched { colorModel ->
                    Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT).show()
                }

                // Callback for picked color (required)
                .onColorSelected { color: Int ->

                    // Save color to variable
                    currentColor = color

                    // Set background color to view result
                    colorView.setBackgroundColor(color)
                }

                // Create dialog
                .create()

            // Show color picker with supportFragmentManager (or childFragmentManager in Fragment)
            colorPicker.show(supportFragmentManager, "color_picker")
        }


    }

    // Save current color to bundle before activity will be destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(colorKey, currentColor)
    }
}
