package com.account.book.recyclerview4;

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
import com.account.book.recyclerview2.AccountItem;

import java.util.ArrayList;

public class ListSubAdapter extends RecyclerView.Adapter<ListSubAdapter.ViewHolder>{

    ArrayList<AccountItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ListSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_list_date_sub, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSubAdapter.ViewHolder holder, int position) {
        AccountItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<AccountItem> items){
        this.items = items;
    }

    public void addItem(AccountItem item){
        items.add(item);
    }

    public AccountItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView sub_list_what_text, sub_list_payment_text, sub_list_content_text, sub_list_content2_text, sub_list_money_text;
        LinearLayout sub_list_item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_list_what_text = itemView.findViewById(R.id.sub_list_what_text);
            sub_list_payment_text = itemView.findViewById(R.id.sub_list_payment_text);
            sub_list_content_text = itemView.findViewById(R.id.sub_list_content_text);
            sub_list_content2_text = itemView.findViewById(R.id.sub_list_content2_text);
            sub_list_money_text = itemView.findViewById(R.id.sub_list_money_text);

            sub_list_item_layout = itemView.findViewById(R.id.sub_list_item_layout);
        }

        public void setItem(AccountItem item){
            if (item.getWhat() != null){
                sub_list_what_text.setText(item.getWhat());
                sub_list_payment_text.setText(item.getTitle());
                sub_list_content_text.setText(item.getContent());
                sub_list_content2_text.setText(item.getContent2());
                sub_list_money_text.setText(MainActivity.setComma(item.getMoney()) + "원");

                if (item.getWhat().equals("수입")){
                    sub_list_what_text.setTextColor(Color.parseColor("#FF0000"));
                } else if (item.getWhat().equals("지출")){
                    sub_list_what_text.setTextColor(Color.parseColor("#0000FF"));
                }
            }

        }
    }
}
