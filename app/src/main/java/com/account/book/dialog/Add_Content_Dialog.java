package com.account.book.dialog;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.fragment.AccountAddFragment;
import com.account.book.recyclerview5.SettingAdapter;

public class Add_Content_Dialog extends AppCompatActivity {

    public static final int RESULT_CODE_OK = 13, RESULT_CODE_NO = 14;

    TextView c_dial_content;
    EditText c_dial_edit;
    Button c_dial_close, c_dial_add;

    String c_dial_what, c_dial_add_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_content);

        c_dial_content = findViewById(R.id.c_dial_content);
        c_dial_edit = findViewById(R.id.c_dial_edit);
        c_dial_close = findViewById(R.id.c_dial_close);
        c_dial_add = findViewById(R.id.c_dial_add);

        if (getIntent().getStringExtra("what") != null){
            c_dial_what = getIntent().getStringExtra("what");
            c_dial_content.setText(c_dial_what);
        }


        c_dial_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c_dial_add_content = c_dial_edit.getText().toString();

                if (!c_dial_add_content.equals("")){

                    if (checkSameName(c_dial_add_content)){
                        saveAccountContent();

                        Intent intent = new Intent();
                        intent.putExtra("what", c_dial_what);
                        setResult(RESULT_CODE_OK, intent);

                        finish();
                    } else {
                        toastCheckError(6);
                    }

                } else {
                    toastCheckError(1);
                }

            }
        });

        c_dial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("what", c_dial_what);
                setResult(RESULT_CODE_NO, intent);

                finish();
            }
        });

    }

    public void saveAccountContent(){
        AccountDatabase.println("saveAccountContent called.");

        String sql = "insert into " + AccountDatabase.TABLE_TITLE
                + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + c_dial_what + "', "
                + "'" + "', " + "'" + "', " + "'" + c_dial_add_content + "', " + "'" + "', " + "'" + "', " + "'" + "')";

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);
    }

    public boolean checkSameName(String name){
        AccountDatabase.println("checkSameName called.");

        String sql = "select _id, CONTENT from " + AccountDatabase.TABLE_TITLE
                + " " + "where CONTENT ='" + name + "' order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();

            AccountDatabase.println("record count : " + recordCount + "\n");

            if (recordCount != 0){
                return false;
            }

            cursor.close();
        }

        return true;
    }

    public void toastCheckError(int num){
        try{
            SettingAdapter.toastSetting(num);
        } catch (Exception e){
            Log.d("1818", "오류 발생");
        }

        try{
            AccountAddFragment.toastSetting(num);
        } catch (Exception e){
            Log.d("1818", "오류 발생");
        }
    }

}
