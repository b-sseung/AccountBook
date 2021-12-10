package com.account.book.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview6.PaymentAdapter;
import com.account.book.recyclerview6.PaymentAdapter2;
import com.account.book.recyclerview6.PaymentItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityPaymentFragment extends Fragment {

    ViewGroup container;

    LinearLayout payment_next_text, payment_filter_layout, payment_next_layout1, payment_layout1, payment_layout2;
    TextView payment_close_text, payment_title_text, payment_next_layout_text1;
    EditText payment_next_layout_edit;
    CheckBox payment_next_layout_check1, payment_next_layout_check2;
    Button payment_next_layout_button1, payment_next_layout_button2, payment_layout2_button1, payment_layout2_button2;
    public static TextView payment_money_text;
    RecyclerView payment_recyclerview;
    RecyclerView payment_recyclerview2;
    Spinner payment_month_spinner, payment_clear_spinner;
    ArrayList<String> spinner_list1 = new ArrayList<>();
    ArrayList<String> spinner_list2 = new ArrayList<>();
    int spinner_year = 0, spinner_month = 0, spinner_num = 0;
    String spinner_text;

    PaymentAdapter adapter = new PaymentAdapter();
    PaymentAdapter2 adapter2 = new PaymentAdapter2();

    ArrayList<PaymentItem> items = new ArrayList<>();
    ArrayList<PaymentItem> items2 = new ArrayList<>();

    public static ArrayList<Boolean> items_checked = new ArrayList<>();
    public static ArrayList<Boolean> items_checked2 = new ArrayList<>();

    public static int all_money = 0;

    String get_titlename;

    Animation trans_left, trans_right1, trans_right2;
    TransRightAnimationListener listener = new TransRightAnimationListener();
    TransRightAnimationListener2 listener2 = new TransRightAnimationListener2();
    boolean anim_value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        this.container = container;

        payment_next_text = rootView.findViewById(R.id.payment_next_text);
        payment_close_text = rootView.findViewById(R.id.payment_close_text);
        payment_title_text = rootView.findViewById(R.id.payment_title_text);
        payment_money_text = rootView.findViewById(R.id.payment_money_text);
        payment_recyclerview = rootView.findViewById(R.id.payment_recyclerView);
        payment_recyclerview2 = rootView.findViewById(R.id.payment_recyclerView2);
        payment_filter_layout = rootView.findViewById(R.id.payment_filter_layout);
        payment_month_spinner = rootView.findViewById(R.id.payment_month_spinner);
        payment_clear_spinner = rootView.findViewById(R.id.payment_clear_spinner);
        payment_next_layout1 = rootView.findViewById(R.id.payment_next_layout1);
        payment_next_layout_text1 = rootView.findViewById(R.id.payment_next_layout_text1);
        payment_next_layout_edit = rootView.findViewById(R.id.payment_next_layout_edit);
        payment_next_layout_check1 = rootView.findViewById(R.id.payment_next_layout_check1);
        payment_next_layout_check2 = rootView.findViewById(R.id.payment_next_layout_check2);
        payment_next_layout_button1 = rootView.findViewById(R.id.payment_next_layout_button1);
        payment_next_layout_button2 = rootView.findViewById(R.id.payment_next_layout_button2);
        payment_layout2_button1 = rootView.findViewById(R.id.payment_layout2_button1);
        payment_layout2_button2 = rootView.findViewById(R.id.payment_layout2_button2);
        payment_layout1 = rootView.findViewById(R.id.payment_layout1);
        payment_layout2 = rootView.findViewById(R.id.payment_layout2);

        trans_left = AnimationUtils.loadAnimation(getContext(), R.anim.trans_left);
        trans_right1 = AnimationUtils.loadAnimation(getContext(), R.anim.trans_right);
        trans_right2 = AnimationUtils.loadAnimation(getContext(), R.anim.trans_right);
        trans_right1.setAnimationListener(listener);
        trans_right2.setAnimationListener(listener2);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        payment_recyclerview.setLayoutManager(manager);
        payment_recyclerview.setAdapter(adapter);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        payment_recyclerview2.setLayoutManager(manager2);
        payment_recyclerview2.setAdapter(adapter2);

        if (getArguments() != null){
            get_titlename = getArguments().getString("titlename");

            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

            if (getArguments().getString("payment").equals("선결제")){
                payment_title_text.setText("선결제");
                payment_layout1.setLayoutParams(params1);
                payment_layout2.setLayoutParams(params2);
                payment_filter_layout.setLayoutParams(params4);

                if (loadNoPayment() == 0) {
                    AccountAddFragment.toastSetting(13);
                    container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
                }

            } else if (getArguments().getString("payment").equals("결제취소")){
                payment_title_text.setText("결제취소");
                payment_layout1.setLayoutParams(params2);
                payment_layout2.setLayoutParams(params1);
                payment_filter_layout.setLayoutParams(params3);

                settingFirst();

                if (loadSpinnerData(spinner_num, spinner_year, spinner_month, spinner_text) == 0) {
                    AccountAddFragment.toastSetting(13);
                    container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
                }
            }
        }

        payment_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim_value = false;
                container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
            }
        });

        payment_next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_next_layout1.setVisibility(View.VISIBLE);
                payment_next_layout1.startAnimation(trans_left);

                payment_next_layout_text1.setText(MainActivity.setComma(all_money) + "원");
                payment_next_layout_check1.setChecked(true);
            }
        });

        payment_next_layout_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payment_next_layout_check2.setChecked(false);
                    payment_next_layout_edit.setText(MainActivity.setComma(all_money));
                }
            }
        });

        payment_next_layout_check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payment_next_layout_check1.setChecked(false);
                    payment_next_layout_edit.setText("");
                }
            }
        });


        payment_next_layout_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_next_layout1.startAnimation(trans_right2);
            }
        });

        payment_next_layout_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < items_checked.size(); i++){
                    if (items_checked.get(i)){
                        modifyPaymentClear(i);
                    }
                }
                anim_value = true;
                container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
            }
        });

        payment_layout2_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim_value = false;
                container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
            }
        });

        payment_layout2_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < items_checked2.size(); i++){
                    if (items_checked2.get(i)){
                        deletePaymentClear(i);
                    }
                }
                anim_value = true;
                container.findViewById(R.id.add_fragment_layout1).startAnimation(trans_right1);
            }
        });

        payment_month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_num = position;
                loadSpinnerData(spinner_num, spinner_year, spinner_month, spinner_text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payment_clear_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_text = spinner_list2.get(position);
                loadSpinnerData(spinner_num, spinner_year, spinner_month, spinner_text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public int loadNoPayment(){
        AccountDatabase.println("loadNoPayment called.");

        String sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                " where CLEAR = '" + "결제예정" + "' and TITLE = '" + get_titlename + "' and WHAT = '" + "지출" +
                "' order by YEAR desc, MONTH desc, DAY desc";

        Log.d("1818", sql);
        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            items = new ArrayList<>();
            items_checked = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                int day = cursor.getInt(3);
                String what = cursor.getString(4);
                String title = cursor.getString(5);
                String content = cursor.getString(6);
                String content2 = cursor.getString(7);
                int money = cursor.getInt(8);
                String clear = cursor.getString(9);

                items.add(new PaymentItem(false, _id, year, month, day, title, content, content2, money, clear));
                items_checked.add(false);
            }

            cursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        }

        return recordCount;
    }

    public void modifyPaymentClear(int num){
        AccountDatabase.println("modifyAccountData called.");

        PaymentItem item = adapter.getItem(num);

        String sql = "update " + AccountDatabase.TABLE_ACCOUNT + " set "
                + " CLEAR = '" + "결제완료" + "'" + " where _id= '" + item.getId() + "'";

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

        String titlename = loadTitleBank(item.getTitlename());

        String sql1 = "insert into " + AccountDatabase.TABLE_ACCOUNT
                + "(YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR) values(" + "'" + item.getYear() + "', "
                + "'" + item.getMonth() + "', " + "'" + item.getDay() + "', " + "'" + "" + "', "
                + "'" + titlename + "', " + "'" + "선결제" + "', " + "'" + "', " + "'" + item.getMoney() + "', "
                + "'" + "결제예정" + "')";
        database.execSQL(sql1);

    }

    public String loadTitleBank(String titlename){
        AccountDatabase.println("loadNoPayment called.");

        String bankname = null;

        String sql = "select _id, BANKNAME from " + AccountDatabase.TABLE_TITLE +
                " where TITLENAME = '" + titlename + "' order by _id desc";

        Log.d("1818", sql);
        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();
                bankname = cursor.getString(1);
                return bankname;
            }
            cursor.close();
        }
        return bankname;
    }


    class TransRightAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (anim_value){
                MainActivity.fragment_layout1.setVisibility(View.INVISIBLE);
                getFragmentManager().beginTransaction().remove(MainActivity.fragment1);

                MainActivity.decorView.setSystemUiVisibility( MainActivity.uiOption );
                MainActivity.loadAccountData();
                MainActivity.loadAccountViewData();

            } else {
                container.findViewById(R.id.add_fragment_layout1).setVisibility(View.INVISIBLE);
                getFragmentManager().beginTransaction().remove(AccountAddFragment.paymentfragment1);

                AccountAddFragment.add_content_spinner.setSelection(0);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }

    class TransRightAnimationListener2 implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationEnd(Animation animation) {
            payment_next_layout1.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }

    public void settingFirst(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        spinner_year = calendar.get(Calendar.YEAR);
        spinner_month = calendar.get(Calendar.MONTH);
        spinner_num = 0;

        spinner_list1.add("최근 3개월");

        for (int i = 0; i < 3; i ++){
            spinner_list1.add(calendar.get(Calendar.YEAR) + "년 " + calendar.get(Calendar.MONTH) + "월");
            calendar.add(Calendar.MONTH, -1);
        }

        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<>(getContext(), R.layout.spinner_theme2, spinner_list1);
        payment_month_spinner.setAdapter(spinner_adapter1);

        spinner_text = "전체";
        spinner_list2.add("전체");
        spinner_list2.add("결제예정");
        spinner_list2.add("결제완료");

        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<>(getContext(), R.layout.spinner_theme2, spinner_list2);
        payment_clear_spinner.setAdapter(spinner_adapter2);
    }

    public int loadSpinnerData(int num, int yy, int mm, String text) {
        AccountDatabase.println("loadSpinnerData called.");

        String sql = null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(yy, mm, 1);

        if (num == 2){
            calendar.add(Calendar.MONTH, -1);
        } else if (num == 3){
            calendar.add(Calendar.MONTH, -2);
        }

        Log.d("1818", String.valueOf(calendar));

        if (num != 0){
            if (text.equals("전체")){
                sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                        " where TITLE = '" + get_titlename + "' and WHAT = '" + "지출"
                        + "' and YEAR = '" + calendar.get(Calendar.YEAR) + "' and MONTH = '" + calendar.get(Calendar.MONTH) + "' order by DAY desc";
            } else {
                sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                        " where CLEAR = '" + text + "' and TITLE = '" + get_titlename + "' and WHAT = '" + "지출"
                        + "' and YEAR = '" + calendar.get(Calendar.YEAR) + "' and MONTH = '" + calendar.get(Calendar.MONTH)
                        + "' order by DAY desc";
            }
        } else {
            if (text.equals("전체")){
                sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                        " where TITLE = '" + get_titlename + "' and WHAT = '" + "지출" + "' order by YEAR desc, MONTH desc, DAY desc";
            } else {
                sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                        " where CLEAR = '" + text + "' and TITLE = '" + get_titlename + "' and WHAT = '" + "지출" + "' order by YEAR desc, MONTH desc, DAY desc";
            }
        }

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null) {
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            items2 = new ArrayList<>();
            items_checked2 = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                int day = cursor.getInt(3);
                String what = cursor.getString(4);
                String title = cursor.getString(5);
                String content = cursor.getString(6);
                String content2 = cursor.getString(7);
                int money = cursor.getInt(8);
                String clear = cursor.getString(9);

                if (num != 0){
                    items2.add(new PaymentItem(false, _id, year, month, day, title, content, content2, money, clear));
                    items_checked2.add(false);
                } else {
                    Date date1 = MainActivity.returnDate(yy, mm - 3);
                    Date date2 = new Date(year, month, day);

                    Log.d("1818", "date1 : " + date1 + "date2 : " + date2);

                    if (date1.compareTo(date2) < 0){
                        items2.add(new PaymentItem(false, _id, year, month, day, title, content, content2, money, clear));
                        items_checked2.add(false);
                    }

                }

            }

            cursor.close();

            adapter2.setItems(items2);
            adapter2.notifyDataSetChanged();
        }

        return recordCount;
    }

    public void deletePaymentClear(int num){
        AccountDatabase.println("modifyAccountData called.");

        PaymentItem item = adapter2.getItem(num);

        String sql = "delete from " + AccountDatabase.TABLE_ACCOUNT + " where _id = " + item.getId();

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

        if (item.getClear().equals("결제완료")){
            String titlename = loadTitleBank(item.getTitlename());

            String sql1 = "delete from " + AccountDatabase.TABLE_ACCOUNT + " where YEAR = '" + item.getYear()
                    + "' and MONTH = '" + item.getMonth() + "' and DAY = '" + item.getDay() + "' and CONTENT = '" + "선결제"
                    + "' and MONEY = '" + item.getMoney() + "'";

//            String sql1 = "insert into " + AccountDatabase.TABLE_ACCOUNT
//                    + "(YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR) values(" + "'" + item.getYear() + "', "
//                    + "'" + item.getMonth() + "', " + "'" + item.getDay() + "', " + "'" + "수입" + "', "
//                    + "'" + titlename + "', " + "'" + "결제취소입금" + "', " + "'" + "', " + "'" + item.getMoney() + "', "
//                    + "'" + "결제예정" + "')";
            database.execSQL(sql1);
        }
    }

}
