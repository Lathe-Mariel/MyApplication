package com.akiranagai.myapplication.gamecontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAccess extends SQLiteOpenHelper {
    public static final String DATABESE_NAME = "score.db";
    public static final int DATABASE_VERSION = 1;
    private static final String COLUMN_NAME_SUBTITLE = "score";
    private static int k;

    private static final String SQL_CREATE_ENTRIES0 =
            "CREATE TABLE " + "scoreTable";

    private static final String SQL_CREATE_ENTRIES1 = " (" + COLUMN_NAME_SUBTITLE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "scoreTable";

    protected SQLiteDatabase db;

    SQLiteAccess(Context context){
        super(context, DATABESE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        for(k = 0; k < StageSelectActivity.SectionsPagerAdapter.PAGES; k++)
            db.execSQL(SQL_CREATE_ENTRIES0 +k + SQL_CREATE_ENTRIES1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void saveData(int stageNumber, int score) {
        ContentValues values = new ContentValues();
        values.put("score", score);
long error;
        error = db.insert("scoreTable"+stageNumber, null, values);
        Log.d("messageg", "insert return: " + error);
        db.close();
    }

    public Cursor getAllNotes(int stageNumber){
        Cursor cursor = db.query("scoreTable"+stageNumber, null, null,null,null,null,null);
        return cursor;
    }

    public void close(){
        db.close();
    }
}
