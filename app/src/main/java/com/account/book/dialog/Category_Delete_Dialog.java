package com.account.book.dialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.fragment.ActivitySetFragment;
import com.account.book.recyclerview5.SettingAdapter;

public class Category_Delete_Dialog extends AppCompatActivity {


    TextView no_text, ok_text, text;
    LinearLayout category_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_category);

        text = findViewById(R.id.category_text);
        no_text = findViewById(R.id.category_no_text);
        ok_text = findViewById(R.id.category_ok_text);
        category_layout = findViewById(R.id.category_layout);

        if (ActivitySetFragment.num == 0 | ActivitySetFragment.num == 1){
            text.setText("현금");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            category_layout.setLayoutParams(params);

        } else if (ActivitySetFragment.num == 2 | ActivitySetFragment.num == 3){
            text.setText("기타");
        }


        no_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getStringExtra("name").equals("삭제")){
                    if (ActivitySetFragment.num == 0 | ActivitySetFragment.num == 1){
                        SettingAdapter.deleteTitleName(getIntent().getStringExtra("text"));
                    } else if (ActivitySetFragment.num == 2){
                        SettingAdapter.deleteContent("수입", getIntent().getStringExtra("text"));
                    } else if (ActivitySetFragment.num == 3){
                        SettingAdapter.deleteContent("지출", getIntent().getStringExtra("text"));
                    }

                    ActivitySetFragment.loadSetTitleData(ActivitySetFragment.num);

                }


                finish();
            }
        });
    }
}
