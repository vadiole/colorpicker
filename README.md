[![maven central](https://img.shields.io/maven-central/v/io.github.vadiole/colorpicker?color=236dc22&labelColor=424242)](https://search.maven.org/artifact/io.github.vadiole/colorpicker/1.0.2/aar)
# Color Picker — beautiful library for Android

<img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/1_l.png" alt="screenshot 1" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/2_l.png" alt="screenshot 2" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/1_d.png" alt="screenshot 3" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/2_d.png" alt="screenshot 4" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/3_l.png" alt="screenshot 5" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/4_l.png" alt="screenshot 6" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/3_d.png" alt="screenshot 7" width="25%" height="25%"><img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/4_d.png" alt="screenshot 8" width="25%" height="25%">

### Features
  - Simple dialog builder 
  - ARGB, RGB & HSV color models
  - Dark theme support
  - Sliders with gradient background
  - Switch color models in runtime


### Setup
```gradle
dependencies {
    implementation 'io.github.vadiole:colorpicker:1.0.3'
}
```

### Usage
```kotlin

// Create dialog
val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()

                // Set initial (default) color
                .setInitialColor(currentColor)

                // Set Color Model. ARGB, RGB or HSV
                .setColorModel(ColorModel.HSV)

                // Set is user be able to switch color model
                .setColorModelSwitchEnabled(true)

                // Set your localized string resource for OK button
                .setButtonOkText(android.R.string.ok)

                // Set your localized string resource for Cancel button
                .setButtonCancelText(android.R.string.cancel)

                // Callback for picked color (required)
                .onColorSelected { color: Int ->
                    // Use color
                }

                // Create dialog
                .create()
                
                
// Show dialog from Activity
colorPicker.show(supportFragmentManager, "color_picker") 

// Show dialog from Fragment
colorPicker.show(childFragmentManager, "color_picker")      
```

### License
```
Copyright 2021 vadiole

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
