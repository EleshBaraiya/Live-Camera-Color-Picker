# Live-Camera-Color-Picker
Pick color from live camera

# Your Activity
startActivityForResult(new Intent(MainActivity.this, PickColorActivity.class), PickColorActivity.REQUEST_PIC_COLOR);

# Get Color Code From Activity Result
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickColorActivity.REQUEST_PIC_COLOR) {
            String color_code = data.getExtras().getString(PickColorActivity.COLOR_CODE);
            mTextView.setBackgroundColor(Color.parseColor(color_code));
            System.out.println("==Colorcode==" + color_code);


        }
    }
