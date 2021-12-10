package com.account.book.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.R;

public class Modify_Title_YesNo_Dialog extends AppCompatActivity {

    Button ok_button, no_button;
    static final int RESULT_CODE_OK = 23, RESULT_CODE_NO = 24;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_title_yesno);

        ok_button = findViewById(R.id.yesno_ok_text);
        no_button = findViewById(R.id.yesno_no_text);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CODE_OK, intent);

                finish();
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CODE_NO, intent);

                finish();
            }
        });
    }
}
