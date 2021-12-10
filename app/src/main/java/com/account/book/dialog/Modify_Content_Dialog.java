package com.account.book.dialog;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.account.book.fragment.ActivitySetFragment;
import com.account.book.recyclerview5.SettingAdapter;

public class Modify_Content_Dialog extends AppCompatActivity {

    final int RESULT_CODE_OK = 21, RESULT_CODE_NO = 22;

    TextView c_dial_content, c_dial_content2;
    EditText c_dial_edit;
    Button c_dial_close, c_dial_add;

    String c_dial_what, c_dial_add_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_content);

        c_dial_content = findViewById(R.id.c_dial_content);
        c_dial_content2 = findViewById(R.id.c_dial_content2);
        c_dial_edit = findViewById(R.id.c_dial_edit);
        c_dial_close = findViewById(R.id.c_dial_close);
        c_dial_add = findViewById(R.id.c_dial_add);

        if (getIntent() != null){
            c_dial_content.setText("");
            c_dial_content2.setText("내역 수정하기");
            c_dial_add.setText("수정");
            c_dial_edit.setText(getIntent().getStringExtra("content"));
        }


        c_dial_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c_dial_add_content = c_dial_edit.getText().toString();

                if (!c_dial_add_content.equals("")){

                    if (checkSameName(c_dial_add_content)){
                        modifyAccountContent();

                        Intent intent = new Intent();
                        setResult(RESULT_CODE_OK, intent);

//                        SettingAdapter.loadCheckETD(ActivitySetFragment.num);

                        finish();
                    } else {
                        SettingAdapter.toastSetting(6);
                    }

                } else {
                    SettingAdapter.toastSetting(1);
                }

            }
        });

        c_dial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CODE_NO, intent);

                finish();
            }
        });

    }

    public void modifyAccountContent(){
        AccountDatabase.println("modifyAccountTitle called.");

        String sql = "update " + AccountDatabase.TABLE_TITLE + " set "
                + " CONTENT = '" + c_dial_add_content + "'" + " where CONTENT = '" + getIntent().getStringExtra("content") + "'";

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

//    public void toastSetting(int num){
//
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = null;
//
//        if (num == 1){
//            layout = inflater.inflate(R.layout.toast_no_edittext_1, (ViewGroup) findViewById(R.id.toast_no_edittext_1));
//        } else if (num == 6){
//            layout = inflater.inflate(R.layout.toast_no_edittext_6, (ViewGroup) findViewById(R.id.toast_no_edittext_6));
//        }
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
//    }
}
