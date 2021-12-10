package com.account.book.fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.account.book.recyclerview6.PaymentItem;
import com.account.book.recyclerview8.DebitAdapter;
import com.account.book.recyclerview8.DebitItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityDebitFragment extends Fragment {

    LinearLayout debit_layout1, debit_text2_4_layout;
    FrameLayout debit_layout2;
    RecyclerView debit_recyclerview;
    TextView debit_text1_1, debit_text1_2, debit_text1_3, debit_text2_1, debit_text2_2, debit_text2_3, debit_text2_4,
            debit_search_text, debit_money_text, debit_text2_4_1, debit_text2_4_2, no_debit_text,
            debit_text3_0, debit_text3_1, debit_text3_2, debit_text3_3, debit_text3_4, debit_text3_5, debit_text3_6;
    Button debit_ok_text, debit_close_text;
    Spinner debit_content_spinner, debit_card_spinner;

    ArrayList<DebitItem> items = new ArrayList<>();
    DebitAdapter adapter = new DebitAdapter();

    ArrayList<String> card_list = new ArrayList<>();
    ArrayList<String> content_list = new ArrayList<>();

    boolean click_value = true;

    DatePickerDialog picker1, picker2;
    int year1, month1, day1, year2, month2, day2;
    String card_string = "전체", content_string = "전체", clear_string = "전체";
    SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");

    Animation trans_visible1, trans_invisible1, trans_down, trans_up;
    AnimListener1 listener1 = new AnimListener1();
    AnimListener2 listener2 = new AnimListener2();

    private AdView mAdView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_debit_fragment, container, false);



        debit_layout1 = rootView.findViewById(R.id.debit_layout1);
        debit_recyclerview= rootView.findViewById(R.id.debit_recyclerview);
        debit_text1_1 = rootView.findViewById(R.id.debit_text1_1);
        debit_text1_2 = rootView.findViewById(R.id.debit_text1_2);
        debit_text1_3 = rootView.findViewById(R.id.debit_text1_3);
        debit_text2_1 = rootView.findViewById(R.id.debit_text2_1);
        debit_text2_2 = rootView.findViewById(R.id.debit_text2_2);
        debit_text2_3 = rootView.findViewById(R.id.debit_text2_3);
        debit_text2_4 = rootView.findViewById(R.id.debit_text2_4);
        debit_money_text = rootView.findViewById(R.id.debit_money_text);
        debit_ok_text = rootView.findViewById(R.id.debit_ok_text);
        debit_close_text = rootView.findViewById(R.id.debit_close_text);
        debit_search_text = rootView.findViewById(R.id.debit_search_text);
        debit_content_spinner = rootView.findViewById(R.id.debit_content_spinner);
        debit_card_spinner = rootView.findViewById(R.id.debit_card_spinner);
        debit_text2_4_layout = rootView.findViewById(R.id.debit_text2_4_layout);
        debit_text2_4_1 = rootView.findViewById(R.id.debit_text2_4_1);
        debit_text2_4_2 = rootView.findViewById(R.id.debit_text2_4_2);
        debit_layout2 = rootView.findViewById(R.id.debit_layout2);
        debit_text3_0 = rootView.findViewById(R.id.debit_text3_0);
        debit_text3_1 = rootView.findViewById(R.id.debit_text3_1);
        debit_text3_2 = rootView.findViewById(R.id.debit_text3_2);
        debit_text3_3 = rootView.findViewById(R.id.debit_text3_3);
        debit_text3_4 = rootView.findViewById(R.id.debit_text3_4);
        debit_text3_5 = rootView.findViewById(R.id.debit_text3_5);
        debit_text3_6 = rootView.findViewById(R.id.debit_text3_6);
        no_debit_text = rootView.findViewById(R.id.no_debit_text);

        trans_visible1 = new AnimationUtils().loadAnimation(getContext(), R.anim.trans_visible);
        trans_invisible1 = new AnimationUtils().loadAnimation(getContext(), R.anim.trans_invisible);
        trans_down = new AnimationUtils().loadAnimation(getContext(), R.anim.trans_down);
        trans_up = new AnimationUtils().loadAnimation(getContext(), R.anim.trans_up);

        trans_invisible1.setAnimationListener(listener1);
        trans_down.setAnimationListener(listener2);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        debit_recyclerview.setLayoutManager(manager);
        debit_recyclerview.setAdapter(adapter);

        setFirstSpinner();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, -7);
        setDateText(1, calendar1);
        setDateText(2, Calendar.getInstance());

        debit_text1_1.setBackgroundResource(R.drawable.background_3_1);
        debit_text2_1.setBackgroundResource(R.drawable.background_3_1);

        loadDebitRecyclerView(card_string, content_string, clear_string, year1, month1, day1, year2, month2, day2);

        debit_search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchLayout(click_value);
            }
        });

        debit_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchLayout(false);
            }
        });

        debit_ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchLayout(false);

                loadDebitRecyclerView(card_string, content_string, clear_string, year1, month1, day1, year2, month2, day2);
            }
        });

        debit_text1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text1_1.setBackgroundResource(R.drawable.background_3_1);
                debit_text1_2.setBackgroundResource(R.drawable.background_3);
                debit_text1_3.setBackgroundResource(R.drawable.background_3);

                clear_string = "전체";
            }
        });

        debit_text1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text1_1.setBackgroundResource(R.drawable.background_3);
                debit_text1_2.setBackgroundResource(R.drawable.background_3_1);
                debit_text1_3.setBackgroundResource(R.drawable.background_3);

                clear_string = "결제예정";
            }
        });

        debit_text1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text1_1.setBackgroundResource(R.drawable.background_3);
                debit_text1_2.setBackgroundResource(R.drawable.background_3);
                debit_text1_3.setBackgroundResource(R.drawable.background_3_1);

                clear_string = "결제완료";
            }
        });

        debit_text2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text2_1.setBackgroundResource(R.drawable.background_3_1);
                debit_text2_2.setBackgroundResource(R.drawable.background_3);
                debit_text2_3.setBackgroundResource(R.drawable.background_3);
                debit_text2_4.setBackgroundResource(R.drawable.background_3);

                Calendar calendar = Calendar.getInstance();
                setDateText(2, calendar);

                calendar.add(Calendar.DAY_OF_MONTH, -7);
                setDateText(1, calendar);
            }
        });

        debit_text2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text2_1.setBackgroundResource(R.drawable.background_3);
                debit_text2_2.setBackgroundResource(R.drawable.background_3_1);
                debit_text2_3.setBackgroundResource(R.drawable.background_3);
                debit_text2_4.setBackgroundResource(R.drawable.background_3);

                Calendar calendar = Calendar.getInstance();
                setDateText(2, calendar);

                calendar.add(Calendar.MONTH, -1);
                setDateText(1, calendar);
            }
        });

        debit_text2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text2_1.setBackgroundResource(R.drawable.background_3);
                debit_text2_2.setBackgroundResource(R.drawable.background_3);
                debit_text2_3.setBackgroundResource(R.drawable.background_3_1);
                debit_text2_4.setBackgroundResource(R.drawable.background_3);

                debit_layout2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                debit_layout2.startAnimation(trans_up);
            }
        });

        debit_text2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_text2_1.setBackgroundResource(R.drawable.background_3);
                debit_text2_2.setBackgroundResource(R.drawable.background_3);
                debit_text2_3.setBackgroundResource(R.drawable.background_3);
                debit_text2_4.setBackgroundResource(R.drawable.background_3_1);

                debit_text2_4_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });

        debit_text2_4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker1 = new DatePickerDialog(getContext(), pickerListener1, year1, month1, day1);
                picker1.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                picker1.show();
            }
        });

        debit_text2_4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker2 = new DatePickerDialog(getContext(), pickerListener2, year2, month2, day2);
                picker2.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                picker2.show();
            }
        });

        debit_text3_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(0);
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(1);
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(2);
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(3);
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(4);
                debit_layout2.startAnimation(trans_down);
            }
        });

        debit_text3_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout2(5);
                debit_layout2.startAnimation(trans_down);
            }
        });


        debit_card_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                card_string = card_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        debit_content_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                content_string = content_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView2 = rootView.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);

        return rootView;
    }

    class AnimListener1 implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            debit_layout1.setLayoutParams(params);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class AnimListener2 implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            debit_layout2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year1 = year;
            month1 = month;
            day1 = dayOfMonth;
            debit_text2_4_1.setText(format.format(new Date(year1, month1, day1)));
        }
    };

    public DatePickerDialog.OnDateSetListener pickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year2 = year;
            month2 = month;
            day2 = dayOfMonth;
            debit_text2_4_2.setText(format.format(new Date(year2, month2, day2)));
        }
    };

    public void setDateText(int num, Calendar calendar){
        if (num == 1){
            year1 = calendar.get(Calendar.YEAR);
            month1 = calendar.get(Calendar.MONTH);
            day1 = calendar.get(Calendar.DAY_OF_MONTH);
            debit_text2_4_1.setText(format.format(new Date(year1, month1, day1)));
        } else if (num == 2){
            year2 = calendar.get(Calendar.YEAR);
            month2 = calendar.get(Calendar.MONTH);
            day2 = calendar.get(Calendar.DAY_OF_MONTH);
            debit_text2_4_2.setText(format.format(new Date(year2, month2, day2)));
        }
    }

    public void setSearchLayout(boolean value){
        if (value){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            debit_layout1.setLayoutParams(params);

            debit_layout1.startAnimation(trans_visible1);
            click_value = false;
        } else {
            debit_layout1.startAnimation(trans_invisible1);

            click_value = true;
        }
    }

    public void clickLayout2(int num){
        Calendar calendar = Calendar.getInstance();
        if (num == 0){
            setDateText(2, calendar);

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            setDateText(1, calendar);
        } else {
            calendar.add(Calendar.MONTH, -num);

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            setDateText(1, calendar);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setDateText(2, calendar);
        }
    }

    public void setFirstLayout2(){
        Calendar calendar;
        ArrayList<String> date_list = new ArrayList<>();

        for (int i = 0; i < 6; i++){
            calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -i);

            date_list.add(calendar.get(Calendar.YEAR) + "년  " + (calendar.get(Calendar.MONTH) + 1) + "월");
        }

        debit_text3_1.setText(date_list.get(0));
        debit_text3_2.setText(date_list.get(1));
        debit_text3_3.setText(date_list.get(2));
        debit_text3_4.setText(date_list.get(3));
        debit_text3_5.setText(date_list.get(4));
        debit_text3_6.setText(date_list.get(5));
    }

    public void setFirstSpinner(){
        AccountDatabase.println("setFirstSpinner called.");

        String sql = "select _id, TITLE, CONTENT from " + AccountDatabase.TABLE_ACCOUNT +
                " where WHAT = '" + "지출" + "' order by TITLE asc";

        Log.d("1818", sql);
        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        card_list = new ArrayList<>();
        content_list = new ArrayList<>();

        card_list.add("전체");
        content_list.add("전체");

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                if (checkDebitTitle(title)){

                    boolean a_value = true;
                    for (int a = 0; a < card_list.size(); a++){
                        if (card_list.get(a).equals(title)){
                            a_value = false;
                        }
                    }

                    if (a_value){
                        card_list.add(title);
                    }

                    boolean b_value = true;
                    for (int b = 0; b < content_list.size(); b++){
                        if (content_list.get(b).equals(content)){
                            b_value = false;
                        }
                    }

                    if (b_value){
                        content_list.add(content);
                    }

                }

            }

            ArrayAdapter<String> card_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_theme2, card_list);
            ArrayAdapter<String> content_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_theme2, content_list);

            debit_card_spinner.setAdapter(card_adapter);
            debit_content_spinner.setAdapter(content_adapter);

            setFirstLayout2();

            cursor.close();

        }
    }

    public boolean checkDebitTitle(String titlename){
        AccountDatabase.println("checkDebitTitle called.");

        String sql = "select _id, TITLE from " + AccountDatabase.TABLE_TITLE + " where TITLENAME = '" + titlename + "' order by TITLE asc";

        Log.d("1818", sql);
        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String title = cursor.getString(1);

                if (title.equals("신용카드")){
                    return true;
                }
            }

            cursor.close();

        }

        return false;
    }

    public int loadDebitRecyclerView(String card, String content, String clear, int year1, int month1, int day1, int year2, int month2, int day2){
        AccountDatabase.println("loadDebitRecyclerView called.");

        String sql = null;

        if (card.equals("전체")){
            if (content.equals("전체")){
                if (clear.equals("전체")){
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " order by YEAR desc, MONTH desc, DAY desc";
                } else {
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where CLEAR = '" + clear + "' order by YEAR desc, MONTH desc, DAY desc";
                }
            } else {
                if (clear.equals("전체")){
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where CONTENT = '" + content + "' order by YEAR desc, MONTH desc, DAY desc";
                } else {
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where CONTENT = '" + content + "' and CLEAR = '" + clear + "' order by YEAR desc, MONTH desc, DAY desc";
                }
            }
        } else {
            if (content.equals("전체")){
                if (clear.equals("전체")){
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where TITLE = '" + card + "' order by YEAR desc, MONTH desc, DAY desc";
                } else {
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where TITLE = '" + card + "' and CLEAR = '" + clear + "' order by YEAR desc, MONTH desc, DAY desc";
                }
            } else {
                if (clear.equals("전체")){
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where TITLE = '" + card + "' and CONTENT = '" + content + "' order by YEAR desc, MONTH desc, DAY desc";
                } else {
                    sql = "select _id, YEAR, MONTH, DAY, WHAT, TITLE, CONTENT, CONTENT2, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT +
                            " where TITLE = '" + card + "' and CONTENT = '" + content + "' and CLEAR = '" + clear +
                            "' order by YEAR desc, MONTH desc, DAY desc";
                }
            }
        }

        Log.d("1818", sql);
        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        Date date1 = new Date(year1, month1, day1);
        Date date2 = new Date(year2, month2, day2);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            items = new ArrayList<>();
            int all_money = 0;

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                int day = cursor.getInt(3);
                String what = cursor.getString(4);
                String title = cursor.getString(5);
                String content1 = cursor.getString(6);
                String content2 = cursor.getString(7);
                int money = cursor.getInt(8);
                String clear1 = cursor.getString(9);

                Date date3 = new Date(year, month - 1, day);

                Log.d("1818", "date1 : " + date1 + ", date2 : " + date2 + ", date3 : " + date3);

                if (checkDebitTitle(title)) {
                    if (date1.compareTo(date3) <= 0 && date2.compareTo(date3) >= 0){
                        all_money = all_money + money;
                        items.add(new DebitItem(_id, year, month, day, title, content1, content2, money, clear1));
                    }
                }
            }

            cursor.close();

            debit_money_text.setText(MainActivity.setComma(all_money));
            adapter.setItems(items);
            adapter.notifyDataSetChanged();

            if (items.size() == 0){
                no_debit_text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

        }

        return recordCount;
    }
}
