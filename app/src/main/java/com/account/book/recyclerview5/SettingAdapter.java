package com.account.book.recyclerview5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.book.AccountDatabase;
import com.account.book.MainActivity;
import com.account.book.R;
import com.account.book.dialog.Category_Delete_Dialog;
import com.account.book.dialog.Modify_Content_Dialog;
import com.account.book.dialog.Modify_Title_Dialog;
import com.account.book.fragment.ActivitySetFragment;
import com.account.book.recyclerview2.AccountItem;
import com.account.book.recyclerview4.ListSubAdapter;
import com.account.book.swipelayout.Swipe_Layout;
import com.account.book.swipelayout.ViewBinderListener;

import java.util.ArrayList;


public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder>{

    ViewBinderListener listener = new ViewBinderListener();

    ArrayList<SettingItem> items = new ArrayList<>();
    static Context mContext;

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = (ViewGroup) inflater.inflate(R.layout.item_set1, parent, false);

        this.mContext = parent.getContext();

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingAdapter.ViewHolder holder, int position) {
        SettingItem item = items.get(position);
        holder.setItem(item);


        listener.setOpenOnlyOne(true);
        listener.bind(holder.swipeLayout, String.valueOf(position));
        listener.closeLayout(String.valueOf(position));

        holder.modify_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivitySetFragment.num == 0 | ActivitySetFragment.num == 1){
                    if (holder.setting1_text.getText().toString().equals("현금")){
                        toastSetting(12);
                        return;
                    }
                } else if (ActivitySetFragment.num == 2 | ActivitySetFragment.num == 3){
                    if (holder.setting1_text.getText().toString().equals("기타") | holder.setting1_text.getText().toString().equals("결제 취소")
                            | holder.setting1_text.getText().toString().equals("선결제") | holder.setting1_text.getText().toString().equals("저축")){
                        toastSetting(12);
                        return;
                    }
                }

                loadModifyData(ActivitySetFragment.num, holder.setting1_text.getText().toString());

            }
        });

        holder.delete_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivitySetFragment.num == 0 | ActivitySetFragment.num == 1){
                    if (holder.setting1_text.getText().toString().equals("현금")){
                        toastSetting(12);
                        return;
                    }
                } else if (ActivitySetFragment.num == 2 | ActivitySetFragment.num == 3){
                    if (holder.setting1_text.getText().toString().equals("기타") | holder.setting1_text.getText().toString().equals("결제 취소")
                            | holder.setting1_text.getText().toString().equals("선결제") | holder.setting1_text.getText().toString().equals("저축")){
                        toastSetting(12);
                        return;
                    }
                }
                Intent intent = new Intent(mContext, Category_Delete_Dialog.class);

                intent.putExtra("text", holder.setting1_text.getText().toString());
                intent.putExtra("name", "삭제");

                ((Activity)mContext).startActivityForResult(intent, ActivitySetFragment.REQUEST_INTENT_9);
            }
        });

    }

    public static void loadModifyData(int num, String data){
        AccountDatabase.println("loadModifyData called.");

        String sql = null;

        if (num == 0 | num == 1){
            sql = "select _id, WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME from " + AccountDatabase.TABLE_TITLE +
                    " where TITLENAME = '" + data + "' order by _id asc";
        } else if (num == 2 | num == 3){
            sql = "select _id, WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME from " + AccountDatabase.TABLE_TITLE +
                    " where CONTENT = '" + data + "' order by _id asc";
        }

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                String what = cursor.getString(1);
                String title = cursor.getString(2);
                String titlename = cursor.getString(3);
                String content = cursor.getString(4);
                int payday = cursor.getInt(5);
                int use = cursor.getInt(6);
                String bankname = cursor.getString(7);


                if (num == 0 | num == 1) {
                    Intent intent = new Intent(mContext, Modify_Title_Dialog.class);
                    intent.putExtra("what", what);
                    intent.putExtra("title", title);
                    intent.putExtra("titlename", titlename);
                    intent.putExtra("content", content);
                    intent.putExtra("payday", payday);
                    intent.putExtra("use", use);
                    intent.putExtra("bankname", bankname);


                    ((Activity) mContext).startActivityForResult(intent, ActivitySetFragment.REQUEST_INTENT_8);

                    return;
                } else if (num == 2 | num == 3){
                    Intent intent = new Intent(mContext, Modify_Content_Dialog.class);
                    intent.putExtra("what", what);
                    intent.putExtra("title", title);
                    intent.putExtra("titlename", titlename);
                    intent.putExtra("content", content);
                    intent.putExtra("payday", payday);
                    intent.putExtra("use", use);
                    intent.putExtra("bankname", bankname);

                    ((Activity) mContext).startActivityForResult(intent, ActivitySetFragment.REQUEST_INTENT_8);

                    return;
                }
            }

//            loadCheckETD(ActivitySetFragment.num);

            cursor.close();
        }
    }

    public static void deleteContent(String what, String data){
        AccountDatabase.println("deleteContent called.");

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        String sql1 = "update " + AccountDatabase.TABLE_ACCOUNT + " set "
                + " CONTENT ='" + "기타" + "'" + " where CONTENT = '" + data + "' and WHAT = '" + what +"'";

        String sql2 = "delete from " + AccountDatabase.TABLE_TITLE + " where CONTENT = '" + data + "' and WHAT = '" + what +"'";

        database.execSQL(sql1);
        database.execSQL(sql2);

//        loadCheckETD(ActivitySetFragment.num);

    }

    public static void deleteTitleName(String data){
        AccountDatabase.println("deleteTitleName called.");

        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        String sql1 = "update " + AccountDatabase.TABLE_ACCOUNT + " set " + " TITLE ='" + "현금" + "'" + " where TITLE = '" + data + "'";
        String sql2 = "delete from " + AccountDatabase.TABLE_TITLE + " where TITLENAME = '" + data +"'";

        database.execSQL(sql1);
        database.execSQL(sql2);

        checkBankName(data);
//        loadCheckETD(ActivitySetFragment.num);
    }

    public static void checkBankName(String data){
        AccountDatabase.println("checkBankName called.");

        String sql = "select _id, WHAT, TITLE, TITLENAME, CONTENT, PAYDAY, USE, BANKNAME from " + AccountDatabase.TABLE_TITLE +
                " where BANKNAME = '" + data + "' order by _id asc";

        int recordCount = -1;
        AccountDatabase database = AccountDatabase.getInstance(MainActivity.context);

        if (database != null){
            Cursor cursor = database.rawQuery(sql);

            recordCount = cursor.getCount();
            AccountDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++){
                cursor.moveToNext();

                String titlename = cursor.getString(3);

                String sql2 = "update " + AccountDatabase.TABLE_ACCOUNT + " set " + " TITLE ='" + "현금" + "'" + " where TITLE = '" + titlename + "'";
                database.execSQL(sql2);

            }

            if (recordCount > 0){
                String sql1 = "delete from " + AccountDatabase.TABLE_TITLE + " where BANKNAME = '" + data +"'";

                database.execSQL(sql1);
            }

            cursor.close();
        }
    }

    public static void toastSetting(int num){

        try{
            InputMethodManager inputMethodManager = (InputMethodManager) ((Activity)mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity)mContext).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            Log.d("1818", "자판 내릴 것 없음.");
        }


        View layout = null;

        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

        Toast toast = new Toast(((Activity)mContext).getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        if (num == 1){
            layout = inflater.inflate(R.layout.toast_no_edittext_1, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_no_edittext_1));
        } else if (num == 3){
            layout = inflater.inflate(R.layout.toast_no_edittext_3, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_no_edittext_3));
        } else if (num == 6){
            layout = inflater.inflate(R.layout.toast_no_edittext_6, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_no_edittext_6));
        } else if (num == 9){
            layout = inflater.inflate(R.layout.toast_no_edittext_9, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_no_edittext_9));
        } else if (num == 8){
            layout = inflater.inflate(R.layout.toast_edittext_8, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_edittext_8));
        } else if (num == 10){
            layout = inflater.inflate(R.layout.toast_edittext_10, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_no_edittext_10));
        } else if (num == 12){
            layout = inflater.inflate(R.layout.toast_edittext_12, (ViewGroup) ((Activity)mContext).findViewById(R.id.toast_edittext_12));

        }

        toast.setView(layout);
        toast.show();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<SettingItem> items){
        this.items = items;
    }

    public void addItem(SettingItem item){
        items.add(item);
    }

    public SettingItem getItem(int position){
        return items.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView setting1_text;
        Swipe_Layout swipeLayout;
        FrameLayout modify_click, delete_click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setting1_text = itemView.findViewById(R.id.setting1_text);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            modify_click = itemView.findViewById(R.id.modify_click);
            delete_click = itemView.findViewById(R.id.delete_click);

        }

        public void setItem(SettingItem item){
            setting1_text.setText(item.getTitle());
        }
    }
}
