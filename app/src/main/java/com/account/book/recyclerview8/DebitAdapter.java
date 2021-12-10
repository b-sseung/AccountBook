package com.account.book.recyclerview8;

import android.content.Context;
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

public class DebitAdapter extends RecyclerView.Adapter<DebitAdapter.ViewHolder>{

    ArrayList<DebitItem> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public DebitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_debit_list, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DebitAdapter.ViewHolder holder, int position) {
        final DebitItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<DebitItem> items){
        this.items = items;
    }

    public void addItem(DebitItem item){
        items.add(item);
    }

    public DebitItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView debit_titlename_text, debit_content_text, debit_content2_text, debit_money_text,
                debit_year_text, debit_month_text, debit_day_text, debit_clear_text;
        LinearLayout debit_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            debit_layout = itemView.findViewById(R.id.debit_layout);
            debit_year_text = itemView.findViewById(R.id.debit_year_text);
            debit_month_text = itemView.findViewById(R.id.debit_month_text);
            debit_day_text = itemView.findViewById(R.id.debit_day_text);
            debit_titlename_text = itemView.findViewById(R.id.debit_titlename_text);
            debit_content_text = itemView.findViewById(R.id.debit_content_text);
            debit_content2_text = itemView.findViewById(R.id.debit_content2_text);
            debit_money_text = itemView.findViewById(R.id.debit_money_text);
            debit_clear_text = itemView.findViewById(R.id.debit_clear_text);

        }

        public void setItem(DebitItem item){


            debit_year_text.setText(String.valueOf(item.getYear()));
            debit_month_text.setText(String.valueOf(item.getMonth()));

            if (item.getMonth() < 10){
                debit_month_text.setText("0" + item.getMonth());
            } else {
                debit_month_text.setText(String.valueOf(item.getMonth()));
            }

            if (item.getDay() < 10){
                debit_day_text.setText("0" + item.getDay());
            } else {
                debit_day_text.setText(String.valueOf(item.getDay()));
            }

            debit_titlename_text.setText(item.getTitlename());
            debit_content_text.setText(item.getContent());
            debit_content2_text.setText(item.getContent2());
            debit_money_text.setText(MainActivity.setComma(item.getMoney()));
            debit_clear_text.setText(item.getClear());

        }
    }
}