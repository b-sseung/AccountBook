package com.account.book.recyclerview3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.MainActivity;
import com.account.book.R;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder>{

    ArrayList<ChartItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_chart, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChartItem item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<ChartItem> items){
        this.items = items;
    }

    public void addItem(ChartItem item){
        items.add(item);
    }

    public ChartItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView chart_item_text1, chart_item_text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chart_item_text1 = itemView.findViewById(R.id.chart_item_text1);
            chart_item_text2 = itemView.findViewById(R.id.chart_item_text2);

        }

        public void setItem(ChartItem item){
            chart_item_text1.setText(item.getChartcontent());
            chart_item_text2.setText(MainActivity.setComma(item.getChartmoney()));
        }
    }
}
