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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.fragment.AccountAddFragment;
import com.account.book.recyclerview5.SettingAdapter;

import java.util.ArrayList;

public class Add_Title_Dialog extends AppCompatActivity {

    public static final int RESULT_CODE_OK = 11;
    static final int RESULT_CODE_NO = 12;

    TextView t_dial_title;
    EditText t_dial_edit, t_text_pay_day;
    Button t_dial_add, t_dial_close;

    CheckBox check_bank, check_checkcard, check_debitcard, check_etd;
    LinearLayout layout_card_1, layout_card_2, layout_card_3;
    Spinner t_card_spinner, t_pay_bank_spinner;
    String[] use_days;
    ArrayList<String> banknames = new ArrayList<>();

    String t_dial_what, t_dial_add_title, t_dial_titlename, t_dial_bankname;
    int t_dial_payday = -1, t_dial_use_day = -1;
    boolean add_value = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_title);

        t_dial_title = findViewById(R.id.t_dial_title);
        t_dial_edit = findViewById(R.id.t_dial_edit);
        t_dial_add = findViewById(R.id.t_dial_add);
        t_dial_close = findViewById(R.id.t_dial_close);
        t_text_pay_day = findViewById(R.id.t_text_pay_day);
        check_bank = findViewById(R.id.check_bank);
        check_checkcard = findViewById(R.id.check_checkcard);
        check_debitcard = findViewById(R.id.check_debitcard);
        check_etd = findViewById(R.id.check_etd);
        layout_card_1 = findViewById(R.id.layout_card_1);
        layout_card_2 = findViewById(R.id.layout_card_2);
        layout_card_3 = findViewById(R.id.layout_card_3);
        t_card_spinner = findViewById(R.id.t_card_spinner);
        t_pay_bank_spinner = findViewById(R.id.t_pay_bank_spinner);

        use_days  = new String[]{"전전월 19일 ~ 전월 18일", "전전월 20일 ~ 전월 19일", "전전월 21일 ~ 전월 20일",
                "전전월 22일 ~ 전월 21일", "전전월 23일 ~ 전월 22일", "전전월 24일 ~ 전월 23일", "전전월 25일 ~ 전월 24일",
                "전전월 26일 ~ 전월 25일", "전전월 27일 ~ 전월 26일", "전전월 28일 ~ 전월 27일", "전전월 29일 ~ 전월 28일",
                "전전월 30일 ~ 전월 29일", "전월 1일 ~ 전월 말일", "전월 2일 ~ 당월 1일", "전월 3일 ~ 당월 2일", "전월 4일 ~ 당월 3일",
                "전월 5일 ~ 당월 4일", "전월 6일 ~ 당월 5일", "전월 7일 ~ 당월 6일", "전월 8일 ~ 당월 7일", "전월 9일 ~ 당월 8일",
                "전월 10일 ~ 당월 9일", "전월 11일 ~ 당월 10일", "전월 12일 ~ 당월 11일", "전월 13일 ~ 당월 12일", "전월 14일 ~ 당월 13일",
                "전월 15일 ~ 당월 14일", "전월 16일 ~ 당월 15일", "전월 17일 ~ 당월 16일", "전월 18일 ~ 당월 17일"};


        if (getIntent().getStringExtra("what") != null){
                t_dial_what = getIntent().getStringExtra("what");
                t_dial_title.setText(t_dial_what);
        }

        //일단 기본 설정 기타로
        check_etd.setChecked(true);
        check_bank.setChecked(false);
        check_checkcard.setChecked(false);
        check_debitcard.setChecked(false);

        t_dial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("what", t_dial_what);
                setResult(RESULT_CODE_NO, intent);

                finish();
            }
        });

        t_dial_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t_dial_titlename = t_dial_edit.getText().toString();

                if (!t_dial_titlename.equals("")){

                    if (check_debitcard.isChecked()){
                        if (t_text_pay_day.getText().toString().equals("")){

                            toastCheckError(9);

                            return;
                        } else {
                            t_dial_payday = Integer.valueOf(t_text_pay_day.getText().toString());

                            if (t_dial_payday > 31){
                                toastCheckError(10);

                                return;
                            }
                        }
                    }

                    if (check_debitcard.isChecked() | check_checkcard.isChecked()){
                        if (t_dial_bankname.equals("")){
                            toastCheckError(9);

                            return;
                        }
                    }

                    if (checkSameName(t_dial_titlename)){
                        saveAccountTitle();

                        Intent intent = new Intent();
                        intent.putExtra("what", t_dial_what);
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

        check_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    check_checkcard.setChecked(false);
                    check_debitcard.setChecked(false);
                    check_etd.setChecked(false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                    layout_card_1.setLayoutParams(params);
                    layout_card_2.setLayoutParams(params);
                    layout_card_3.setLayoutParams(params);

                    t_dial_add_title = "계좌";
                    t_dial_bankname = "";

                } else {
                    if (!check_checkcard.isChecked()){
                        if (!check_debitcard.isChecked()){
                            if (!check_etd.isChecked()){
                                check_etd.setChecked(true);
                            }
                        }
                    }
                }
            }
        });

        check_checkcard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    check_bank.setChecked(false);
                    check_debitcard.setChecked(false);
                    check_etd.setChecked(false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                    layout_card_1.setLayoutParams(params);
                    layout_card_2.setLayoutParams(params);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout_card_3.setLayoutParams(params2);

                    t_dial_add_title = "체크카드";

                    setSpinnerAdapter();

                } else {
                    if (!check_bank.isChecked()){
                        if (!check_debitcard.isChecked()){
                            if (!check_etd.isChecked()){
                                check_etd.setChecked(true);
                            }
                        }
                    }
                }
            }
        });

        check_debitcard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    check_bank.setChecked(false);
                    check_checkcard.setChecked(false);
                    check_etd.setChecked(false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout_card_1.setLayoutParams(params);
                    layout_card_2.setLayoutParams(params);
                    layout_card_3.setLayoutParams(params);

                    t_dial_add_title = "신용카드";

                    setSpinnerAdapter();

                } else {
                    if (!check_bank.isChecked()){
                        if (!check_checkcard.isChecked()){
                            if (!check_etd.isChecked()){
                                check_etd.setChecked(true);
                            }
                        }
                    }
                }
            }
        });

        check_etd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    check_bank.setChecked(false);
                    check_checkcard.setChecked(false);
                    check_debitcard.setChecked(false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                    layout_card_1.setLayoutParams(params);
                    layout_card_2.setLayoutParams(params);
                    layout_card_3.setLayoutParams(params);

                    t_dial_bankname = "";
                    t_dial_add_title = "기타";
                } else {
                    if (!check_bank.isChecked()){
                        if (!check_checkcard.isChecked()){
                            if (!check_debitcard.isChecked()){
                                check_etd.setChecked(true);
                            }
                        }
                    }
                }
            }
        });

        t_card_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t_dial_use_day = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                t_dial_payday = 12;
            }
        });

        t_pay_bank_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    t_dial_bankname = "";
                } else {
                    t_dial_bankname = banknames.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                t_dial_bankname = "";
            }
        });

    }

    public void saveAccountTitle(){
        AccountDatabase.println("saveAccountTitle called.");

        String sql = null;


        String[] strings = new String[]{"수입", "지출"};

        if (check_bank.isChecked() || check_etd.isChecked()) {

            for (int i = 0; i < strings.length; i++) {
                sql = "insert into " + AccountDatabase.TABLE_TITLE
                        + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + strings[i] + "', "
                        + "'" + t_dial_add_title + "', " + "'" + t_dial_titlename + "', " + "'" + "', " + "'" + "', " + "'" + "', " + "'" + t_dial_bankname + "')";

                AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
                database.execSQL(sql);
            }

        } else if (check_checkcard.isChecked()){

//            loadCardList();

            for (int i = 0; i < strings.length; i++) {
                sql = "insert into " + AccountDatabase.TABLE_TITLE
                        + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + strings[i] + "', "
                        + "'" + t_dial_add_title + "', " + "'" + t_dial_titlename + "', " + "'" + "', " + "'" + "', " + "'" + "', "
                        + "'" + t_dial_bankname + "')";

                AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
                database.execSQL(sql);
            }

        } else if (check_debitcard.isChecked()){

//            loadCardList();

            for (int i = 0; i < strings.length; i++){
                sql  = "insert into " + AccountDatabase.TABLE_TITLE
                        + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + strings[i] + "', "
                        + "'" + t_dial_add_title + "', " + "'" + t_dial_titlename + "', " + "'" + "', " + "'" + t_dial_payday + "', "
                        + "'" + t_dial_use_day + "', " + "'" + t_dial_bankname + "')";

                AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
                database.execSQL(sql);
            }
        }
    }

    public void setSpinnerAdapter(){
        //t_card_spinner 설정하기
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, use_days);
        t_card_spinner.setAdapter(adapter);

        t_card_spinner.setSelection(12);

        //t_paybank_spinner 설정하기
        if (loadBankName() == 0){

            toastCheckError(3);

            check_checkcard.setChecked(false);
            check_debitcard.setChecked(false);
            check_bank.setChecked(true);

        } else {
            loadBankName();

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, banknames);
            t_pay_bank_spinner.setAdapter(adapter2);
        }
    }

    public int loadCardList(){
        AccountDatabase.println("loadCardList called.");

        String sql1 = "select _id, TITLE from " + AccountDatabase.TABLE_TITLE
                + " " + "where TITLE =" + "'체크카드'" + " or TITLE =" + "'신용카드'" + " order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql1);

            recordCount = cursor.getCount();

            AccountDatabase.println("record count : " + recordCount + "\n");

            if (recordCount == 0){
                String sql2 = "insert into " + AccountDatabase.TABLE_TITLE
                        + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "수입" + "', "
                        + "'" + "', " + "'" + "', " + "'" + "결제 취소" + "', " + "'" + "', " + "'" + "', "
                        + "'" + "')";

                database.execSQL(sql2);
            }

            cursor.close();
        }

        return recordCount;
    }

    public int loadBankName(){
        AccountDatabase.println("loadBankName called.");

        String sql = "select _id, TITLE, TITLENAME from " + AccountDatabase.TABLE_TITLE
                + " " + "where TITLE =" + "'계좌'" + " order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        banknames = new ArrayList<>();
        banknames.add("선택해주세요.");

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();

            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String title = cursor.getString(1);
                String titlename = cursor.getString(2);

                int num = 0;
                for (int a = 0; a < banknames.size(); a++){
                    if (titlename.equals(banknames.get(a))) {
                        num = 1;
                    }
                }
                if (num == 0){
                    banknames.add(titlename);
                }
            }

            cursor.close();
        }

        return recordCount;
    }

    public boolean checkSameName(String name){
        AccountDatabase.println("checkSameName called.");

        String sql = "select _id, TITLE, TITLENAME from " + AccountDatabase.TABLE_TITLE
                + " " + "where TITLENAME ='" + name + "' order by _id desc";

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
