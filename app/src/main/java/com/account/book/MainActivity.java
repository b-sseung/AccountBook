package com.account.book;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.dialog.Modify_Account_Dialog;
import com.account.book.fragment.AccountAddFragment;
import com.account.book.fragment.ActivityChartFragment;
import com.account.book.fragment.ActivityDebitFragment;
import com.account.book.fragment.ActivityListFragment;
import com.account.book.fragment.ActivitySetFragment;
import com.account.book.fragment.ActivitySubFragment;
import com.account.book.recyclerview1.CalendarAdapter;
import com.account.book.recyclerview1.DayItem;
import com.account.book.recyclerview2.AccountAdapter;
import com.account.book.recyclerview2.AccountItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_INTENT_3 = 103, REQUEST_INTENT_10 = 110;

    Calendar calendar;
    RecyclerView calendarView, accountView;
    static CalendarAdapter adapter;
    static AccountAdapter adapter2;

    LinearLayout layout0, layout1, layout2;
    boolean open_close = true;
    TextView prev_text, next_text, text_year, text_month, layout0_text, all_money_title_text;

    public static TextView click_date_text;

    static int main_year, main_month;

    Button add_button;

    public static FrameLayout fragment_layout1, fragment_layout2, fragment_layout3, fragment_layout4, fragment_layout5, fragment_layout6;
    public static AccountAddFragment fragment1;
    static ActivitySubFragment fragment2;
    static ActivityChartFragment fragment3;
    static ActivityListFragment fragment4;
    static ActivitySetFragment fragment5;
    static ActivityDebitFragment fragment6;

    public static int data_year, data_month, data_day;
    private Calendar today = Calendar.getInstance();

    public static Context context;

    static int plus_money = 0, minus_money = 0, main_plus_money = 0, main_minus_money = 0;
    static TextView all_money_text, plus_money_text, minus_money_text;

    public static View decorView;
    public static int	uiOption;

    Animation trans_visible, trans_invisible;

    BottomNavigationView bottomMenu;

    public static ArrayList<String[]> debitCard_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        context = getApplicationContext();

        calendarView = findViewById(R.id.calendarView);
        accountView = findViewById(R.id.accountView);
        prev_text = findViewById(R.id.prev_text);
        next_text = findViewById(R.id.next_text);
        text_year = findViewById(R.id.text_year);
        text_month = findViewById(R.id.text_month);
        layout0_text = findViewById(R.id.layout0_text);
        layout0 = findViewById(R.id.layout0);
        add_button = findViewById(R.id.add_button);
        click_date_text = findViewById(R.id.click_date_text);
        all_money_text = findViewById(R.id.all_money_text);
        fragment_layout1 = findViewById(R.id.fragment_layout1);
        fragment_layout2 = findViewById(R.id.fragment_layout2);
        fragment_layout3 = findViewById(R.id.fragment_layout3);
        fragment_layout4 = findViewById(R.id.fragment_layout4);
        fragment_layout5 = findViewById(R.id.fragment_layout5);
        fragment_layout6 = findViewById(R.id.fragment_layout6);
        bottomMenu = findViewById(R.id.bottom_navigation);
        all_money_title_text = findViewById(R.id.all_money_title_text);

        fragment1 = new AccountAddFragment();
        fragment2 = new ActivitySubFragment();
        fragment3 = new ActivityChartFragment();
        fragment4 = new ActivityListFragment();
        fragment5 = new ActivitySetFragment();
        fragment6 = new ActivityDebitFragment();

        trans_visible = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_visible);
        trans_invisible = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_invisible);

        TransListener translistener = new TransListener();
        trans_invisible.setAnimationListener(translistener);

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);
        calendarView.setLayoutManager(manager);
        adapter = new CalendarAdapter();

        LinearLayoutManager manager2 = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true);
        accountView.setLayoutManager(manager2);
        adapter2 = new AccountAdapter();

        setCalendarDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));

        calendarView.setAdapter(adapter);
        accountView.setAdapter(adapter2);

        loadAccountData();

        data_year = today.get(Calendar.YEAR);
        data_month = today.get(Calendar.MONTH) + 1;
        data_day = today.get(Calendar.DAY_OF_MONTH);

        loadAccountViewData();

        //하단 홈버튼 표시할지 말지
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
//            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        prev_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                setCalendarDate(year, month);
                CalendarAdapter.holders.clear();

                loadAccountData();

            }
        });

        next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                setCalendarDate(year, month);
                CalendarAdapter.holders.clear();

                loadAccountData();
            }
        });

        layout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (open_close){

                    fragment_layout2.setVisibility(View.VISIBLE);
                    fragment_layout2.startAnimation(trans_visible);

                    if (fragment2.isAdded()){
                        getSupportFragmentManager().beginTransaction().remove(fragment2);
                        fragment2 = new ActivitySubFragment();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("YEAR", main_year);
                    bundle.putInt("MONTH", main_month);
                    fragment2.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout2, fragment2).commit();

                    layout0_text.setText("  ▲");
                    open_close = false;
                } else {
                    fragment_layout2.startAnimation(trans_invisible);

                    layout0_text.setText("  ▼");
                    open_close = true;
                }

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_layout1.setVisibility(View.VISIBLE);

                fragment1 = new AccountAddFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("today_year", data_year);
                bundle.putInt("today_month", data_month);
                bundle.putInt("today_day", data_day);
                fragment1.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout1, fragment1).commit();
            }
        });

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.main :
                        fragment_layout2.setVisibility(View.INVISIBLE);
                        fragment_layout3.setVisibility(View.INVISIBLE);
                        fragment_layout4.setVisibility(View.INVISIBLE);
                        fragment_layout5.setVisibility(View.INVISIBLE);
                        fragment_layout6.setVisibility(View.INVISIBLE);

                        getSupportFragmentManager().beginTransaction().remove(fragment2);
                        getSupportFragmentManager().beginTransaction().remove(fragment3);
                        getSupportFragmentManager().beginTransaction().remove(fragment4);
                        getSupportFragmentManager().beginTransaction().remove(fragment5);

                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        setCalendarDate(year, month);

                        loadAccountData();
                        loadAccountViewData();

                        break;

                    case R.id.list :
                        fragment_layout2.setVisibility(View.INVISIBLE);
                        fragment_layout3.setVisibility(View.INVISIBLE);
                        fragment_layout4.setVisibility(View.VISIBLE);
                        fragment_layout5.setVisibility(View.INVISIBLE);
                        fragment_layout6.setVisibility(View.INVISIBLE);

                        if (fragment4.isAdded()){
                            getSupportFragmentManager().beginTransaction().remove(fragment4);
                            fragment4 = new ActivityListFragment();
                        }

                        Bundle bundlelist = new Bundle();
                        bundlelist.putInt("today_year", today.get(Calendar.YEAR));
                        bundlelist.putInt("today_month", today.get(Calendar.MONTH));
                        bundlelist.putInt("today_day", today.get(Calendar.DAY_OF_MONTH));
                        fragment4.setArguments(bundlelist);

                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout4, fragment4).commit();

                        break;

                    case R.id.stats :
                        fragment_layout2.setVisibility(View.INVISIBLE);
                        fragment_layout3.setVisibility(View.VISIBLE);
                        fragment_layout4.setVisibility(View.INVISIBLE);
                        fragment_layout5.setVisibility(View.INVISIBLE);
                        fragment_layout6.setVisibility(View.INVISIBLE);

                        if (fragment3.isAdded()){
                            getSupportFragmentManager().beginTransaction().remove(fragment3);
                            fragment3 = new ActivityChartFragment();
                        }

                        Bundle bundlechart = new Bundle();
                        bundlechart.putInt("today_year", today.get(Calendar.YEAR));
                        bundlechart.putInt("today_month", today.get(Calendar.MONTH));
                        fragment3.setArguments(bundlechart);

                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout3, fragment3).commit();

                        break;

                    case R.id.set :
                        fragment_layout2.setVisibility(View.INVISIBLE);
                        fragment_layout3.setVisibility(View.INVISIBLE);
                        fragment_layout4.setVisibility(View.INVISIBLE);
                        fragment_layout5.setVisibility(View.VISIBLE);
                        fragment_layout6.setVisibility(View.INVISIBLE);

                        if (fragment5.isAdded()){
                            getSupportFragmentManager().beginTransaction().remove(fragment5);
                            fragment5 = new ActivitySetFragment();
                        }

//                        Bundle bundleset = new Bundle();
//                        bundleset.putInt("today_year", today.get(Calendar.YEAR));
//                        bundleset.putInt("today_month", today.get(Calendar.MONTH));
//                        bundleset.putInt("today_day", today.get(Calendar.DAY_OF_MONTH));
//                        fragment5.setArguments(bundleset);

                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout5, fragment5).commit();

                        break;
                    case R.id.card :
                        fragment_layout2.setVisibility(View.INVISIBLE);
                        fragment_layout3.setVisibility(View.INVISIBLE);
                        fragment_layout4.setVisibility(View.INVISIBLE);
                        fragment_layout5.setVisibility(View.INVISIBLE);
                        fragment_layout6.setVisibility(View.VISIBLE);

                        if (fragment6.isAdded()){
                            getSupportFragmentManager().beginTransaction().remove(fragment6);
                            fragment6 = new ActivityDebitFragment();
                        }

//                        Bundle bundleset = new Bundle();
//                        bundleset.putInt("today_year", today.get(Calendar.YEAR));
//                        bundleset.putInt("today_month", today.get(Calendar.MONTH));
//                        bundleset.putInt("today_day", today.get(Calendar.DAY_OF_MONTH));
//                        fragment5.setArguments(bundleset);

                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout6, fragment6).commit();

                        break;
                }

                return true;
            }
        });
    }

    public void setCalendarDate(int year, int month){

        ArrayList<DayItem> items = new ArrayList<>();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        text_year.setText(calendar.get(Calendar.YEAR) + "년");
        text_month.setText(calendar.get(Calendar.MONTH) + 1 + "월");
        all_money_title_text.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));

        if (calendar.get(Calendar.DAY_OF_WEEK) != 1){
            for (int i = 1; i < calendar.get(Calendar.DAY_OF_WEEK); i++){
                items.add(new DayItem("", 0, 0, 0, 0, 0));
            }
        }

        for (int a = 1; a <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); a++){
            calendar.set(Calendar.DAY_OF_MONTH, a);
            items.add(new DayItem(String.valueOf(a), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_WEEK), 0, 0));
        }

        main_year = calendar.get(Calendar.YEAR);
        main_month = calendar.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (calendar.get(Calendar.DAY_OF_WEEK) != 7){
            for (int i = calendar.get(Calendar.DAY_OF_WEEK) + 1; i <= 7; i++){
                items.add(new DayItem("", 0, 0, 0, 0, 0));
            }
        }

        adapter.setItems(items);
    }

    public static int loadAccountData(){
        AccountDatabase.println("loadAccountData called.");

        String sql = "select _id, YEAR, MONTH, DAY, WHAT, MONEY from " + AccountDatabase.TABLE_ACCOUNT +
                " order by YEAR desc, MONTH desc, DAY desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(context);

        Date date = returnDate(main_year, main_month);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            int day_pre = 0;

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                int day = cursor.getInt(3);
                String what = cursor.getString(4);
                int money = cursor.getInt(5);

                if (day_pre != day){
                    plus_money = 0;
                    minus_money = 0;
                }

                if (year == main_year){
                    if (month == main_month){

                        if (what.equals("수입")){
                            main_plus_money = main_plus_money + money;
                        } else if (what.equals("지출")){
                            main_minus_money = main_minus_money + money;
                        }

                        for (int a = 0; a < adapter.getItemCount(); a++){
                            if (adapter.getItem(a).getDay().equals(String.valueOf(day))){
                                if (what.equals("수입")){
                                    plus_money = plus_money + money;
                                    adapter.getItem(a).setPlus(plus_money);
                                } else if (what.equals("지출")){
                                    minus_money = minus_money + money;
                                    adapter.getItem(a).setMinus(minus_money);
                                }
                            }
                        }
                    }
                }

                Date date2 = returnDate(year, month);
                int date_value = date.compareTo(date2);

                if (date_value > 0){
                    if (what.equals("수입")){
                        main_plus_money = main_plus_money + money;
                    } else if (what.equals("지출")){
                        main_minus_money = main_minus_money + money;
                    }
                }

                day_pre = day;

            }

            cursor.close();

            int all_money = main_plus_money - main_minus_money;
            all_money_text.setText(setComma(all_money));

            main_plus_money = 0;
            main_minus_money = 0;

            adapter.notifyDataSetChanged();
            try{
                checkPayDate();
            } catch (Exception e){}
        }

        return recordCount;
    }

    public static int loadAccountViewData(){
        AccountDatabase.println("loadAccountViewData called.");

        String sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                + " " + "where YEAR =" + data_year + " and MONTH =" + data_month + " and DAY =" + data_day + " order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            ArrayList<AccountItem> items = new ArrayList<>();

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

                if (!content.equals("선결제")){
                    items.add(new AccountItem(what, title, content, content2, money));
                }
            }

            for (int i = 0; i < debitCard_list.size(); i++){
//                Log.d("database", "비교 : " + Integer.valueOf(debitCard_list.get(i)[2]) + ", " + data_day);

                if (Integer.valueOf(debitCard_list.get(i)[2]) == data_day){

                    items.add(new AccountItem("", "결제예정", debitCard_list.get(i)[0],
                            debitCard_list.get(i)[1], createPayDate(debitCard_list.get(i)[1], Integer.valueOf(debitCard_list.get(i)[3]))));

                }
            }


            cursor.close();
            adapter2.setItems(items);
            adapter2.notifyDataSetChanged();
        }

        return recordCount;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

    class TransListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animatrion) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            fragment_layout2.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public static Date returnDate(int year, int month){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        Date date = new Date(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INTENT_3){
            if (resultCode == Modify_Account_Dialog.RESULT_CODE_OK) {
                int year = data.getIntExtra("year", 0);
                int month = data.getIntExtra("month", 0);
                setCalendarDate(year, month - 1);
                loadAccountData();
                loadAccountViewData();
            }
        } else if (requestCode == ActivitySetFragment.REQUEST_INTENT_8){
            ActivitySetFragment.loadSetTitleData(ActivitySetFragment.num);

        }
    }

    public static void checkPayDate(){
        AccountDatabase.println("checkPayDate called.");

        String sql = "select TITLE, TITLENAME, PAYDAY, USE, BANKNAME from " + AccountDatabase.TABLE_TITLE
                + " where TITLE = '" + "신용카드" + "' and WHAT = '" + "지출" + "' order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(context);

        if (database != null) {
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            debitCard_list = new ArrayList<>();
            ArrayList<Integer> card_days = new ArrayList<>();

            for (int i = 0; i < recordCount; i++){
                cursor.moveToNext();

                String title = cursor.getString(0);
                String titlename = cursor.getString(1);
                int payday = cursor.getInt(2);
                int use = cursor.getInt(3);
                String bankname = cursor.getString(4);

                if (title.equals("신용카드")){
                    debitCard_list.add(new String[]{title, titlename, String.valueOf(payday), String.valueOf(use), bankname});

                    card_days.add(payday);
                }
            }
            adapter.setCardColor(card_days);
            cursor.close();
        }
    }

    public static int createPayDate(String name, int use) {

        int[] use_days2 = new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(data_year, data_month - 1, data_day);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(data_year, data_month - 1, data_day);

        int firstYear = -1, firstMonth = -1, firstDay = -1, lastYear = -1, lastMonth = -1, lastDay = -1;

        switch (use) {
            case 0: case 1: case 2: case 3: case 4: case 5:
            case 6: case 7: case 8: case 9: case 10: case 11:
                calendar1.add(Calendar.MONTH, -2);
                calendar1.set(Calendar.DAY_OF_MONTH, use_days2[use]);
                firstYear = calendar1.get(Calendar.YEAR);
                firstMonth = calendar1.get(Calendar.MONTH) + 1;
                firstDay = calendar1.get(Calendar.DAY_OF_MONTH);

                calendar2.add(Calendar.MONTH, -1);
                calendar2.set(Calendar.DAY_OF_MONTH, use_days2[use] - 1);
                lastYear = calendar2.get(Calendar.YEAR);
                lastMonth = calendar2.get(Calendar.MONTH) + 1;
                lastDay = calendar2.get(Calendar.DAY_OF_MONTH);

                break;

            case 12:
                calendar1.add(Calendar.MONTH, -1);
                calendar1.set(Calendar.DAY_OF_MONTH, use_days2[use]);
                firstYear = calendar1.get(Calendar.YEAR);
                firstMonth = calendar1.get(Calendar.MONTH) + 1;
                firstDay = calendar1.get(Calendar.DAY_OF_MONTH);

                calendar2.add(Calendar.MONTH, -1);
                lastYear = calendar2.get(Calendar.YEAR);
                lastMonth = calendar2.get(Calendar.MONTH) + 1;
                lastDay = calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);

                break;

            case 13: case 14: case 15: case 16: case 17: case 18: case 19:
            case 20: case 21: case 22: case 23: case 24: case 25: case 26:
            case 27: case 28: case 29:
                calendar1.add(Calendar.MONTH, -1);
                calendar1.set(Calendar.DAY_OF_MONTH, use_days2[use]);
                firstYear = calendar1.get(Calendar.YEAR);
                firstMonth = calendar1.get(Calendar.MONTH) + 1;
                firstDay = calendar1.get(Calendar.DAY_OF_MONTH);

                calendar2.set(Calendar.DAY_OF_MONTH, use_days2[use] - 1);
                lastYear = calendar2.get(Calendar.YEAR);
                lastMonth = calendar2.get(Calendar.MONTH) + 1;
                lastDay = calendar2.get(Calendar.DAY_OF_MONTH);

                break;
        }

        AccountDatabase.println("createPayDate called.");

        String sql1 = null, sql2 = null;

        if (firstMonth != lastMonth){
            sql1 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + firstYear + " and MONTH = " + firstMonth + " and DAY >=" + firstDay + " order by _id desc";

            sql2 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + lastYear + " and MONTH = " + lastMonth + " and DAY <= " + lastDay + " order by _id desc";
        } else {
            sql1 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + firstYear + " and MONTH = " + firstMonth + " and DAY >=" + firstDay + " order by _id desc";
            sql2 = null;
        }

        int recordCount1 = -1;
        int recordCount2 = -1;
        AccountDatabase database = AccountDatabase.getInstance(context);

        int return_money = 0;

        if (database != null) {

            Cursor cursor1 = database.rawQuery(sql1);
            Cursor cursor2 = database.rawQuery(sql2);

            recordCount1 = cursor1.getCount();
            AccountDatabase.println("record count1 : " + recordCount1 + "\n");

            for (int i = 0; i < recordCount1; i++){
                cursor1.moveToNext();

                int year = cursor1.getInt(0);
                int month = cursor1.getInt(1);
                int day = cursor1.getInt(2);
                String title = cursor1.getString(3);
                String content = cursor1.getString(4);
                String content2 = cursor1.getString(5);
                int money = cursor1.getInt(6);
                String clear = cursor1.getString(7);

                if (title.equals(name)){
                    if (clear.equals("결제예정")){
                        return_money = return_money + money;
                    }
                }

            }

            try{
                recordCount2 = cursor2.getCount();
                AccountDatabase.println("record count1 : " + recordCount1 + "\n");

                for (int i = 0; i < recordCount2; i++){
                    cursor2.moveToNext();

                    int year = cursor2.getInt(0);
                    int month = cursor2.getInt(1);
                    int day = cursor2.getInt(2);
                    String title = cursor2.getString(3);
                    String content = cursor2.getString(4);
                    String content2 = cursor2.getString(5);
                    int money = cursor2.getInt(6);
                    String clear = cursor1.getString(7);

                    if (title.equals(name)){
                        if (clear.equals("결제예정")){
                            return_money = return_money + money;
                        }
                    }
                }

            } catch (Exception e){ }

        }

        return return_money;
    }



    public static String setComma(int money){
        DecimalFormat format = new DecimalFormat("#,##0");

        return format.format(money);
    }


}
