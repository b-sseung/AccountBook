package com.account.book.recyclerview7;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview7.ActivitySubAdapter;

import java.util.ArrayList;

public class ActivitySubAdapter extends RecyclerView.Adapter<ActivitySubAdapter.ViewHolder>{

    ArrayList<ActivitySubItem> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public ActivitySubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_all_title, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ActivitySubAdapter.ViewHolder holder, int position) {
        final ActivitySubItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<ActivitySubItem> items){
        this.items = items;
    }

    public void addItem(ActivitySubItem item){
        items.add(item);
    }

    public ActivitySubItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView text1, text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.sub_text1);
            text2 = itemView.findViewById(R.id.sub_text2);

        }

        public void setItem(ActivitySubItem item){

            if (item.getTitle().equals("현금")){
                text1.setText(item.getTitlename() + "소지액");
            } else if (item.getTitle().equals("계좌")){
                text1.setText(item.getTitlename() + " 계좌 잔액");
            } else if (item.getTitle().equals("신용카드")){
                text1.setText(item.getTitlename() + " 잔여 결제 금액");
            }

            text2.setText(MainActivity.setComma(item.getMoney()));

        }
    }
}
