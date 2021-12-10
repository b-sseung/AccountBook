package com.account.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccountDatabase {
    private static final String TAG = "DATABASE";

    private static AccountDatabase database;
    public static String TABLE_ACCOUNT = "ACCOUNT";
    public static String TABLE_TITLE = "TITLE";
    public static int DATABASE_VERSION = 1;

    public static String DATABASE_NAME = "AccountDatabase";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private AccountDatabase(Context context){
        this.context = context;
    }

    public static AccountDatabase getInstance(Context context){
        if (database == null){
            database = new AccountDatabase(context);
        }

        return database;
    }

    public boolean open(){
        println("opening database [" + DATABASE_NAME + "]");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        println("closing database [" + DATABASE_NAME + "].");
        db.close();

        database = null;
    }

    public Cursor rawQuery(String SQL){
        println("\nexecuteQuery called.\n");

        if (db == null){
            open();
        }

        Cursor cursor = null;
        try{
            cursor = db.rawQuery(SQL, null);
            println("cursor count : " + cursor.getCount());
        } catch(Exception e){
            Log.e(TAG, "Exception in executeQuery", e);
        }

        return cursor;
    }

    public boolean execSQL(String SQL){
        println("\nexecute called.\n");

        try{
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception e){
            Log.e(TAG, "Exception in executeQuery", e);
            return false;
        }

        return true;
    }



    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");
            println("creating table [" + TABLE_ACCOUNT + "].");

            String DROP_SQL = "drop table if exists " + TABLE_ACCOUNT;
            String DROP_SQL2 = "drop table if exists " + TABLE_TITLE;

            try{
                db.execSQL(DROP_SQL);
                db.execSQL(DROP_SQL2);
            } catch (Exception e){
                Log.e(TAG, "Exception in DROP_SQL", e);
            }

            String CREATE_SQL = "create table " + TABLE_ACCOUNT + "("
                    + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "YEAR INTEGER DEFAULT '', "
                    + "MONTH INTEGER DEFAULT '', "
                    + "DAY INTEGER DEFAULT '', "
                    + "WHAT TEXT DEFAULT '', "
                    + "TITLE TEXT DEFAULT '', "
                    + "CONTENT TEXT DEFAULT '', "
                    + "CONTENT2 TEXT DEFAULT '', "
                    + "MONEY INTEGER DEFAULT '', "
                    + "CLEAR TEXT DEFAULT '' "
                    + ")";

            String CREATE_SQL2 = "create table " + TABLE_TITLE + "("
                    + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "WHAT TEXT DEFAULT '', "
                    + "TITLE TEXT DEFAULT '', "
                    + "TITLENAME TEXT DEFAULT '', "
                    + "CONTENT TEXT DEFAULT '', "
                    + "PAYDAY INTEGER DEFAULT '', "
                    + "USE INTEGER DEFAULT '', "
                    + "BANKNAME TEXT DEFAULT '' "
                    + ")";

            //what:수입인지 지출인지, title:현금,은행,카드,기타, titlename:oo은행, oo카드 등등,
            //content:교통비,식비 등등, payday:결제일, use:이용기간, bankname:결제은행

            try{
                db.execSQL(CREATE_SQL);
                db.execSQL(CREATE_SQL2);
            } catch (Exception e){
                Log.e(TAG, "Exception in CREATE_SQL", e);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_ACCOUNT + "_IDX ON " + TABLE_ACCOUNT + "("
                    + "CREATE_DATE" + ")";

            String CREATE_INDEX_SQL2 = "create index " + TABLE_TITLE + "_IDX ON " + TABLE_TITLE + "("
                    + "CREATE_DATE" + ")";
            try{
                db.execSQL(CREATE_INDEX_SQL);
                db.execSQL(CREATE_INDEX_SQL2);
            } catch (Exception e){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", e);
            }

        }

        public void onOpen(SQLiteDatabase db){
            println("opened database [" + DATABASE_NAME + "].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }

    }

    public static void println(String msg){
        Log.d(TAG, msg);
    }
}
