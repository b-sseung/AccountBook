package com.account.book.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.account.book.AccountDatabase;
import com.account.book.dialog.Add_Content_Dialog;
import com.account.book.dialog.Add_Title_Dialog;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview9.SavingItem;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class AccountAddFragment extends Fragment {

    final int REQUEST_INTENT_1 = 101, REQUEST_INTENT_2 = 102;

    static Context context;

    ViewGroup rootView;

    TextView add_close_text, add_add_text, add_select_date_text, add_title_text, add_content_text, add_money_text, add_content2_title;
    CheckBox checkBox1, checkBox2, add_content2_check1, add_content2_check2;
    Button add_date_button;
    static Spinner add_title_spinner, add_content_spinner, add_content2_spinner;
    EditText add_content2_text, add_money_edit;
    LinearLayout add_content2_layout, add_content2_layout2;

    int today_year, today_month, today_day;

    ArrayList<String> plus_title = new ArrayList<>();
    ArrayList<String> plus_content = new ArrayList<>();
    ArrayList<String> minus_title = new ArrayList<>();
    ArrayList<String> minus_content = new ArrayList<>();
    ArrayList<String> content2s = new ArrayList<>();

    int select_year, select_month, select_day, select_money, content2_num;
    String select_what, select_title, select_content, select_content2;

    static ActivityPaymentFragment paymentfragment1;
    static FrameLayout add_fragment_layout1;

    Animation trans_left;

    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.account_add, container, false);

        MainActivity.decorView.setSystemUiVisibility( MainActivity.uiOption );
        context = getActivity();

        add_close_text = rootView.findViewById(R.id.add_close_text);
        add_add_text = rootView.findViewById(R.id.add_add_text);
        add_select_date_text = rootView.findViewById(R.id.add_select_date_text);
        add_title_text = rootView.findViewById(R.id.add_title_text);
        checkBox1 = rootView.findViewById(R.id.checkBox);
        checkBox2 = rootView.findViewById(R.id.checkBox2);
        add_date_button = rootView.findViewById(R.id.add_date_button);
        add_title_spinner = rootView.findViewById(R.id.add_title_spinner);
        add_content_text = rootView.findViewById(R.id.add_content_text);
        add_content_spinner = rootView.findViewById(R.id.add_content_spinner);
        add_content2_text = rootView.findViewById(R.id.add_content2_text);
        add_money_text = rootView.findViewById(R.id.add_money_text);
        add_money_edit = rootView.findViewById(R.id.add_money_edit);
        add_content2_layout = rootView.findViewById(R.id.add_content2_layout);
        add_content2_layout2 = rootView.findViewById(R.id.add_content2_layout2);
        add_content2_check1 = rootView.findViewById(R.id.add_content2_check1);
        add_content2_check2 = rootView.findViewById(R.id.add_content2_check2);
        add_content2_spinner = rootView.findViewById(R.id.add_content2_spinner);
        add_content2_title = rootView.findViewById(R.id.add_content2_title);

        add_fragment_layout1 = rootView.findViewById(R.id.add_fragment_layout1);
        paymentfragment1 = new ActivityPaymentFragment();

        trans_left = AnimationUtils.loadAnimation(getContext(), R.anim.trans_left);

        if (getArguments() != null){
            today_year = getArguments().getInt("today_year");
            today_month = getArguments().getInt("today_month");
            today_day = getArguments().getInt("today_day");

            select_year = today_year;
            select_month = today_month;
            select_day = today_day;


            add_select_date_text.setText(today_year + "년 " + today_month + "월 " + today_day + "일");
        }

        loadTitleDdata();

        checkBox1.setChecked(true);  //프래그먼크 켜지면 수입 눌러져있게
        setSpinnerAndCheckBox(1);


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastAndTextColor(1);

                if (isChecked){
                    setSpinnerAndCheckBox(1);
                } else {
                    checkBox2.setChecked(true);
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastAndTextColor(1);

                if (isChecked){
                    setSpinnerAndCheckBox(2);
                } else {
                    checkBox1.setChecked(true);
                }
            }
        });


        add_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(getContext(), pickerListener,
                        today_year, today_month - 1, today_day);

                picker.show();
            }
        });

        add_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastAndTextColor(1);

                if (select_content.equals("저축") | select_content.equals("저축 출금")){
                    if (add_content2_check1.isChecked()){
                        if (add_content2_text.getText().toString().equals("")){
                            ToastAndTextColor(6);
                        }
                    }

                    if (add_content2_check2.isChecked()){
                        if (add_content2_spinner.getSelectedItemPosition() == 0){
                            ToastAndTextColor(6);
                        }
                    }
                }

                if(select_title == null || select_content == null || add_money_edit.getText().toString().equals("")){
                    ToastAndTextColor(4);

                    return;
                }

                select_money = Integer.valueOf(add_money_edit.getText().toString());

                if (select_content.equals("저축 출금")){
                    if (select_money > loadSavingMoney(content2s.get(content2_num))){
                        Log.d("1818", "tlqkf222" + content2s.get(content2_num));
                        toastSetting(14);

                        add_money_edit.setText("");
                        add_money_text.setTextColor(Color.parseColor("#FF0000"));
                        return;
                    }
                }


                saveAddClick();

                MainActivity.fragment_layout1.setVisibility(View.INVISIBLE);

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                getFragmentManager().beginTransaction().remove(MainActivity.fragment1).commit();

                MainActivity.decorView.setSystemUiVisibility(MainActivity.uiOption);

            }
        });

        add_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragment_layout1.setVisibility(View.INVISIBLE);

                if (getActivity().getCurrentFocus() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                getFragmentManager().beginTransaction().remove(MainActivity.fragment1).commit();

                MainActivity.decorView.setSystemUiVisibility(MainActivity.uiOption);
            }
        });

        add_title_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                select_title = "";
            } else if (position == 1){
                Intent intent = new Intent(getContext(), Add_Title_Dialog.class);

                if (select_what.equals("수입")){
                    intent.putExtra("what", "수입");
                } else if (select_what.equals("지출")){
                    intent.putExtra("what", "지출");
                }

                startActivityForResult(intent, REQUEST_INTENT_1);

            } else {
                if (select_what.equals("수입")){
                    select_title = plus_title.get(position);
                } else if (select_what.equals("지출")){
                    select_title = minus_title.get(position);
                }
            }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                add_title_spinner.setSelection(0);
                select_title = "";
            }
        });

        add_content_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    select_content = "";
                } else if (position == 1){
                    Intent intent = new Intent(getContext(), Add_Content_Dialog.class);

                    if (select_what.equals("수입")){
                        intent.putExtra("what", "수입");
                    } else if (select_what.equals("지출")){
                        intent.putExtra("what", "지출");
                    }

                    startActivityForResult(intent, REQUEST_INTENT_2);
                } else {
                    if (select_what.equals("수입")){
                        if (plus_content.get(position).equals("결제 취소")){
                            add_content2_spinner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                            add_content2_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            add_content2_text.setText("");

                            //만약 결제할 내역이 0이면 토스트 : 결제할 내역이 없습니다.

                            if (paymentfragment1.isAdded()){
                                getFragmentManager().beginTransaction().remove(paymentfragment1);
                                paymentfragment1 = new ActivityPaymentFragment();
                            }

                            if (select_title.equals("")){
                                ToastAndTextColor(5);

                                return;
                            }

                            if (!selectCheck(select_title)){
                                ToastAndTextColor(5);

                                return;
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("payment", "결제취소");
                            bundle.putString("titlename", select_title);
                            paymentfragment1.setArguments(bundle);

                            Log.d("1818", select_title);
                            getFragmentManager().beginTransaction().add(R.id.add_fragment_layout1, paymentfragment1).commit();

                            add_fragment_layout1.setVisibility(View.VISIBLE);
                            add_fragment_layout1.startAnimation(trans_left);

                        } else if (plus_content.get(position).equals("저축 출금")){
                            select_content = plus_content.get(position);

//                            add_content2_check2.setChecked(true);
                            content2_num = 0;
                            loadSavingContent2();
                            add_content2_spinner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            add_content2_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

                        } else {
                            add_content2_spinner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                            add_content2_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            add_content2_text.setText("");
                            select_content = plus_content.get(position);
                        }

                    } else if (select_what.equals("지출")){
                        if (minus_content.get(position).equals("선결제")){
                            add_content2_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

                            //만약 결제할 내역이 0이면 토스트 : 결제할 내역이 없습니다.

                            if (paymentfragment1.isAdded()){
                                getFragmentManager().beginTransaction().remove(paymentfragment1);
                                paymentfragment1 = new ActivityPaymentFragment();
                            }

                            if (select_title.equals("")){
                                ToastAndTextColor(5);

                                return;
                            }

                            if (!selectCheck(select_title)){
                                ToastAndTextColor(5);

                                return;
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("payment", "선결제");
                            bundle.putString("titlename", select_title);
                            paymentfragment1.setArguments(bundle);

                            Log.d("1818", select_title);
                            getFragmentManager().beginTransaction().add(R.id.add_fragment_layout1, paymentfragment1).commit();

                            add_fragment_layout1.setVisibility(View.VISIBLE);
                            add_fragment_layout1.startAnimation(trans_left);

                        } else if (minus_content.get(position).equals("저축")){
                            select_content = minus_content.get(position);
                            add_content2_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            add_content2_check1.setChecked(true);
                            add_content2_check2.setChecked(false);
                            add_content2_text.setText("");
                        } else {
                            add_content2_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                            select_content = minus_content.get(position);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                add_content_spinner.setSelection(0);
                select_content = "";
            }
        });

        add_content2_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    add_content2_check2.setChecked(false);
                    add_content2_spinner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    add_content2_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    add_content2_text.setText("");
                } else {
                    add_content2_check2.setChecked(true);
                }
            }
        });

        add_content2_check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    add_content2_check1.setChecked(false);
                    content2_num = 0;
                    loadSavingContent2();
                    add_content2_spinner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    add_content2_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                } else {
                    add_content2_check1.setChecked(true);
                }
            }
        });

        add_content2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    content2_num = 0;
                } else {
                    content2_num = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            select_year = year;
            select_month = monthOfYear + 1;
            select_day = dayOfMonth;
            add_select_date_text.setText(select_year + "년 " + select_month + "월 " + select_day + "일");
        }
    };

    public int loadSavingContent2(){
        AccountDatabase.println("loadSavingContent2 called.");

        String sql = "select _id, CONTENT2 from " + AccountDatabase.TABLE_ACCOUNT + " where CONTENT = '" + "저축" + "' order by CONTENT2 asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            content2s = new ArrayList<>();
            content2s.add("선택해주세요.");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String content2 = cursor.getString(1);

                boolean con_value = true;
                for (int a = 0; a < content2s.size(); a++){
                    if (content2s.get(a).equals(content2)){
                        con_value = false;
                    }
                }
                if (con_value){
                    content2s.add(content2);
                }
            }
            cursor.close();

            ArrayAdapter<String> con_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_theme, content2s);
            add_content2_spinner.setAdapter(con_adapter);
        }
        return recordCount;
    }


    public void setSpinnerAndCheckBox(int num){

        add_money_edit.setText("");
        if (num == 1){
            checkBox2.setChecked(false);
            select_what = "수입";
            add_title_text.setText("입금 선택  :  ");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_theme, plus_title);
            add_title_spinner.setAdapter(adapter);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_theme, plus_content);
            add_content_spinner.setAdapter(adapter2);

            add_content_spinner.setSelection(0);
        } else if (num == 2){
            checkBox1.setChecked(false);
            select_what = "지출";
            add_title_text.setText("결제 선택  :  ");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_theme, minus_title);
            add_title_spinner.setAdapter(adapter);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_theme, minus_content);
            add_content_spinner.setAdapter(adapter2);

            add_content_spinner.setSelection(0);
        }
    }

    public int loadTitleDdata(){

        AccountDatabase.println("loadTitleDdata called.");

        String sql = "select _id, WHAT, TITLENAME, CONTENT from " + AccountDatabase.TABLE_TITLE +
                " order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            plus_title = new ArrayList<>();
            plus_content = new ArrayList<>();
            minus_title = new ArrayList<>();
            minus_content = new ArrayList<>();

            String[] alpha = new String[]{"선택해주세요.", "+  추가하기"};
            for (int i = 0; i < alpha.length; i++){
                plus_title.add(alpha[i]);
                plus_content.add(alpha[i]);
                minus_title.add(alpha[i]);
                minus_content.add(alpha[i]);
            }

            if (recordCount == 0){
                saveFirstSetting();
                return 0;
            }

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String what = cursor.getString(1);
                String title = cursor.getString(2); //입금된 곳 또는 출금된 곳을 의미
                String content = cursor.getString(3); //교통비, 급여 등등 항목을 의미

                if (what.equals("수입")){
                    if (!title.equals("")){
                        plus_title.add(title);
                    } else if (!content.equals("")){
                        plus_content.add(content);
                    }
                } else if (what.equals("지출")){
                    if (!title.equals("")){
                        minus_title.add(title);
                    } else if (!content.equals("")){
                        minus_content.add(content);
                    }
                }
            }
            cursor.close();
        }
        return recordCount;
    }

    public void saveFirstSetting(){
        AccountDatabase.println("saveFirstSetting called.");

        String[] p_title = new String[]{"현금"};
        String[] p_content = new String[]{"급여", "용돈", "결제 취소", "저축 출금", "기타"};
        for (int i = 0; i < p_title.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "수입" + "', "
                    + "'" + p_title[i] + "', " + "'" + p_title[i] + "', " + "'" + "', " + "'" + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        for (int i = 0; i < p_content.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "수입" + "', "
                    + "'" + "', " + "'" + "', " + "'" + p_content[i] + "', " + "'"  + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        String[] m_title = new String[]{"현금"};
        String[] m_content = new String[]{"저축", "식비", "교통비", "선결제", "기타"};
        for (int i = 0; i < m_title.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "지출" + "', "
                    + "'" + m_title[i] + "', " + "'" + m_title[i] + "', " + "'" + "', " + "'" + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }
        for (int i = 0; i < m_content.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "지출" + "', "
                    + "'" + "', " + "'" + "', " + "'" + m_content[i] + "', " + "'"  + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        loadTitleDdata();
    }

    public void saveAddClick(){
        AccountDatabase.println("saveAddClick called.");

        if (add_content2_spinner.getHeight() == 0){
            select_content2 = add_content2_text.getText().toString();
        } else {
            select_content2 = content2s.get(content2_num);
        }

        String clear;

        if (!selectCheck(select_title)){
            clear = "결제완료";
        } else {
            clear = "결제예정";
        }

        String sql = "insert into " + AccountDatabase.TABLE_ACCOUNT
                + "(YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR) values(" + "'" + select_year + "', "
                + "'" + select_month + "', " + "'" + select_day + "', " + "'" + select_what + "', "
                + "'" + select_title + "', " + "'" + select_content + "', " + "'" + select_content2 + "', " + "'" + select_money + "', "
                + "'" + clear + "')";

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

        if (MainActivity.loadAccountData() % 5 == 0){
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//            "ca-app-pub-3940256099942544/1033173712"   //테스트
//            "ca-app-pub-8631957304793435/2368720881"   //실사
        }
        MainActivity.loadAccountViewData();



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INTENT_1){
            if (resultCode == Add_Title_Dialog.RESULT_CODE_OK){
                loadTitleDdata();

                if (data.getStringExtra("what").equals("수입")){
                    setSpinnerAndCheckBox(1);
                    add_title_spinner.setSelection(plus_title.size() - 1);
                } else if (data.getStringExtra("what").equals("지출")){
                    setSpinnerAndCheckBox(2);
                    add_title_spinner.setSelection(minus_title.size() - 1);
                }
            } else {
                add_title_spinner.setSelection(0);
            }
        } else if (requestCode == REQUEST_INTENT_2){
            if (resultCode == Add_Content_Dialog.RESULT_CODE_OK){
                loadTitleDdata();

                if (data.getStringExtra("what").equals("수입")){
                    setSpinnerAndCheckBox(1);
                    add_content_spinner.setSelection(plus_content.size() - 1);
                } else if (data.getStringExtra("what").equals("지출")){
                    setSpinnerAndCheckBox(2);
                    add_content_spinner.setSelection(minus_content.size() - 1);
                }
            } else {
                add_content_spinner.setSelection(0);
            }
        }
    }

    public static boolean selectCheck(String data){
        AccountDatabase.println("selectCheck called.");

        String sql = "select _id, TITLE, TITLENAME from " + AccountDatabase.TABLE_TITLE +
                " where TITLENAME='" + data + "' order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String title = cursor.getString(1); //입금된 곳 또는 출금된 곳을 의미
                String titlename = cursor.getString(2); //교통비, 급여 등등 항목을 의미

                if (!title.equals("체크카드") & !title.equals("신용카드")){
                    return false;
                }
            }
            cursor.close();
        }

        return true;
    }

    public static void toastSetting(int num){

        try{
            InputMethodManager inputMethodManager = (InputMethodManager) ((Activity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            Log.d("1818", "자판 내릴 것 없음.");
        }


        View layout = null;

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        Toast toast = new Toast(((Activity)context).getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        if (num == 1){
            layout = inflater.inflate(R.layout.toast_no_edittext_1, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_1));
        } else if (num == 3){
            layout = inflater.inflate(R.layout.toast_no_edittext_3, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_3));
        } else if (num == 6){
            layout = inflater.inflate(R.layout.toast_no_edittext_6, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_6));
        } else if (num == 9){
            layout = inflater.inflate(R.layout.toast_no_edittext_9, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_9));
        } else if (num == 8){
            layout = inflater.inflate(R.layout.toast_edittext_8, (ViewGroup) ((Activity)context).findViewById(R.id.toast_edittext_8));
        } else if (num == 10){
            layout = inflater.inflate(R.layout.toast_edittext_10, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_10));
        } else if (num == 12){
            layout = inflater.inflate(R.layout.toast_edittext_12, (ViewGroup) ((Activity)context).findViewById(R.id.toast_edittext_12));
        } else if (num == 13){
            layout = inflater.inflate(R.layout.toast_no_edittext_13, (ViewGroup) ((Activity)context).findViewById(R.id.toast_no_edittext_13));
        } else if (num == 14){
            layout = inflater.inflate(R.layout.toast_edittext_14, (ViewGroup) ((Activity)context).findViewById(R.id.toast_edittext_14));
        }

        toast.setView(layout);
        toast.show();
    }

    public void ToastAndTextColor(int num){
        LayoutInflater inflater = getLayoutInflater();


        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);



        if (num == 4){
            View layout = inflater.inflate(R.layout.toast_no_edittext_4, (ViewGroup) rootView.findViewById(R.id.toast_no_edittext_4));
            toast.setView(layout);

            if (select_title == ""){
                add_title_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                add_title_text.setTextColor(Color.parseColor("#000000"));
            }

            if (select_content == ""){
                add_content_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                add_content_text.setTextColor(Color.parseColor("#000000"));
            }

            if (add_money_edit.getText().toString().equals("")){
                add_money_text.setTextColor(Color.parseColor("#FF0000"));
            } else {
                add_money_text.setTextColor(Color.parseColor("#000000"));
            }
        } else if (num == 5){
            View layout = inflater.inflate(R.layout.toast_no_edittext_5, (ViewGroup) rootView.findViewById(R.id.toast_no_edittext_5));
            toast.setView(layout);

            add_content_spinner.setSelection(0);
            add_content_text.setTextColor(Color.parseColor("#FF0000"));
        } else if (num == 1){
            add_title_text.setTextColor(Color.parseColor("#000000"));
            add_content_text.setTextColor(Color.parseColor("#000000"));
            add_money_text.setTextColor(Color.parseColor("#000000"));
            add_content2_title.setTextColor(Color.parseColor("#000000"));
        } else if (num == 6){
            add_content2_title.setTextColor(Color.parseColor("#FF0000"));
        }

        try{
            toast.show();
        } catch (Exception e){

        }

    }

    public int loadSavingMoney(String text){
        AccountDatabase.println("loadSetSavingData called.");

        String sql = "select _id, WHAT, CONTENT2, MONEY from " + AccountDatabase.TABLE_ACCOUNT +
                " where CONTENT2 = '" + text + "' order by CONTENT2 desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        int all_money = 0;

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String what = cursor.getString(1);
                int money = cursor.getInt(3);

                if (what.equals("수입")){
                    all_money = all_money - money;
                } else {
                    all_money = all_money + money;
                }

            }
            cursor.close();

        }
        Log.d("1818", "tlqkf" + all_money);
        return all_money;
    }
}
