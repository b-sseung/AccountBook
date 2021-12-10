package com.account.book.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.fragment.AccountAddFragment;

import java.util.ArrayList;

public class Modify_Account_Dialog extends AppCompatActivity {

    final int REQUEST_INTENT_4 = 104, REQUEST_INTENT_5 = 105;
    public static final int RESULT_CODE_OK = 15, RESULT_CODE_NO = 16;

    TextView modify_select_date_text, modify_title_text, modify_content_text, modify_money_text;
    TextView modify_modify_text, modify_delete_text, modify_close_text;
    CheckBox modify_checkBox1, modify_checkBox2;
    Button modify_date_button;
    Spinner modify_title_spinner, modify_content_spinner;
    EditText modify_content2_text, modify_money_edit;

    int load_id, intent_year, intent_month, intent_day, intent_money;
    String intent_what, intent_titlename, intent_content, intent_content2;

    int modify_select_year, modify_select_month, modify_select_day, modify_select_money;
    String modify_select_what, modify_select_titlename, modify_select_content, modify_select_content2;

    ArrayList<String> modify_plus_title = new ArrayList<>();
    ArrayList<String> modify_plus_content = new ArrayList<>();
    ArrayList<String> modify_minus_title = new ArrayList<>();
    ArrayList<String> modify_minus_content = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_modify);

        MainActivity.decorView.setSystemUiVisibility(MainActivity.uiOption);

        modify_select_date_text = findViewById(R.id.modify_select_date_text);
        modify_title_text = findViewById(R.id.modify_title_text);
        modify_content_text = findViewById(R.id.modify_content_text);
        modify_money_text = findViewById(R.id.modify_money_text);
        modify_checkBox1 = findViewById(R.id.modify_checkBox);
        modify_checkBox2 = findViewById(R.id.modify_checkBox2);
        modify_date_button = findViewById(R.id.modify_date_button);
        modify_title_spinner = findViewById(R.id.modify_title_spinner);
        modify_content_spinner = findViewById(R.id.modify_content_spinner);
        modify_content2_text = findViewById(R.id.modify_content2_text);
        modify_money_edit = findViewById(R.id.modify_money_edit);
        modify_modify_text = findViewById(R.id.modify_modify_text);
        modify_delete_text = findViewById(R.id.modify_delete_text);
        modify_close_text = findViewById(R.id.modify_close_text);

        loadModifySpinner();

        if (getIntent() != null){
            intent_year = getIntent().getIntExtra("year", 0);
            modify_select_year = intent_year;
            intent_month = getIntent().getIntExtra("month", 0);
            modify_select_month = intent_month;
            intent_day = getIntent().getIntExtra("day", 0);
            modify_select_day = intent_day;
            intent_what = getIntent().getStringExtra("what");
            intent_titlename = getIntent().getStringExtra("titlename");
            intent_content = getIntent().getStringExtra("content");
            intent_content2 = getIntent().getStringExtra("content2");
            intent_money = getIntent().getIntExtra("money", 0);

            if (intent_what.equals("수입")){
                modify_checkBox1.setChecked(true);

                setSpinnerAndCheckBox(1);

                for (int i = 0; i < modify_plus_title.size(); i++){
                    if (modify_plus_title.get(i).equals(intent_titlename)){
                        modify_title_spinner.setSelection(i);
                    }
                }
                for (int i = 0; i < modify_plus_content.size(); i++){
                    if (modify_plus_content.get(i).equals(intent_content)){
                        modify_content_spinner.setSelection(i);
                    }
                }
            } else if (intent_what.equals("지출")){
                modify_checkBox2.setChecked(true);

                setSpinnerAndCheckBox(2);

                for (int i = 0; i < modify_plus_title.size(); i++){
                    if (modify_minus_title.get(i).equals(intent_titlename)){
                        modify_title_spinner.setSelection(i);
                    }
                }
                for (int i = 0; i < modify_plus_content.size(); i++){
                    if (modify_minus_content.get(i).equals(intent_content)){
                        modify_content_spinner.setSelection(i);
                    }
                }
            }

            modify_select_date_text.setText(intent_year + "년 " + intent_month + "월 " + intent_day + "일");

            modify_content2_text.setText(intent_content2);
            modify_money_edit.setText(String.valueOf(intent_money));

        }

        modify_checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setSpinnerAndCheckBox(1);
                } else {
                    modify_checkBox2.setChecked(true);
                }
            }
        });

        modify_checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setSpinnerAndCheckBox(2);
                } else {
                    modify_checkBox1.setChecked(true);
                }
            }
        });

        modify_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(Modify_Account_Dialog.this, pickerListener,
                        intent_year, intent_month - 1, intent_day);

                picker.show();
            }
        });

        modify_title_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    modify_select_titlename = null;
                } else if (position == 1){
                    Intent intent = new Intent(getApplicationContext(), Add_Title_Dialog.class);

                    if (modify_select_what.equals("수입")) {
                        intent.putExtra("what", "수입");
                    } else if (modify_select_what.equals("지출")) {
                        intent.putExtra("what", "지출");
                    }

                    startActivityForResult(intent, REQUEST_INTENT_4);
                } else {
                    if (modify_select_what.equals("수입")){
                        modify_select_titlename = modify_plus_title.get(position);
                    } else if (modify_select_what.equals("지출")){
                        modify_select_titlename = modify_minus_title.get(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modify_title_spinner.setSelection(0);
                modify_select_titlename = null;
            }
        });

        modify_content_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    modify_select_content = null;
                } else if (position == 1){
                    Intent intent = new Intent(getApplicationContext(), Add_Content_Dialog.class);

                    if (modify_select_what.equals("수입")) {
                        intent.putExtra("what", "수입");
                    } else if (modify_select_what.equals("지출")) {
                        intent.putExtra("what", "지출");
                    }

                    startActivityForResult(intent, REQUEST_INTENT_5);
                } else {
                    if (modify_select_what.equals("수입")){
                        modify_select_content = modify_plus_content.get(position);
                    } else if (modify_select_what.equals("지출")){
                        modify_select_content = modify_minus_content.get(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modify_content_spinner.setSelection(0);
                modify_select_content = null;
            }
        });

        modify_modify_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modify_select_titlename == null || modify_select_content == null || modify_money_edit.getText().toString().equals("")){
                    ToastAndTextColor(4);

                    return;
                }

                if (modify_select_content.equals("결제 취소")){
                    if (!AccountAddFragment.selectCheck(modify_select_titlename)){
                        ToastAndTextColor(5);

                        return;
                    }
                }

                modifyAccountData();

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                intent.putExtra("year", modify_select_year);
                intent.putExtra("month", modify_select_month);
                setResult(RESULT_CODE_OK, intent);

                finish();

            }
        });

        modify_delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAccountData();

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                intent.putExtra("year", intent_year);
                intent.putExtra("month", intent_month);
                setResult(RESULT_CODE_OK, intent);

                finish();
            }
        });

        modify_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();

                setResult(RESULT_CODE_NO, intent);

                finish();

            }
        });
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            modify_select_year = year;
            modify_select_month = monthOfYear + 1;
            modify_select_day = dayOfMonth;

            modify_select_date_text.setText(modify_select_year + "년 " + modify_select_month + "월 " + modify_select_day + "일");
        }
    };

    public void setSpinnerAndCheckBox(int num){
        if (num == 1){
            modify_checkBox2.setChecked(false);
            modify_select_what = "수입";
            modify_title_text.setText("입금 선택  :  ");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, modify_plus_title);
            modify_title_spinner.setAdapter(adapter);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, modify_plus_content);
            modify_content_spinner.setAdapter(adapter2);

            modify_content_spinner.setSelection(0);
        } else if (num == 2){
            modify_checkBox1.setChecked(false);
            modify_select_what = "지출";
            modify_title_text.setText("결제 선택  :  ");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, modify_minus_title);
            modify_title_spinner.setAdapter(adapter);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, modify_minus_content);
            modify_content_spinner.setAdapter(adapter2);

            modify_content_spinner.setSelection(0);
        }
    }

    public int loadModifySpinner(){
        AccountDatabase.println("loadModifySpinner called.");

        String sql = "select _id, WHAT, TITLENAME, CONTENT from " + AccountDatabase.TABLE_TITLE +
                " order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            modify_plus_title = new ArrayList<>();
            modify_plus_content = new ArrayList<>();
            modify_minus_title = new ArrayList<>();
            modify_minus_content = new ArrayList<>();

            String[] alpha = new String[]{"선택해주세요.", "+  추가하기"};
            for (int i = 0; i < alpha.length; i++){
                modify_plus_title.add(alpha[i]);
                modify_plus_content.add(alpha[i]);
                modify_minus_title.add(alpha[i]);
                modify_minus_content.add(alpha[i]);
            }

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String what = cursor.getString(1);
                String title = cursor.getString(2); //입금된 곳 또는 출금된 곳을 의미
                String content = cursor.getString(3); //교통비, 급여 등등 항목을 의미

                if (what.equals("수입")){
                    if (!title.equals("")){
                        modify_plus_title.add(title);
                    } else if (!content.equals("")){
                        modify_plus_content.add(content);
                    }
                } else if (what.equals("지출")){
                    if (!title.equals("")){
                        modify_minus_title.add(title);
                    } else if (!content.equals("")){
                        modify_minus_content.add(content);
                    }
                }
            }
            cursor.close();
        }
        return recordCount;
    }

    public int loadModifyData(){
        AccountDatabase.println("loadModifyData called.");

        String sql = "select _id from " + AccountDatabase.TABLE_ACCOUNT +
                " where YEAR ='" + intent_year + "' and MONTH ='" + intent_month
                + "' and DAY ='" + intent_day + "' and TITLE ='" + intent_titlename
                + "' and CONTENT ='" + intent_content + "' and CONTENT2 ='" + intent_content2
                + "' and MONEY ='" + intent_money + "' order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);

                load_id = _id;
            }
            cursor.close();
        }
        return load_id;
    }

    public void modifyAccountData(){
        loadModifyData();

        AccountDatabase.println("modifyAccountData called.");

        modify_select_content2 = modify_content2_text.getText().toString();
        modify_select_money = Integer.valueOf(modify_money_edit.getText().toString());

        String sql = "update " + AccountDatabase.TABLE_ACCOUNT + " set "
                + " YEAR = '" + modify_select_year + "'" + " ,MONTH = '" + modify_select_month + "'"
                + " ,DAY = '" + modify_select_day + "'" + " ,WHAT = '" + modify_select_what + "'"
                + " ,TITLE ='" + modify_select_titlename + "'" + " ,CONTENT ='" + modify_select_content + "'"
                + ", CONTENT2 ='" + modify_select_content2 + "'" + " ,MONEY ='" +modify_select_money + "'"
                + " where _id =" + load_id;

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

    }

    public void deleteAccountData(){
        loadModifyData();

        AccountDatabase.println("deleteAccountData called.");

        String sql = "delete from " + AccountDatabase.TABLE_ACCOUNT + " where _id = " + load_id;

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

    }

    public void ToastAndTextColor(int num){
        LayoutInflater inflater = getLayoutInflater();


        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        if (num == 4){
            View layout = inflater.inflate(R.layout.toast_no_edittext_4, (ViewGroup) findViewById(R.id.toast_no_edittext_4));
            toast.setView(layout);

            if (modify_select_titlename == null){
                modify_title_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                modify_title_text.setTextColor(Color.parseColor("#000000"));
            }

            if (modify_select_content == null){
                modify_content_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                modify_content_text.setTextColor(Color.parseColor("#000000"));
            }

            if (modify_money_edit.getText().toString().equals("")){
                modify_money_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                modify_money_text.setTextColor(Color.parseColor("#000000"));
            }
        } else if (num == 5){
            View layout = inflater.inflate(R.layout.toast_no_edittext_5, (ViewGroup) findViewById(R.id.toast_no_edittext_5));
            toast.setView(layout);

            modify_content_spinner.setSelection(0);
            modify_content_text.setTextColor(Color.parseColor("#FF0000"));
        }

        toast.show();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if( hasFocus ) {
           MainActivity.decorView.setSystemUiVisibility( MainActivity.uiOption );
        }
    }

}
