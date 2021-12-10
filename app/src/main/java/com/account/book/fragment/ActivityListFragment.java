package com.account.book.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview4.ListItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityListFragment extends Fragment {

    Calendar calendar;
    TextView list_prev_text, list_next_text, list_text_year, list_text_month, list_plus_text, list_minus_text, no_list_text, list_card_text;
    RecyclerView list_recyclerView;

    com.account.book.recyclerview4.ListAdapter adapter;

    int date_year, date_month, date_day;

    private AdView mAdView1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_list_fragment, container, false);



        list_prev_text = rootView.findViewById(R.id.list_prev_text);
        list_next_text = rootView.findViewById(R.id.list_next_text);
        list_text_year = rootView.findViewById(R.id.list_text_year);
        list_text_month = rootView.findViewById(R.id.list_text_month);
        list_recyclerView = rootView.findViewById(R.id.list_recyclerView);
        list_plus_text = rootView.findViewById(R.id.list_plus_text);
        list_minus_text = rootView.findViewById(R.id.list_minus_text);
        no_list_text = rootView.findViewById(R.id.no_list_text);
        list_card_text = rootView.findViewById(R.id.list_card_text);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        list_recyclerView.setLayoutManager(manager);
        adapter = new com.account.book.recyclerview4.ListAdapter();
        list_recyclerView.setAdapter(adapter);
        calendar = Calendar.getInstance();

        if (getArguments() != null){
            date_year = getArguments().getInt("today_year");
            date_month = getArguments().getInt("today_month") + 1;
            date_day = getArguments().getInt("today_day");


            calendar.set(Calendar.YEAR, date_year);
            calendar.set(Calendar.MONTH, date_month - 1);

            list_text_year.setText(date_year + "년");
            list_text_month.setText(date_month + "월");
        }

        createRecycler(date_year, date_month);
        createTopTitle(date_year, date_month);
        list_card_text.setText(MainActivity.setComma(fillCardText()));

        list_prev_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);

                createRecycler(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                createTopTitle(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

                list_text_year.setText(calendar.get(Calendar.YEAR) + "년");
                list_text_month.setText((calendar.get(Calendar.MONTH) + 1) + "월");

                list_card_text.setText(MainActivity.setComma(fillCardText()));

            }
        });

        list_next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);

                createRecycler(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                createTopTitle(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

                list_text_year.setText(calendar.get(Calendar.YEAR) + "년");
                list_text_month.setText((calendar.get(Calendar.MONTH) + 1) + "월");

                list_card_text.setText(MainActivity.setComma(fillCardText()));

            }
        });

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView1 = rootView.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

        return rootView;
    }

    public int createRecycler(int year, int month){
        AccountDatabase.println("createRecycler called.");

        String sql = "select _id, YEAR, MONTH, DAY from " + AccountDatabase.TABLE_ACCOUNT
                + " " + "where YEAR =" + year + " and MONTH =" + month + " order by DAY desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            ArrayList<ListItem> items = new ArrayList<>();
            ArrayList<Integer> days = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int get_year = cursor.getInt(1);
                int get_month = cursor.getInt(2);
                int get_day = cursor.getInt(3);

                int checknum = 0;

                for (int a = 0; a < days.size(); a++){

                    if (days.get(a) == get_day){
                        checknum = 1;
                    }
                }

                if (checknum == 0){
                    days.add(get_day);
                    items.add(new ListItem(get_year, get_month, get_day));
                }

            }

            cursor.close();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        if (recordCount != 0){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(0, 0);
            no_list_text.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            no_list_text.setLayoutParams(params);
        }

        return recordCount;
    }

    public int createTopTitle(int year, int month){
        AccountDatabase.println("createTopTitle called.");

        String sql = "select _id, WHAT, MONEY from " + AccountDatabase.TABLE_ACCOUNT
                + " " + "where YEAR =" + year + " and MONTH =" + month + " order by _id desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            int plus_money = 0;
            int minus_money = 0;

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String what = cursor.getString(1);
                int money = cursor.getInt(2);

                if (what.equals("수입")){
                    plus_money = plus_money + money;
                } else if (what.equals("지출")){
                    minus_money = minus_money + money;
                }

            }

            cursor.close();
            list_plus_text.setText(MainActivity.setComma(plus_money));
            list_minus_text.setText(MainActivity.setComma(minus_money));
       }

        return recordCount;
    }

    public int fillCardText(){
        int[] use_days2 = new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();



        int firstYear = -1, firstMonth = -1, firstDay = -1, lastYear = -1, lastMonth = -1, lastDay = -1;
        int return_money = 0;

        for (int i = 0; i < MainActivity.debitCard_list.size(); i++){
            calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            Log.d("1818", "use : " + Integer.valueOf(MainActivity.debitCard_list.get(i)[3]));

            switch (Integer.valueOf(MainActivity.debitCard_list.get(i)[3])) {
                case 0: case 1: case 2: case 3: case 4: case 5:
                case 6: case 7: case 8: case 9: case 10: case 11:
                    calendar1.add(Calendar.MONTH, -2);
                    calendar1.set(Calendar.DAY_OF_MONTH, use_days2[Integer.parseInt(MainActivity.debitCard_list.get(i)[3])]);
                    firstYear = calendar1.get(Calendar.YEAR);
                    firstMonth = calendar1.get(Calendar.MONTH) + 1;
                    firstDay = calendar1.get(Calendar.DAY_OF_MONTH);

                    calendar2.add(Calendar.MONTH, -1);
                    calendar2.set(Calendar.DAY_OF_MONTH, use_days2[Integer.parseInt(MainActivity.debitCard_list.get(i)[3])] - 1);
                    lastYear = calendar2.get(Calendar.YEAR);
                    lastMonth = calendar2.get(Calendar.MONTH) + 1;
                    lastDay = calendar2.get(Calendar.DAY_OF_MONTH);


                    break;

                case 12:
                    calendar1.add(Calendar.MONTH, -1);
                    calendar1.set(Calendar.DAY_OF_MONTH, use_days2[Integer.parseInt(MainActivity.debitCard_list.get(i)[3])]);
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
                    calendar1.set(Calendar.DAY_OF_MONTH, use_days2[Integer.parseInt(MainActivity.debitCard_list.get(i)[3])]);
                    firstYear = calendar1.get(Calendar.YEAR);
                    firstMonth = calendar1.get(Calendar.MONTH) + 1;
                    firstDay = calendar1.get(Calendar.DAY_OF_MONTH);

                    calendar2.set(Calendar.DAY_OF_MONTH, use_days2[Integer.parseInt(MainActivity.debitCard_list.get(i)[3])] - 1);
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

            Log.d("1818", "sql1 : " + sql1 + ", sql2 : " + sql2);

            int recordCount1 = -1;
            int recordCount2 = -1;
            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);


            if (database != null) {

                Cursor cursor1 = database.rawQuery(sql1);
                Cursor cursor2 = database.rawQuery(sql2);

                recordCount1 = cursor1.getCount();
                AccountDatabase.println("record count1 : " + recordCount1 + "\n");

                for (int a = 0; a < recordCount1; a++){
                    cursor1.moveToNext();

                    int year = cursor1.getInt(0);
                    int month = cursor1.getInt(1);
                    int day = cursor1.getInt(2);
                    String title = cursor1.getString(3);
                    String content = cursor1.getString(4);
                    String content2 = cursor1.getString(5);
                    int money = cursor1.getInt(6);
                    String clear = cursor1.getString(7);

                    if (title.equals(MainActivity.debitCard_list.get(i)[1])){
                        if (clear.equals("결제예정")){
                            return_money = return_money + money;
                        }
                    }

                }

                try{
                    recordCount2 = cursor2.getCount();
                    AccountDatabase.println("record count2 : " + recordCount2 + "\n");

                    for (int b = 0; b < recordCount2; b++){
                        cursor2.moveToNext();

                        int year = cursor2.getInt(0);
                        int month = cursor2.getInt(1);
                        int day = cursor2.getInt(2);
                        String title = cursor2.getString(3);
                        String content = cursor2.getString(4);
                        String content2 = cursor2.getString(5);
                        int money = cursor2.getInt(6);
                        String clear = cursor1.getString(7);

                        if (title.equals(MainActivity.debitCard_list.get(i)[1])){
                            if (clear.equals("결제예정")){
                                return_money = return_money + money;
                            }
                        }
                    }

                } catch (Exception e){ }

            }

            Log.d("1818", "돈 : " + return_money);

        }
        Log.d("1818", "최종 : " + return_money);

        return return_money;

    }
}
