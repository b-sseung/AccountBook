package com.account.book.recyclerview6;

import android.content.Context;
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

public class PaymentAdapter2 extends RecyclerView.Adapter<PaymentAdapter2.ViewHolder>{

    ArrayList<PaymentItem> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public PaymentAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_payment, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentAdapter2.ViewHolder holder, int position) {
        final PaymentItem item = items.get(position);
        holder.setItem(item);

        holder.card_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    ActivityPaymentFragment.items_checked2.set(holder.getAdapterPosition(), true);
                } else {
                    ActivityPaymentFragment.items_checked2.set(holder.getAdapterPosition(), false);
                }

            }
        });

        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.card_checkbox.isChecked()){
                    holder.card_checkbox.setChecked(false);
                } else {
                    holder.card_checkbox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<PaymentItem> items){
        this.items = items;
    }

    public void addItem(PaymentItem item){
        items.add(item);
    }

    public PaymentItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox card_checkbox;
        TextView card_titlename_text, card_content_text, card_content2_text, card_money_text, card_month_text, card_day_text;
        LinearLayout card_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_layout = itemView.findViewById(R.id.card_layout);
            card_checkbox = itemView.findViewById(R.id.card_checkbox);
            card_month_text = itemView.findViewById(R.id.card_month_text);
            card_day_text = itemView.findViewById(R.id.card_day_text);
            card_titlename_text = itemView.findViewById(R.id.card_titlename_text);
            card_content_text = itemView.findViewById(R.id.card_content_text);
            card_content2_text = itemView.findViewById(R.id.card_content2_text);
            card_money_text = itemView.findViewById(R.id.card_money_text);

        }

        public void setItem(PaymentItem item){

            card_checkbox.setChecked(item.getCheck());
            card_month_text.setText(String.valueOf(item.getMonth()));

            if (item.getMonth() < 10){
                card_month_text.setText("0" + item.getMonth());
            } else {
                card_month_text.setText(String.valueOf(item.getMonth()));
            }

            if (item.getDay() < 10){
                card_day_text.setText("0" + item.getDay());
            } else {
                card_day_text.setText(String.valueOf(item.getDay()));
            }

            card_titlename_text.setText(item.getTitlename());
            card_content_text.setText(item.getContent());
            card_content2_text.setText(item.getContent2());
            card_money_text.setText(MainActivity.setComma(item.getMoney()));

        }
    }
}
