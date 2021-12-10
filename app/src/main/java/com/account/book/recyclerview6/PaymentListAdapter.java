package com.account.book.recyclerview6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.fragment.ActivityPaymentFragment;

import java.util.ArrayList;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder>{

    ArrayList<PaymentListItem> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public PaymentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_payment_list, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentListAdapter.ViewHolder holder, int position) {
        final PaymentListItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<PaymentListItem> items){
        this.items = items;
    }

    public void addItem(PaymentListItem item){
        items.add(item);
    }

    public PaymentListItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView payment_list_titlename_text, payment_list_content_text, payment_list_content2_text, payment_list_money_text, payment_list_month_text, payment_list_day_text;
        LinearLayout payment_list_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            payment_list_layout = itemView.findViewById(R.id.payment_list_layout);
            payment_list_month_text = itemView.findViewById(R.id.payment_list_month_text);
            payment_list_day_text = itemView.findViewById(R.id.payment_list_day_text);
            payment_list_titlename_text = itemView.findViewById(R.id.payment_list_titlename_text);
            payment_list_content_text = itemView.findViewById(R.id.payment_list_content_text);
            payment_list_content2_text = itemView.findViewById(R.id.payment_list_content2_text);
            payment_list_money_text = itemView.findViewById(R.id.payment_list_money_text);

        }

        public void setItem(PaymentListItem item){

            payment_list_month_text.setText(String.valueOf(item.getMonth()));

            if (item.getMonth() < 10){
                payment_list_month_text.setText("0" + item.getMonth());
            } else {
                payment_list_month_text.setText(String.valueOf(item.getMonth()));
            }

            if (item.getDay() < 10){
                payment_list_day_text.setText("0" + item.getDay());
            } else {
                payment_list_day_text.setText(String.valueOf(item.getDay()));
            }

            payment_list_titlename_text.setText(item.getTitlename());
            payment_list_content_text.setText(item.getContent());
            payment_list_content2_text.setText(item.getContent2());
            payment_list_money_text.setText(MainActivity.setComma(item.getMoney()));

        }
    }
}
