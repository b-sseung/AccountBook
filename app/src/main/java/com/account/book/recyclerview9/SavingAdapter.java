package com.account.book.recyclerview9;

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
import com.account.book.recyclerview4.ListSubAdapter;

import java.util.ArrayList;

public class SavingAdapter extends RecyclerView.Adapter<SavingAdapter.ViewHolder>{

    ArrayList<SavingItem> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public SavingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_saving, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingAdapter.ViewHolder holder, int position) {
        SavingItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<SavingItem> items){
        this.items = items;
    }

    public void addItem(SavingItem item){
        items.add(item);
    }

    public SavingItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView saving_text1, saving_text2;
        RecyclerView saving_recycler;

        SavingSubAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saving_text1 = itemView.findViewById(R.id.saving_text1);
            saving_text2 = itemView.findViewById(R.id.saving_text2);
            saving_recycler = itemView.findViewById(R.id.saving_recyclerView);

        }

        public void setItem(SavingItem item){
            saving_text1.setText(item.getContent2());
            saving_text2.setText(MainActivity.setComma(item.getMoney()));

            LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);
            saving_recycler.setLayoutManager(manager);
            adapter = new SavingSubAdapter();

            saving_recycler.setAdapter(adapter);

            createSavingSubRecycler(item.getContent2());

        }

        public int createSavingSubRecycler(String text){
            AccountDatabase.println("createSavingSubRecycler called.");

            String sql = "select _id, YEAR, MONTH, DAY, MONEY, WHAT from " + AccountDatabase.TABLE_ACCOUNT
                    + " where CONTENT2 = '" + text + "' order by YEAR desc, MONTH desc, DAY desc";

            int recordCount = -1;
            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

            if (database != null){
                Cursor cursor = database.rawQuery(sql);

                recordCount = cursor.getCount();
                AccountDatabase.println("record count : " + recordCount + "\n");

                ArrayList<SavingSubItem> items = new ArrayList<>();

                for (int i = 0; i < recordCount; i++) {
                    cursor.moveToNext();

                    int _id = cursor.getInt(0);
                    int year = cursor.getInt(1);
                    int month = cursor.getInt(2);
                    int day = cursor.getInt(3);
                    int money = cursor.getInt(4);
                    String what = cursor.getString(5);

                    if (what.equals("수입")){
                        items.add(new SavingSubItem(year, month, day, -money));
                    } else {
                        items.add(new SavingSubItem(year, month, day, money));
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
