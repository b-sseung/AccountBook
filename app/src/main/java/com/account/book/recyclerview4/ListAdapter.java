package com.account.book.recyclerview4;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview2.AccountItem;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

        ArrayList<ListItem> items = new ArrayList<>();
        Context mContext;

        @NonNull
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View rootView = (ViewGroup) inflater.inflate(R.layout.item_list_date, parent, false);

            this.mContext = parent.getContext();

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
            ListItem item = items.get(position);
            holder.setItem(item);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(ArrayList<ListItem> items){
            this.items = items;
        }

        public void addItem(ListItem item){
            items.add(item);
        }

        public ListItem getItem(int position){
            return items.get(position);
        }



        class ViewHolder extends RecyclerView.ViewHolder{

            TextView list_date_text;
            RecyclerView list_date_recycler;

            ListSubAdapter adapter;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                list_date_text = itemView.findViewById(R.id.listDate_text);
                list_date_recycler = itemView.findViewById(R.id.listDate_recyclerView);



            }

            public void setItem(ListItem item){
                list_date_text.setText(item.getDay() + "일");

                LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);
                list_date_recycler.setLayoutManager(manager);
                adapter = new ListSubAdapter();

                list_date_recycler.setAdapter(adapter);

                createSubRecycler(item.getYear(), item.getMonth(), item.getDay());

            }

            public int createSubRecycler(int year, int month, int day){
                AccountDatabase.println("createSubRecycler called.");

                String sql = "select _id, WHAT, TITLE, CONTENT, CONTENT2, MONEY from " + AccountDatabase.TABLE_ACCOUNT
                        + " " + "where YEAR =" + year + " and MONTH =" + month + " and DAY =" + day + " order by _id desc";

                int recordCount = -1;
                AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

                if (database != null){
                    Cursor cursor = database.rawQuery(sql);

                    recordCount = cursor.getCount();
                    AccountDatabase.println("record count : " + recordCount + "\n");

                    ArrayList<AccountItem> items = new ArrayList<>();

                    for (int i = 0; i < recordCount; i++) {
                        cursor.moveToNext();

                        int _id = cursor.getInt(0);
                        String what = cursor.getString(1);
                        String title = cursor.getString(2);
                        String content = cursor.getString(3);
                        String content2 = cursor.getString(4);
                        int money = cursor.getInt(5);

                        if (!content.equals("선결제")){
                            items.add(new AccountItem(what, title, content, content2, money));
                        }
                    }

                    cursor.close();
                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
                }

                return recordCount;
            }
        }
}
