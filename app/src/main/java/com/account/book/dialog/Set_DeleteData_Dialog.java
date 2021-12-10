package com.account.book.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.google.android.gms.ads.InterstitialAd;

public class Set_DeleteData_Dialog extends AppCompatActivity {

    public static final int RESULT_CODE_OK = 17;
    public final int RESULT_CODE_NO = 18;
    Button s_dial_yes, s_dial_no;
    ProgressBar pro_bar;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_all);

        s_dial_yes = findViewById(R.id.s_dial_yes);
        s_dial_no = findViewById(R.id.s_dial_no);
        pro_bar = findViewById(R.id.progressBar);

        pro_bar.setIndeterminate(true);
        pro_bar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A6A6A6"), PorterDuff.Mode.MULTIPLY);

        s_dial_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pro_bar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pro_bar.setVisibility(View.INVISIBLE);

                        LayoutInflater inflater = getLayoutInflater();

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(Toast.LENGTH_SHORT);

                        View layout = inflater.inflate(R.layout.toast_edittext_7, (ViewGroup) findViewById(R.id.toast_edittext_7));

                        toast.setView(layout);
                        toast.show();

                        String sql1 = "delete from " + AccountDatabase.TABLE_ACCOUNT;
                        String sql2 = "delete from " + AccountDatabase.TABLE_TITLE;

                        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);
                        database.execSQL(sql1);
                        database.execSQL(sql2);

                        Intent intent = new Intent();
                        setResult(RESULT_CODE_OK, intent);

                        finish();

                        mInterstitialAd = new InterstitialAd(getApplicationContext());
                        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//                        "ca-app-pub-3940256099942544/1033173712"   //테스트
//                        "ca-app-pub-8631957304793435/4529131749"   //실사
                    }
                }, 2000);

            }
        });

        s_dial_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CODE_NO, intent);

                finish();
            }
        });

    }
}
