package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/*

 * This is a simple SQLiteDatabase, I think that functions are pretty
 * straightforward so they don't need any sort of comments. If the reader
 * still has any doubts on them, please reach me at: 257544@studenti.unimore.it
 */

public class ArticleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ARTICLES = "articles";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_ARTICLES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_URL + " TEXT)";

    public ArticleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database migration if needed
    }

    public void addArticle(String title, String url) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_URL, url);
        db.insert(TABLE_ARTICLES, null, values);
        db.close();
    }

    public Cursor getAllArticles() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_ARTICLES, null, null, null, null, null, null);
    }

    public int deleteAllArticles() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_ARTICLES, null, null);
    }

    public ArrayList<String> getAllArticleTitles() {
        ArrayList<String> articleTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, new String[]{COLUMN_TITLE}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                articleTitles.add(title);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return articleTitles;
    }
}
