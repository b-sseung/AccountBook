package com.account.book.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview7.ActivitySubAdapter;
import com.account.book.recyclerview7.ActivitySubItem;

import java.util.ArrayList;
import java.util.Date;

public class ActivitySubFragment extends Fragment {

    TextView activity_text_1, activity_text_2, activity_text_3, activity_text_4, activity_text_5, activity_month_1, activity_month_2, activity_month_3;

    int main_year, main_month, text1_money;

    ArrayList<String> titlenames = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Integer> moneys = new ArrayList<>();

    RecyclerView sub_recyclerview;
    ActivitySubAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_activity_sub, container, false);

        activity_text_1 = rootView.findViewById(R.id.activity_text_1);
        activity_text_2 = rootView.findViewById(R.id.activity_text_2);
        activity_text_3 = rootView.findViewById(R.id.activity_text_3);
        activity_text_4 = rootView.findViewById(R.id.activity_text_4);
        activity_text_5 = rootView.findViewById(R.id.activity_text_5);
        sub_recyclerview = rootView.findViewById(R.id.sub_recyclerview);
        activity_month_1 = rootView.findViewById(R.id.activity_month_1);
        activity_month_2 = rootView.findViewById(R.id.activity_month_2);
        activity_month_3 = rootView.findViewById(R.id.activity_month_3);

        if (getArguments() != null){
            main_year = getArguments().getInt("YEAR");
            main_month = getArguments().getInt("MONTH");

            activity_month_1.setText(String.valueOf(main_month - 1));
            activity_month_2.setText(String.valueOf(main_month));
            activity_month_3.setText(String.valueOf(main_month));
        }

        Log.d("1818", "1, " + main_year + ", " + main_month);


        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        sub_recyclerview.setLayoutManager(manager);
        adapter = new ActivitySubAdapter();
        sub_recyclerview.setAdapter(adapter);

        loadPreMoney();
        loadMoneyData();
        loadTitleTitlename();

        return rootView;
    }

    public int loadMoneyData(){

        AccountDatabase.println("loadMoneyData called.");

        String sql = "select _id, YEAR, MONTH, WHAT, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                + " where YEAR=" + main_year + " and MONTH=" + main_month + " order by DAY desc";

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
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                String what = cursor.getString(3);
                int money = cursor.getInt(4);
                String clear = cursor.getString(5);

                if (what.equals("수입")){
                    plus_money = plus_money + money;
                } else if (what.equals("지출")){
                    minus_money = minus_money + money;
                }

            }

            activity_text_2.setText(MainActivity.setComma(plus_money));
            activity_text_3.setText(MainActivity.setComma(minus_money));
            activity_text_4.setText(MainActivity.setComma(plus_money - minus_money + text1_money));

            cursor.close();
        }

        return recordCount;
    }

    public int loadPreMoney(){

        AccountDatabase.println("loadPreMoney called.");

        Date date = MainActivity.returnDate(main_year, main_month);

        String sql = "select _id, YEAR, MONTH, WHAT, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                + " order by _id desc";

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
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                String what = cursor.getString(3);
                int money = cursor.getInt(4);
                String clear = cursor.getString(5);

                Date date2 = MainActivity.returnDate(year, month);
                int date_value = date.compareTo(date2);

                if (date_value > 0){
                    if (what.equals("수입")){
                        plus_money = plus_money + money;
                    } else if (what.equals("지출")) {
                        minus_money = minus_money + money;
                    }
                }

            }

            text1_money = plus_money - minus_money;
            activity_text_1.setText(MainActivity.setComma(text1_money));

            cursor.close();
        }

        return recordCount;
    }

    public void loadTitleTitlename(){
        AccountDatabase.println("loadTitleTitlename called.");

        String sql = "select _id, TITLE, TITLENAME from " + AccountDatabase.TABLE_TITLE
                + " where WHAT = '" + "지출" + "' and CONTENT = '" + "" + "' order by TITLE asc" + ", _id asc";

        ArrayList<ActivitySubItem> items = new ArrayList<>();

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null) {
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int a = 0; a < recordCount; a++) {
                cursor.moveToNext();

                String title = cursor.getString(1);
                String titlename = cursor.getString(2);
                int money = loadTitleMoney(title, titlename);

                items.add(new ActivitySubItem(title, titlename, money));
            }

            cursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    }

    public int loadTitleMoney(String title, String titlename){

        AccountDatabase.println("loadTitleMoney2 called.");

        Date date = MainActivity.returnDate(main_year, main_month);
        Log.d("1818", "2, " + titlename + main_year + ", " + main_month);

        String sql = "select _id, YEAR, MONTH, WHAT, TITLE, MONEY, CLEAR from " + AccountDatabase.TABLE_ACCOUNT
                + " where TITLE = '" + titlename + "' order by _id desc";

        int all_money = 0;

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                String what = cursor.getString(3);
                String title2 = cursor.getString(4);
                int money = cursor.getInt(5);
                String clear = cursor.getString(6);

                Date date2 = MainActivity.returnDate(year, month);
                Log.d("1818", "3, " + year + ", 2 , " + month);

                int date_value = date.compareTo(date2);

                if (title.equals("신용카드")){
                    if (clear.equals("결제예정")) {
                        if (date_value >= 0){
                            if (what.equals("수입")){
                                all_money = all_money + money;
                            } else if (what.equals("지출")){
                                all_money = all_money - money;
                            } else if (what.equals("")){
                                if (title.equals("계좌")){
                                    all_money = all_money - money;
                                }
                            }
                        }
                    }
                } else {
                    if (date_value >= 0){
                        if (what.equals("수입")){
                            all_money = all_money + money;
                        } else if (what.equals("지출")){
                            all_money = all_money - money;
                        } else if (what.equals("")){
                            if (title.equals("계좌")){
                                all_money = all_money - money;
                            }
                        }
                    }
                }


                Log.d("1818", "4, " + money + ", " + all_money);

            }

            cursor.close();
        }

        return all_money;

    }
}
