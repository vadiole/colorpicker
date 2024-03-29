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
    var currentColor: Int = Color.RED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colorView = findViewById<View>(R.id.color_view)
        val pickColorButton = findViewById<Button>(R.id.pick_color_button)
        val alphaChannelToggle = findViewById<SwitchCompat>(R.id.alpha_channel_toggle)
        val colorModelSwitchingToggle = findViewById<SwitchCompat>(R.id.color_model_switching_toggle)

        // Set current color as background
        colorView.setBackgroundColor(currentColor)

        // When button click -> pick color
        pickColorButton.setOnClickListener {

            // Create Builder
            val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()
                // Set initial (default) color
                .setInitialColor(currentColor)

                // Set Color Model, can be ARGB, RGB, AHSV or HSV
                .setColorModel(if (alphaChannelToggle.isChecked) ColorModel.ARGB else ColorModel.RGB)

                // Set is user be able to switch color model
                .setColorModelSwitchEnabled(colorModelSwitchingToggle.isChecked)

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
    }

    // Save current color to bundle before activity will be destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(colorKey, currentColor)
    }
}
