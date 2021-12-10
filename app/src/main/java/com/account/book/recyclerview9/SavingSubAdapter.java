package com.account.book.recyclerview9;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.MainActivity;
import com.account.book.R;

import java.util.ArrayList;

public class SavingSubAdapter extends RecyclerView.Adapter<SavingSubAdapter.ViewHolder>{

    ArrayList<SavingSubItem> items = new ArrayList<>();

    @NonNull
    @Override
    public SavingSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_saving_sub, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingSubAdapter.ViewHolder holder, int position) {
        SavingSubItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<SavingSubItem> items){
        this.items = items;
    }

    public void addItem(SavingSubItem item){
        items.add(item);
    }

    public SavingSubItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView saving_sub_year, saving_sub_month, saving_sub_day, saving_sub_money;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            saving_sub_year = itemView.findViewById(R.id.saving_sub_year);
            saving_sub_month = itemView.findViewById(R.id.saving_sub_month);
            saving_sub_day = itemView.findViewById(R.id.saving_sub_day);
            saving_sub_money = itemView.findViewById(R.id.saving_sub_money);
        }

        public void setItem(SavingSubItem item){
            saving_sub_year.setText(String.valueOf(item.getYear()));
            saving_sub_month.setText(String.valueOf(item.getMonth()));
            saving_sub_day.setText(String.valueOf(item.getDay()));
            saving_sub_money.setText(MainActivity.setComma(item.getMoney()));
        }
    }
}
