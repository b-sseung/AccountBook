package com.account.book.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview6.PaymentListAdapter;
import com.account.book.recyclerview6.PaymentListItem;

import java.util.ArrayList;
import java.util.Calendar;

public class Payment_List_Dialog extends AppCompatActivity {

    Button payment_list_dial_close;
    TextView payment_list_dial_title, payment_list_no_text;
    RecyclerView payment_list_recyclerview;

    ArrayList<PaymentListItem> items = new ArrayList<>();
    PaymentListAdapter adapter = new PaymentListAdapter();

    String intent_titlename;
    int intent_year, intent_month, intent_day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment_list);

        payment_list_dial_close = findViewById(R.id.payment_list_dial_close);
        payment_list_dial_title = findViewById(R.id.payment_list_dial_title);
        payment_list_recyclerview = findViewById(R.id.payment_list_recyclerview);
        payment_list_no_text = findViewById(R.id.payment_list_no_text);

        if (getIntent() != null){

            intent_titlename = getIntent().getStringExtra("content2");
            intent_year = getIntent().getIntExtra("year", 0);
            intent_month = getIntent().getIntExtra("month", 0);
            intent_day = getIntent().getIntExtra("day", 0);

            payment_list_dial_title.setText(intent_titlename);

        }

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        payment_list_recyclerview.setLayoutManager(manager);
        payment_list_recyclerview.setAdapter(adapter);

        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        if (createPayDate(intent_titlename, loadTitlenameUse(intent_titlename)) == 0){
            payment_list_no_text.setLayoutParams(params1);
            payment_list_recyclerview.setLayoutParams(params2);
        } else {
            payment_list_no_text.setLayoutParams(params2);
            payment_list_recyclerview.setLayoutParams(params1);
        }


        payment_list_dial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public int loadTitlenameUse(String name){
        AccountDatabase.println("loadTitlenameUse called.");

        String sql1 = "select USE from " + AccountDatabase.TABLE_TITLE
                + " " + "where TITLENAME = '" + name + "' order by _id desc";

        int use = -1;

        int recordCount1 = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null) {

            Cursor cursor1 = database.rawQuery(sql1);

            recordCount1 = cursor1.getCount();
            AccountDatabase.println("record count1 : " + recordCount1 + "\n");

            for (int i = 0; i < recordCount1; i++){
                cursor1.moveToNext();

                use = cursor1.getInt(0);

                return use;
            }

            cursor1.close();
        }

        return use;
    }

    public int createPayDate(String name, int use) {

        int[] use_days2 = new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(intent_year, intent_month - 1, intent_day);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(intent_year, intent_month - 1, intent_day);

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
            sql1 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR, _id from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + firstYear + " and MONTH = " + firstMonth + " and DAY >=" + firstDay
                    + " order by DAY desc";

            sql2 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR, _id from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + lastYear + " and MONTH = " + lastMonth + " and DAY <= " + lastDay
                    + " order by DAY desc";

        } else {
            sql1 = "select YEAR, MONTH, DAY, TITLE, CONTENT, CONTENT2, MONEY, CLEAR, _id from " + AccountDatabase.TABLE_ACCOUNT
                    + " " + "where YEAR = " + firstYear + " and MONTH = " + firstMonth + " and DAY >=" + firstDay
                    + " order by DAY desc";

            sql2 = null;
        }

        int recordCount1 = -1;
        int recordCount2 = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        int return_money = 0;

        if (database != null) {

            Cursor cursor1 = database.rawQuery(sql1);
            Cursor cursor2 = database.rawQuery(sql2);

            recordCount1 = cursor1.getCount();
            AccountDatabase.println("record count1 : " + recordCount1 + "\n");

            items = new ArrayList<>();

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
                int id = cursor1.getInt(8);

                if (title.equals(name)){
                    if (clear.equals("결제예정")){
                        items.add(new PaymentListItem(id, year, month, day, title, content, content2, money, clear));
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
                    String clear = cursor2.getString(7);
                    int id = cursor2.getInt(8);

                    if (title.equals(name)){
                        if (clear.equals("결제예정")){
                            items.add(new PaymentListItem(id, year, month, day, title, content, content2, money, clear));
                        }
                    }
                }

                cursor2.close();

            } catch (Exception e){
                recordCount2 = 0;
            }

            cursor1.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount1 + recordCount2;
    }

}
