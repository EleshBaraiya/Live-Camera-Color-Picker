package com.tatwadeep.livecameracolorpicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tatwadeep.livecameracolorpickerlib.PickColorActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.tv_click);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, PickColorActivity.class), PickColorActivity.REQUEST_PIC_COLOR);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickColorActivity.REQUEST_PIC_COLOR) {
            String color_code = data.getExtras().getString(PickColorActivity.COLOR_CODE);
            mTextView.setBackgroundColor(Color.parseColor(color_code));
            System.out.println("==Colorcode==" + color_code);


        }
    }
}
