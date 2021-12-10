package com.account.book.dialog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview5.SettingAdapter;

import java.util.ArrayList;

public class Modify_Title_Dialog extends AppCompatActivity {

    final int RESULT_CODE_OK = 19, RESULT_CODE_NO = 20;
    final int REQUEST_INTENT_8 = 108;

    TextView t_dial_title, t_dial_title2;
    EditText t_dial_edit, t_text_pay_day;
    Button t_dial_add, t_dial_close;

    CheckBox check_bank, check_checkcard, check_debitcard, check_etd;
    LinearLayout layout_card_1, layout_card_2, layout_card_3;
    Spinner t_card_spinner, t_pay_bank_spinner;
    String[] use_days;
    ArrayList<String> banknames = new ArrayList<>();

    String t_dial_what, t_dial_add_title, t_dial_titlename, t_dial_bankname;
    int t_dial_payday = -1, t_dial_use_day = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_title);

        t_dial_title = findViewById(R.id.t_dial_title);
        t_dial_title2 = findViewById(R.id.t_dial_title2);
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


        //일단 기본 설정 기타로
        check_etd.setChecked(false);
        check_bank.setChecked(false);
        check_checkcard.setChecked(false);
        check_debitcard.setChecked(false);

        t_dial_add.setText("수정");

        if (getIntent() != null){
            t_dial_what = getIntent().getStringExtra("what");
            t_dial_title.setText(t_dial_what);
            t_dial_title2.setText(" 항목 수정하기");

            t_dial_title.setText("");

            switch (getIntent().getStringExtra("title")) {
                case "계좌":
                    check_bank.setChecked(true);
                    t_dial_add_title = "계좌";
                    break;
                case "체크카드":
                    setSpinnerAdapter();
                    check_checkcard.setChecked(true);
                    checkBoxSetting(2);
                    for (int i = 0; i < banknames.size(); i++){
                        if (banknames.get(i).equals(getIntent().getStringExtra("bankname"))){
                            t_pay_bank_spinner.setSelection(i);
                        }
                    }
                    t_dial_add_title = "체크카드";
                    break;
                case "신용카드":
                    setSpinnerAdapter();
                    check_debitcard.setChecked(true);
                    checkBoxSetting(3);
                    for (int i = 0; i < banknames.size(); i++){
                        if (banknames.get(i).equals(getIntent().getStringExtra("bankname"))){
                            t_pay_bank_spinner.setSelection(i);
                        }
                    }
                    t_text_pay_day.setText(String.valueOf(getIntent().getIntExtra("payday", -1)));
                    t_card_spinner.setSelection(getIntent().getIntExtra("use", -1));
                    t_dial_add_title = "신용카드";
                    break;
                case "기타":
                    check_etd.setChecked(true);
                    t_dial_add_title = "기타";
                    break;
            }

            t_dial_edit.setText(getIntent().getStringExtra("titlename"));

        }



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
                            SettingAdapter.toastSetting(9);
                            return;
                        } else {
                            t_dial_payday = Integer.valueOf(t_text_pay_day.getText().toString());
                            if (t_dial_payday > 31){
                                SettingAdapter.toastSetting(10);
                                return;
                            }
                        }
                    }


                    if (check_debitcard.isChecked() | check_checkcard.isChecked()){
                        if (t_dial_bankname.equals("")){
                            SettingAdapter.toastSetting(9);

                            return;
                        }
                    }

                    if (checkSameName(t_dial_titlename, t_dial_add_title)){
                        modifyAccountTitle();

                        Intent intent = new Intent();
                        intent.putExtra("what", t_dial_what);
                        setResult(RESULT_CODE_OK, intent);
//                        SettingAdapter.loadCheckETD(ActivitySetFragment.num);

                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Modify_Title_YesNo_Dialog.class);
                        startActivityForResult(intent, REQUEST_INTENT_8);
//                        SettingAdapter.toastSetting(6);
                    }

                } else {

                    SettingAdapter.toastSetting(1);
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

                    checkBoxSetting(1);

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

                    checkBoxSetting(2);
                    setSpinnerAdapter();

                    t_dial_add_title = "체크카드";


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

                    checkBoxSetting(3);
                    setSpinnerAdapter();

                    t_dial_add_title = "신용카드";

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

                    checkBoxSetting(1);

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
                t_dial_use_day = 12;
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

                if (banknames.get(position).equals(getIntent().getStringExtra("titlename"))){
                    SettingAdapter.toastSetting(8);

                    t_pay_bank_spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                t_dial_bankname = "";
            }
        });

    }

    public void checkBoxSetting(int num){
        if (num == 1){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            layout_card_1.setLayoutParams(params);
            layout_card_2.setLayoutParams(params);
            layout_card_3.setLayoutParams(params);
        } else if (num == 2){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            layout_card_1.setLayoutParams(params);
            layout_card_2.setLayoutParams(params);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout_card_3.setLayoutParams(params2);

        } else if (num == 3){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout_card_1.setLayoutParams(params);
            layout_card_2.setLayoutParams(params);
            layout_card_3.setLayoutParams(params);

        }
    }

    public void modifyAccountTitle(){
        AccountDatabase.println("modifyAccountTitle called.");

        String sql = null;

        if (check_bank.isChecked() || check_etd.isChecked()) {

            sql = "update " + AccountDatabase.TABLE_TITLE + " set "
                    + " TITLE ='" + t_dial_add_title + "', " + "TITLENAME ='" + t_dial_titlename + "', "
                    + " CONTENT ='" + "', " + " PAYDAY ='" + "', " + " USE ='" + "', " + " BANKNAME ='" + "'"
                    + " where TITLENAME ='" + getIntent().getStringExtra("titlename") + "'";

        } else if (check_checkcard.isChecked()){

//            loadCardList();

            sql = "update " + AccountDatabase.TABLE_TITLE + " set "
                    + " TITLE ='" + t_dial_add_title + "', " + "TITLENAME ='" + t_dial_titlename + "', "
                    + " CONTENT ='" + "', " + " PAYDAY ='" + "', " + " USE ='" + "', "
                    + " BANKNAME ='" + t_dial_bankname + "'" + " where TITLENAME ='" + getIntent().getStringExtra("titlename") + "'";

        } else if (check_debitcard.isChecked()){

//            loadCardList();

            sql = "update " + AccountDatabase.TABLE_TITLE + " set "
                    + " TITLE ='" + t_dial_add_title + "', " + "TITLENAME ='" + t_dial_titlename + "', "
                    + " CONTENT ='" + "', "  + " PAYDAY ='" + t_dial_payday + "', " + " USE ='" + t_dial_use_day + "', "
                    + " BANKNAME ='" + t_dial_bankname + "'" + " where TITLENAME ='" + getIntent().getStringExtra("titlename") + "'";

        }

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);
    }

    public void setSpinnerAdapter(){
        //t_card_spinner 설정하기
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_theme, use_days);
        t_card_spinner.setAdapter(adapter);

        //t_paybank_spinner 설정하기
        if (loadBankName() == 0){

            SettingAdapter.toastSetting(3);

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

        String sql = "select _id, TITLE from " + AccountDatabase.TABLE_TITLE
                + " " + "where TITLE =" + "'체크카드'" + " or TITLE =" + "'신용카드'" + " order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

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

    public boolean checkSameName(String name, String title){
        AccountDatabase.println("checkSameName called.");

        String sql = "select _id, TITLE, TITLENAME from " + com.account.book.AccountDatabase.TABLE_TITLE
                + " " + "where TITLENAME ='" + name + "' order by _id desc";

        int recordCount = -1;
        AccountDatabase database = com.account.book.AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();

            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++){
                cursor.moveToNext();

                if (title.equals(cursor.getString(1))) {
                    return false;
                }
            }

            cursor.close();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Modify_Title_YesNo_Dialog.RESULT_CODE_OK) {
            modifyAccountTitle();
        }
    }
}
