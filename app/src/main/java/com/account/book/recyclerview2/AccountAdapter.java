package com.account.book.recyclerview2;

import android.content.Context;
import android.content.Intent;
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
import com.account.book.dialog.Modify_Account_Dialog;
import com.account.book.dialog.Payment_List_Dialog;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>{

    ArrayList<AccountItem> items2 = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_account, parent, false);

        mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AccountItem item = items2.get(position);
        holder.setItem(item);

        holder.account_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountItem get_item = items2.get(holder.getAdapterPosition());

                if (get_item.getTitle().equals("결제예정")){
                    Intent intent = new Intent(mContext, Payment_List_Dialog.class);
                    intent.putExtra("year", MainActivity.data_year);
                    intent.putExtra("month", MainActivity.data_month);
                    intent.putExtra("day", MainActivity.data_day);
                    intent.putExtra("what", get_item.getWhat());
                    intent.putExtra("titlename", get_item.getTitle());
                    intent.putExtra("content", get_item.getContent());
                    intent.putExtra("content2", get_item.getContent2());
                    intent.putExtra("money", get_item.getMoney());

                    ((MainActivity)mContext).startActivityForResult(intent, MainActivity.REQUEST_INTENT_10);
                } else {
                    Intent intent = new Intent(mContext, Modify_Account_Dialog.class);
                    intent.putExtra("year", MainActivity.data_year);
                    intent.putExtra("month", MainActivity.data_month);
                    intent.putExtra("day", MainActivity.data_day);
                    intent.putExtra("what", get_item.getWhat());
                    intent.putExtra("titlename", get_item.getTitle());
                    intent.putExtra("content", get_item.getContent());
                    intent.putExtra("content2", get_item.getContent2());
                    intent.putExtra("money", get_item.getMoney());

                    ((MainActivity)mContext).startActivityForResult(intent, MainActivity.REQUEST_INTENT_3);
                }


            }
        });

    }

    public int getActivityWidth(){
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        return width/7;
    }

    @Override
    public int getItemCount() {
        return items2.size();
    }

    public void setItems(ArrayList<AccountItem> items2){
        this.items2 = items2;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView what_text, payment_text, content_text, content2_text, money_text;
        LinearLayout account_item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            what_text = itemView.findViewById(R.id.what_text);
            payment_text = itemView.findViewById(R.id.payment_text);
            content_text = itemView.findViewById(R.id.content_text);
            content2_text = itemView.findViewById(R.id.content2_text);
            money_text = itemView.findViewById(R.id.money_text);

            account_item_layout = itemView.findViewById(R.id.account_item_layout);
        }

        public void setItem(AccountItem item){
            what_text.setText(item.getWhat());
            payment_text.setText(item.getTitle());
            content_text.setText(item.getContent());
            content2_text.setText(item.getContent2());
            money_text.setText(MainActivity.setComma(item.getMoney()) + "원");

            if (item.getWhat().equals("수입")){
                what_text.setTextColor(Color.parseColor("#FF0000"));
            } else if (item.getWhat().equals("지출")){
                what_text.setTextColor(Color.parseColor("#0000FF"));
            }
        }
    }
}
