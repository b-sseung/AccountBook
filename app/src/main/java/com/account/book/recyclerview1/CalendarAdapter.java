package com.account.book.recyclerview1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    private ArrayList<DayItem> items = new ArrayList<>();
    private Context mContext;
    private Calendar calendar = Calendar.getInstance();

    public static ArrayList<ViewHolder> holders = new ArrayList<>();
    ArrayList<Integer> card_days = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mContext = parent.getContext();

        View itemView = inflater.inflate(R.layout.item_calendar, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        DayItem item = items.get(position);
        holder.setItem(item);

        holders.add(holder);

        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(getActivityWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

        if (item.getDayNum() == 1){
            holder.item_day.setTextColor(Color.parseColor("#FF0000"));
        }

        if (item.getDayNum() == 7){
            holder.item_day.setTextColor(Color.parseColor("#0000FF"));
        }

        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int itemday = 0;
        if (!item.getDay().equals("")){
            itemday = Integer.valueOf(item.getDay());
        }

        if (today == itemday){
            holder.itemView.setBackgroundResource(R.drawable.background_image_today_ver2);

            MainActivity.click_date_text.setText(item.getYear() + "년 "
                    + item.getMonth() + "월 "
                    + item.getDay() + "일");
        } else {
            holder.itemView.setBackgroundResource(R.drawable.background_image_ver2);
        }

        Log.d("1818", String.valueOf(card_days.size()));

        for (int i = 0; i < card_days.size(); i++){
            Log.d("1818", item.getDay() + ", " + card_days.get(i));
            if (item.getDay().equals(card_days.get(i).toString())){
                holder.item_day.setTextColor(Color.parseColor("#00FF00"));

                Log.d("1818", "해당 있음");
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayItem item = items.get(holder.getAdapterPosition());
                if (!item.getDay().equals("")){
                    ClickMethod(holder.getAdapterPosition());
                    MainActivity.click_date_text.setText(item.getYear() + "년 " + item.getMonth() + "월 " + item.getDay() + "일");
                    MainActivity.data_year = item.getYear();
                    MainActivity.data_month = item.getMonth();
                    MainActivity.data_day = Integer.valueOf(item.getDay());

                    MainActivity.loadAccountViewData();

                }
            }
        });
    }

    public void setCardColor(ArrayList<Integer> list){
        this.card_days = list;
        notifyDataSetChanged();
    }

    public void ClickMethod(int position){

        for (int i = 0; i < holders.size(); i++){
            Log.d("1818", position + ", " + i);
            if (holders.get(i).getAdapterPosition() == position){
                holders.get(i).itemView.setBackgroundResource(R.drawable.background_image_today_ver2);
            } else {
                holders.get(i).itemView.setBackgroundResource(R.drawable.background_image_ver2);
            }
        }

//        for (int a = 0; a < items.size(); a++){
//            String itemday = String.valueOf(items.get(a).getYear()) + String.valueOf(items.get(a).getMonth()) + items.get(a).getDay();
//
//            if (today.equals(itemday)){
//                holders.get(a).itemView.setBackgroundResource(R.drawable.background_image_today_ver1);
//            }
//        }

    }

    public int getActivityWidth(){
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        return width/7;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<DayItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(DayItem item){
        items.add(item);
    }

    public DayItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, DayItem item){
        items.set(position, item);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_day, item_plus, item_minus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_day = itemView.findViewById(R.id.item_day);
            item_plus = itemView.findViewById(R.id.item_plus);
            item_minus = itemView.findViewById(R.id.item_minus);

        }

        public void setItem(DayItem item){

            if (!item.getDay().equals("")){
                if (Integer.valueOf(item.getDay()) < 10){
                    item_day.setText("0" + item.getDay());
                } else {
                    item_day.setText(item.getDay());
                }
                if (item.getPlus() == 0){
                    item_plus.setText("");
                } else {
                    item_plus.setText("+" + MainActivity.setComma(item.getPlus()));
                    item_plus.setBackgroundColor(Color.parseColor("#88FF0000"));
                    item_plus.setTextColor(Color.parseColor("#FFFFFF"));
                }

                if (item.getMinus() == 0){
                    item_minus.setText("");
                } else {
                    item_minus.setText("-" + MainActivity.setComma(item.getMinus()));
                    item_minus.setBackgroundColor(Color.parseColor("#880000FF"));
                    item_minus.setTextColor(Color.parseColor("#FFFFFF"));
                }
            } else {
                item_day.setText(item.getDay());
                item_plus.setText("");
                item_minus.setText("");
            }

        }
    }

}
