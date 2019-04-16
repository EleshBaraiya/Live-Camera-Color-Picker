# Live-Camera-Color-Picker
It is a very light weight library that uses the camera of your device to capture a color.
It analyses in real time the camera preview and extracts the color that you are pointing at.
You don't have to take a picture and then pinpoint the color you want: it all happens in real time.

[![](https://jitpack.io/v/EleshBaraiya/Live-Camera-Color-Picker.svg)](https://jitpack.io/#EleshBaraiya/Live-Camera-Color-Picker)

    repositories {	
		  maven { url 'https://jitpack.io' }
		 }

    implementation 'com.github.EleshBaraiya:Live-Camera-Color-Picker:0.1.0'

### Register in Manifest
    <activity android:name="com.tatwadeep.livecameracolorpickerlib.PickColorActivity" />

### Your Activity
    startActivityForResult(new Intent(MainActivity.this, PickColorActivity.class), PickColorActivity.REQUEST_PIC_COLOR);

### Get Color Code From Activity Result
   
       @Override
       protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickColorActivity.REQUEST_PIC_COLOR) {
            String color_code = data.getExtras().getString(PickColorActivity.COLOR_CODE);
            mTextView.setBackgroundColor(Color.parseColor(color_code));
            System.out.println("==Colorcode==" + color_code);


        }
    }

 ![alt text](https://github.com/EleshBaraiya/Live-Camera-Color-Picker/blob/master/screenshot.png)
