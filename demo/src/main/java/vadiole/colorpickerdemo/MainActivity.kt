package vadiole.colorpickerdemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class MainActivity : AppCompatActivity() {

    private val colorKey = "KEY_COLOR"

    @ColorInt
    var currentColor: Int = Color.LTGRAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colorView = findViewById<View>(R.id.color_view)
        val pickColor = findViewById<Button>(R.id.button_pick_color)
        val useAlpha = findViewById<CheckBox>(R.id.checkbox_use_alpha)
        val colorModelSwitchEnabled = findViewById<CheckBox>(R.id.chackbox_enabled_switch)


        //  restore color and listeners after activity recreate
        if (savedInstanceState != null) {
            currentColor = savedInstanceState.getInt(colorKey)

            val colorPicker = supportFragmentManager.findFragmentByTag("color_picker") as ColorPickerDialog?
            colorPicker?.setOnSelectColorListener { color ->
                //  save color to variable
                currentColor = color

                //  set background color to view result
                colorView.setBackgroundColor(color)
            }

            colorPicker?.setOnSwitchColorModelListener { colorModel ->
                Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT).show()
            }
        }



        //  set current color as background
        colorView.setBackgroundColor(currentColor)

        //  when button click -> pick color
        pickColor.setOnClickListener {

            //  Create Builder
            val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()
                //  set initial (default) color
                .setInitialColor(currentColor)

                //  set Color Model. If use alpha - ARGB, else RGB. Use what your want
                .setColorModel(if (useAlpha.isChecked) ColorModel.ARGB else ColorModel.RGB)

                //  set is user be able to switch color model. If ARGB - switch not available
                .setColorModelSwitchEnabled(colorModelSwitchEnabled.isChecked)

                //  set your localized string resource for OK button
                .setButtonOkText(R.string.action_ok)

                //  set your localized string resource for Cancel button
                .setButtonCancelText(R.string.action_cancel)

                //  callback for switched color model
                .onColorModelSwitched { colorModel ->
                    Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT).show()
                }

                //  callback for picked color (required)
                .onColorSelected { color: Int ->

                    //  save color to variable
                    currentColor = color

                    //  set background color to view result
                    colorView.setBackgroundColor(color)
                }

                //  create dialog
                .create()


            //  show color picker with supportFragmentManager (or childFragmentManager in Fragment)
            colorPicker.show(supportFragmentManager, "color_picker")
        }


    }

    //  save current color when
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(colorKey, currentColor)
    }
}
