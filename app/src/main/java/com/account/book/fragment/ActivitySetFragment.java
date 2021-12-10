package com.account.book.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.dialog.Add_Content_Dialog;
import com.account.book.dialog.Add_Title_Dialog;
import com.account.book.dialog.Set_DeleteData_Dialog;
import com.account.book.recyclerview5.SettingAdapter;
import com.account.book.recyclerview5.SettingItem;
import com.account.book.recyclerview9.SavingAdapter;
import com.account.book.recyclerview9.SavingItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class ActivitySetFragment extends Fragment {

    public static int num = -1, REQUEST_INTENT_8 = 108, REQUEST_INTENT_9 = 109, REQUEST_INTENT_10 = 110;
    final int REQUEST_INTENT_6 = 106, REQUEST_INTENT_7 = 107;


    LinearLayout set1_sublayout, set2_text, set2_text2, set1_subText1, set1_subText3, set1_subText4, set1_subTitleLayout;
    String[] set1_subTitle;
    TextView set1_subTitle_text;
    static TextView set1_sub_add;
    static RecyclerView set_recycler;
    static SettingAdapter adapter = new SettingAdapter();
    static SavingAdapter adapter2 = new SavingAdapter();

    Animation trans_left, trans_right;
    TransListener animListener = new TransListener();

    private AdView mAdView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_set_fragment, container, false);

        set2_text = rootView.findViewById(R.id.set2_text);
        set2_text2 = rootView.findViewById(R.id.set2_text2);
        set1_subText1 = rootView.findViewById(R.id.set1_subText1);
        set1_subText3 = rootView.findViewById(R.id.set1_subText3);
        set1_subText4 = rootView.findViewById(R.id.set1_subText4);
        set1_sublayout = rootView.findViewById(R.id.set1_sublayout);
        set1_subTitle_text = rootView.findViewById(R.id.set1_subTitle_text);
        set1_subTitleLayout = rootView.findViewById(R.id.set1_subTitleLayout);
        set_recycler = rootView.findViewById(R.id.set_recycler);
        set1_sub_add = rootView.findViewById(R.id.set1_sub_add);

        set1_subTitle = new String[]{"수입/지출 항목 카테고리 관리", "수입 내역 카테고리 관리", "지출 내역 카테고리 관리"};

        trans_left = AnimationUtils.loadAnimation(getContext(), R.anim.trans_left);
        trans_right = AnimationUtils.loadAnimation(getContext(), R.anim.trans_right);
        trans_right.setAnimationListener(animListener);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        set_recycler.setLayoutManager(manager);
        set_recycler.setAdapter(adapter);

        set2_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Set_DeleteData_Dialog.class);
                startActivityForResult(intent, REQUEST_INTENT_6);
            }
        });

        set2_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set1_subTitle_text.setText("저축 내역 모아보기");
                set1_sublayout.setVisibility(View.VISIBLE);
                set1_sublayout.startAnimation(trans_left);

                set1_sub_add.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                loadSetSavingData();
            }
        });

        set1_sub_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;

                if (num == 0 | num == 1){
                    intent = new Intent(getContext(), Add_Title_Dialog.class);
                } else if (num == 2){
                    intent = new Intent(getContext(), Add_Content_Dialog.class);
                    intent.putExtra("what", "수입");
                } else if (num == 3){
                    intent = new Intent(getContext(), Add_Content_Dialog.class);
                    intent.putExtra("what", "지출");
                }
                startActivityForResult(intent, REQUEST_INTENT_7);
            }
        });

        set1_subTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set1_sublayout.startAnimation(trans_right);
            }
        });

        set1_subText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 0;
                set1_subTitle_text.setText(set1_subTitle[0]);
                set1_sublayout.setVisibility(View.VISIBLE);
                set1_sublayout.startAnimation(trans_left);

                loadSetTitleData(0);
            }
        });

        set1_subText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 2;
                set1_subTitle_text.setText(set1_subTitle[1]);
                set1_sublayout.setVisibility(View.VISIBLE);
                set1_sublayout.startAnimation(trans_left);

                loadSetTitleData(2);
            }
        });

        set1_subText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 3;
                set1_subTitle_text.setText(set1_subTitle[2]);
                set1_sublayout.setVisibility(View.VISIBLE);
                set1_sublayout.startAnimation(trans_left);

                loadSetTitleData(3);
            }
        });

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView3 = rootView.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest);

        return rootView;
    }

    class TransListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animatrion) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            set1_sublayout.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public int loadSetSavingData(){
        AccountDatabase.println("loadSetSavingData called.");

        String sql = "select _id, CONTENT2, MONEY, CONTENT from " + AccountDatabase.TABLE_ACCOUNT +
                " order by CONTENT2 desc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            ArrayList<SavingItem> items = new ArrayList<>();
            ArrayList<String> contents = new ArrayList<>();
            ArrayList<Integer> moneys = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String content2 = cursor.getString(1);
                int money = cursor.getInt(2);
                String content = cursor.getString(3);

                if (content.equals("저축 출금")){
                    money = money * -1;
                } else if (content.equals("저축")){
                    money = money * 1;
                } else {
                    money = 0;
                }

                Log.d("1818", "moneytqtq" + money);
                boolean c_value = true;
                int a_num = -1;
                for (int a = 0; a < contents.size(); a++){
                    if (contents.get(a).equals(content2)){
                        c_value = false;
                        a_num = a;
                    }
                }
                if (c_value){
                    contents.add(content2);
                    moneys.add(money);
                } else {
                    int moneys_money = moneys.get(a_num) + money;
                    moneys.set(a_num, moneys_money);
                }

            }
            cursor.close();

            for (int b = 0; b < contents.size(); b++){
                items.add(new SavingItem(contents.get(b), moneys.get(b)));
            }

            set_recycler.setAdapter(adapter2);
            adapter2.setItems(items);
            adapter2.notifyDataSetChanged();
        }
        return recordCount;
    }

    public static int loadSetTitleData(int num){

        AccountDatabase.println("loadSetTitleData called.");

        if (set1_sub_add.getWidth() == 0){
            set1_sub_add.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        String sql = "select _id, WHAT, TITLENAME, CONTENT from " + AccountDatabase.TABLE_TITLE +
                " order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            if (recordCount == 0){
                saveFirstSetting(num);
                return 0;
            }

            ArrayList<SettingItem> items = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int _id = cursor.getInt(0);
                String what = cursor.getString(1);
                String title = cursor.getString(2); //입금된 곳 또는 출금된 곳을 의미
                String content = cursor.getString(3); //교통비, 급여 등등 항목을 의미

                if (num == 0){
                    if (what.equals("수입")){
                        if (!title.equals("")){
                            items.add(new SettingItem(title));
                        }
                    }
                } else if (num == 1){
                    if (what.equals("지출")){
                        if (!title.equals("")){
                            items.add(new SettingItem(title));
                        }
                    }
                } else if (num == 2){
                    if (what.equals("수입")){
                        if (!content.equals("")){
                            items.add(new SettingItem(content));
                        }
                    }
                } else if (num == 3){
                    if (what.equals("지출")){
                        if (!content.equals("")){
                            items.add(new SettingItem(content));
                        }
                    }
                }
            }
            cursor.close();

            set_recycler.setAdapter(adapter);
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
        return recordCount;
    }

    public static void saveFirstSetting(int num){
        AccountDatabase.println("saveFirstSetting called.");

        String[] p_title = new String[]{"현금"};
        String[] p_content = new String[]{"급여", "용돈", "결제 취소", "저축 출금", "기타"};
        for (int i = 0; i < p_title.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "수입" + "', "
                    + "'" + p_title[i] + "', " + "'" + p_title[i] + "', " + "'" + "', " + "'" + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        for (int i = 0; i < p_content.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "수입" + "', "
                    + "'" + "', " + "'" + "', " + "'" + p_content[i] + "', " + "'"  + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        String[] m_title = new String[]{"현금"};
        String[] m_content = new String[]{"저축", "식비", "교통비", "선결제", "기타"};
        for (int i = 0; i < m_title.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "지출" + "', "
                    + "'" + m_title[i] + "', " + "'" + m_title[i] + "', " + "'" + "', " + "'" + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }

        for (int i = 0; i < m_content.length; i++){
            String sql = "insert into " + AccountDatabase.TABLE_TITLE
                    + "(WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME) values(" + "'" + "지출" + "', "
                    + "'" + "', " + "'" + "', " + "'" + m_content[i] + "', " + "'"  + "', " + "'" + "', " + "'" + "')";

            AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }
        loadSetTitleData(num);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INTENT_7){
            loadSetTitleData(num);
        }
    }
}
