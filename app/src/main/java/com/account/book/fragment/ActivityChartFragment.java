package com.account.book.fragment;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.recyclerview3.ChartAdapter;
import com.account.book.recyclerview3.ChartItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityChartFragment extends Fragment {

    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;
    TextView chart_toptext1, chart_toptext2, chart_prev_text, chart_next_text, chart_text_year,
            chart_text_month, chart_text_1, chart_text_2, chart_text_3, chart_text_4, no_chart_text, no_chart_text2;

    LinearLayout layout1, layout2;
    BarChart barChart;

    Calendar calendar = Calendar.getInstance();
    int today_year, today_month, data_year, data_month, data_year2, data_month2;
    String data_what, data_what2;

    ArrayList<String> pie_datas = new ArrayList<>();
    ArrayList<Integer> pie_moneys = new ArrayList<>();
    ArrayList pie_setData = new ArrayList();
    ArrayList<String> bar_labels = new ArrayList<>();
    ArrayList<Integer> bar_moneys = new ArrayList<>();
    ArrayList bar_setData = new ArrayList();

    RecyclerView recyclerView;
    ChartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chart_fragment, container, false);

        pieChart = rootView.findViewById(R.id.piechart);
        barChart = rootView.findViewById(R.id.barchart);
        chart_toptext1 = rootView.findViewById(R.id.chart_toptext1);
        chart_toptext2 = rootView.findViewById(R.id.chart_toptext2);
        chart_prev_text = rootView.findViewById(R.id.chart_prev_text);
        chart_next_text = rootView.findViewById(R.id.chart_next_text);
        chart_text_year = rootView.findViewById(R.id.chart_text_year);
        chart_text_month = rootView.findViewById(R.id.chart_text_month);
        chart_text_1 = rootView.findViewById(R.id.chart_text1);
        chart_text_2 = rootView.findViewById(R.id.chart_text2);
        chart_text_3 = rootView.findViewById(R.id.chart_text3);
        chart_text_4 = rootView.findViewById(R.id.chart_text4);
        no_chart_text = rootView.findViewById(R.id.no_chart_text);
        no_chart_text2 = rootView.findViewById(R.id.no_chart_text2);
        recyclerView = rootView.findViewById(R.id.chart_recycler);
        layout1 = rootView.findViewById(R.id.chart_layout1);
        layout2 = rootView.findViewById(R.id.chart_layout2);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new ChartAdapter();

        recyclerView.setAdapter(adapter);

        setFirstTextSetting(1, chart_toptext1);
        setFirstTextSetting(2, chart_toptext2);
        setSecondTextSetting(1, chart_text_1);
        setSecondTextSetting(2, chart_text_2);
        setSecondTextSetting(1, chart_text_3);
        setSecondTextSetting(2, chart_text_4);

        if (getArguments() != null){
            today_year = getArguments().getInt("today_year");
            today_month = getArguments().getInt("today_month") + 1;
            data_year = today_year;
            data_month = today_month;

            chart_text_year.setText(today_year + "년");
            chart_text_month.setText(today_month + "월");

            calendar.set(Calendar.YEAR, today_year);
            calendar.set(Calendar.MONTH, today_month - 1);
        }

        pieChart.setUsePercentValues(true); //true면 차트 내 값이 백분율로 그려짐
        pieChart.getDescription().setEnabled(false);  //차트 오른쪽 하단에 표시되는 설명 텍스트 안보이게 설정
        pieChart.setExtraOffsets(20,20,20,20);
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawEntryLabels(true);  //파이조각에 항목레이블을 넣을것인지
        pieChart.setDrawHoleEnabled(true);   //가운데 원 넣을지 말지
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD); //항목레이블 글씨 굵게
        pieChart.setEntryLabelColor(Color.parseColor("#000000"));
        pieChart.setEntryLabelTextSize(15);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.getLegend().setEnabled(false);  //범례 비활성화
        pieChart.animateXY(500, 500); //애니메이션
        pieChart.highlightValue(null);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleYEnabled(false); //Y로 확대 못하게
        barChart.setScaleXEnabled(false);  //X로 확대 못하게
        barChart.getXAxis().setDrawGridLines(false); //세로 격자 없앰
        barChart.getAxisLeft().setDrawGridLines(false);  //가로 격자 없앰
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(500);

        setChartData("수입");
        data_what = "수입";
        data_what2 = "수입";
        setContentRecycler();
        createBarChart();

        chart_toptext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirstTextSetting(1, chart_toptext1);
                setFirstTextSetting(2, chart_toptext2);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.INVISIBLE);

                setContentRecycler();
                pieChart.animateXY(500, 500); //애니메이션
                pieChart.invalidate();
            }
        });

        chart_toptext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirstTextSetting(2, chart_toptext1);
                setFirstTextSetting(1, chart_toptext2);

                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);

                loadBarRecyclerData(data_what2, data_year2, data_month2);
                barChart.animateY(500);
                barChart.invalidate();
            }
        });

        chart_prev_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);

                data_year = calendar.get(Calendar.YEAR);
                data_month = calendar.get(Calendar.MONTH) + 1;

                chart_text_year.setText(data_year + "년");
                chart_text_month.setText(data_month + "월");

                setChartData(data_what);
            }
        });

        chart_next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);

                data_year = calendar.get(Calendar.YEAR);
                data_month = calendar.get(Calendar.MONTH) + 1;

                chart_text_year.setText(data_year + "년");
                chart_text_month.setText(data_month + "월");

                setChartData(data_what);
            }
        });

        chart_text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondTextSetting(1, chart_text_1);
                setSecondTextSetting(2, chart_text_2);

                data_what = "수입";
                setChartData(data_what);
            }
        });

        chart_text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondTextSetting(2, chart_text_1);
                setSecondTextSetting(1, chart_text_2);

                data_what = "지출";
                setChartData(data_what);
            }
        });

        chart_text_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondTextSetting(1, chart_text_3);
                setSecondTextSetting(2, chart_text_4);

                data_what2 = "수입";
                createBarChart();
                loadBarRecyclerData(data_what2, data_year2, data_month2);

            }
        });

        chart_text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondTextSetting(2, chart_text_3);
                setSecondTextSetting(1, chart_text_4);

                data_what2 = "지출";
                createBarChart();
                loadBarRecyclerData(data_what2, data_year2, data_month2);

            }
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                calendar.set(Calendar.YEAR, data_year2);
                calendar.set(Calendar.MONTH, data_month2 - 1);

                calendar.add(Calendar.MONTH, (int) (-6 + e.getX()));

                if (e.getY() != 0){
                    if (no_chart_text2.getHeight() != 0){
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                        no_chart_text2.setLayoutParams(params);
                    }
                    loadBarRecyclerData(data_what2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                } else {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    no_chart_text2.setLayoutParams(params);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return rootView;
    }

    public void pie_settingPieDataSet(PieDataSet dataSet){
        dataSet.setSliceSpace(1f); //사이사이 간격
        dataSet.setSelectionShift(5f);  //이 DataSet의 강조 표시된 원형 차트 조각이있는 거리를 설정합니다.
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueLinePart1Length(0.4f);  //밖으로 뺄 때 막대 길이
        dataSet.setValueLinePart2Length(0.4f);  //밖으로 뺄 때 막대 길이
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); //항목숫자값 밖으로 빼기
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); //항목이름값 밖으로 빼기
    }

    public void pie_settingData(PieData data){
        //글자 설정 속성
        data.setValueTextSize(15);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());  //값을 %퍼센트로 표시
    }

    public void setFirstTextSetting(int num, TextView view){  //월별, 6개월간 추이 부분
        if (num == 1){
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.0f);
            view.setTextColor(Color.parseColor("#000000"));
//            view.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//            view.setTypeface(null, Typeface.BOLD);
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (num == 2){
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.0f);
            view.setTextColor(Color.parseColor("#88000000"));
//            view.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//            view.setTypeface(null, Typeface.BOLD);
            view.setBackgroundColor(Color.parseColor("#E2E2E2"));
        }
    }

    public void setSecondTextSetting(int num, TextView view){   //수입, 지출 부분
        if (num == 1){
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.0f);
            view.setTextColor(Color.parseColor("#FF0000"));
//            view.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (num == 2){
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.0f);
            view.setTextColor(Color.parseColor("#000000"));
//            view.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//            view.setBackgroundColor(Color.parseColor("#E2E2E2"));
        }
    }

    public void setChartData(String what){
        loadChartData(what, data_year, data_month);

        pie_setData = new ArrayList();
        for (int i = 0; i < pie_datas.size(); i++){
            pie_setData.add(new PieEntry(pie_moneys.get(i), pie_datas.get(i)));
        }

        if (pie_setData.size() != 0){

            if (no_chart_text.getHeight() != 0){
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                no_chart_text.setLayoutParams(params);
            }

            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pieChart.setLayoutParams(params2);

            dataSet = new PieDataSet(pie_setData, "name");
            data = new PieData((dataSet));

            pie_settingPieDataSet(dataSet);
            pie_settingData(data);

            pieChart.setData(data);
            pieChart.animateXY(500, 500);
            pieChart.invalidate();

            setContentRecycler();

        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
            no_chart_text.setLayoutParams(params);
            pieChart.setLayoutParams(params2);
            setContentRecycler();
        }
    }

    public int loadChartData(String data_what, int data_year, int data_month){

        AccountDatabase.println("loadChartData called.");

        String sql = "select _id, YEAR, MONTH, CONTENT, MONEY from " + AccountDatabase.TABLE_ACCOUNT +
                " where YEAR = '" + data_year + "' and MONTH = '" + data_month + "' and WHAT = '" + data_what
                + "' order by CONTENT desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            pie_datas = new ArrayList<>();
            pie_moneys = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                String content = cursor.getString(3);
                int money = cursor.getInt(4);

                int checkname = 1;
                int checknum = -1;
                for (int a = 0; a < pie_datas.size(); a++){
                    if (pie_datas.get(a).equals(content)){
                        checkname = 2;
                        checknum = a;
                    }
                }

                if (checkname == 1){
                    pie_datas.add(content);
                    pie_moneys.add(money);
                } else if (checkname == 2){
                    int chart_money = pie_moneys.get(checknum) + money;
                    pie_moneys.set(checknum, chart_money);
                }

            }
            cursor.close();
        }
        return recordCount;
    }

    public void setContentRecycler(){
        ArrayList<ChartItem> items = new ArrayList<>();

        for (int i = 0; i < pie_datas.size(); i++){
            items.add(new ChartItem(pie_datas.get(i), pie_moneys.get(i)));
        }

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    public int loadMonthData(String data_what, int year, int month){

        AccountDatabase.println("loadMonthData called.");

        String sql = "select _id, MONEY from " + AccountDatabase.TABLE_ACCOUNT +
                " where YEAR = '" + year + "' and MONTH = '" + month + "' and WHAT = '" + data_what
                + "' order by CONTENT desc";

        int recordCount = -1;
        int all_money = 0;

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");



            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int money = cursor.getInt(1);

                all_money = all_money + money;

            }
            cursor.close();
        }
        return all_money;
    }

    public void createBarChart(){

        ArrayList<String> names = new ArrayList<>();
        bar_labels = new ArrayList<>();
        bar_moneys = new ArrayList<>();
        bar_setData = new ArrayList<>();


        for (int i = 5; i >= 0; i--){
            calendar.set(Calendar.YEAR, today_year);
            calendar.set(Calendar.MONTH, today_month - 1);

            calendar.add(Calendar.MONTH, -i);

            data_year2 = calendar.get(Calendar.YEAR);
            data_month2 = calendar.get(Calendar.MONTH) + 1;

            bar_moneys.add(loadMonthData(data_what2, data_year2, data_month2));

            names.add((data_year2-2000) + "년 " + (data_month2) + "월");
        }

        for (int a = 0; a < bar_moneys.size(); a++){
            bar_labels.add("");
            for(int b = 0; b < names.size(); b++){
                bar_labels.add(names.get(b));
            }
        }

        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return bar_labels.get((int) value); }
        });

        for (int c = 1; c <= 6; c++){
            bar_setData.add(new BarEntry(c, bar_moneys.get(c - 1)));
        }
        BarDataSet set = new BarDataSet(bar_setData, "");
        set.setDrawValues(true);
        BarData barData = new BarData(set);
        barChart.setData(barData);
        barChart.animateY(500);
        barChart.invalidate();

    }

    public int loadBarRecyclerData(String data_what, int data_year, int data_month) {

        AccountDatabase.println("loadBarRecyclerData called.");

        String sql = "select _id, CONTENT, MONEY from " + AccountDatabase.TABLE_ACCOUNT +
                " where YEAR = '" + data_year + "' and MONTH = '" + data_month + "' and WHAT = '" + data_what
                + "' order by CONTENT desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null) {
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            ArrayList<String> items_name = new ArrayList<>();
            ArrayList<Integer> items_money = new ArrayList<>();
            ArrayList<ChartItem> items = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String content = cursor.getString(1);
                int money = cursor.getInt(2);

                int checkname = 1;
                int checknum = -1;
                for (int a = 0; a < items_name.size(); a++) {
                    if (items_name.get(a).equals(content)) {
                        checkname = 2;
                        checknum = a;
                    }
                }

                if (checkname == 1) {
                    items_name.add(content);
                    items_money.add(money);
                } else if (checkname == 2) {
                    int chart_money = items_money.get(checknum) + money;
                    items_money.set(checknum, chart_money);
                }

            }
            cursor.close();

            for (int i = 0; i < items_name.size(); i++) {
                items.add(new ChartItem(items_name.get(i), items_money.get(i)));
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
        return recordCount;
    }
}
