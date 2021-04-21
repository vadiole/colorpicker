# Color Picker — beautiful library for Android

<img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/1_l.png" alt="screenshot 1" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/1_d.png" alt="screenshot 2" width="23.9%" height="29.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/2_l.png" alt="screenshot 3" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/2_d.png" alt="screenshot 4" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/3_l.png" alt="screenshot 5" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/3_d.png" alt="screenshot 6" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/4_l.png" alt="screenshot 7" width="23.9%" height="23.9%"> <img src="https://raw.githubusercontent.com/vadiole/colorpicker/master/assets/4_d.png" alt="screenshot 8" width="23.9%" height="23.9%">


### Features
  - Simple dialog builder 
  - ARGB, RGB & HSV color models
  - Dark theme support
  - Sliders with gradient background
  - Switch color models in runtime


### Setup
```gradle
dependencies {
    implementation 'io.github.vadiole:colorpicker:1.0.1'
}
```

### Usage
```kotlin

//  create dialog
val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()

                //  set initial (default) color
                .setInitialColor(currentColor)

                //  set Color Model. ARGB, RGB or HSV
                .setColorModel(ColorModel.HSV)

                //  set is user be able to switch color model
                .setColorModelSwitchEnabled(true)

                //  set your localized string resource for OK button
                .setButtonOkText(android.R.string.ok)

                //  set your localized string resource for Cancel button
                .setButtonCancelText(android.R.string.cancel)

                //  callback for picked color (required)
                .onColorSelected { color: Int ->
                    //  use color
                }

                //  create dialog
                .create()
                
                
//  show dialog from Activity
colorPicker.show(supportFragmentManager, "color_picker") 

//  show dialog from Fragment
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
