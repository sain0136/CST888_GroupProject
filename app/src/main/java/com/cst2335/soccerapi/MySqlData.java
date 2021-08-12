package com.cst2335.soccerapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Sql connection class
 */
public class MySqlData extends SQLiteOpenHelper {
    /** database name */
    public static final String name = "TheDatabase";
    /** database version */
    public static final int version = 1;
    /** individual column names for table */
    public static final String TABLE_NAME = "MovieDatbase";
    /** individual column names for table */
    public static final String col_movie_Title = "movieTitle";
    /** individual column names for table */
    public static final String col_year = "year";
    /** individual column names for table */
    public static final String col_rating = "rating";
    /** individual column names for table */
    public static final String col_runtime = "runtime";
    /** individual column names for table */
    public static final String col_plot = "plot";
    /** individual column names for table */
    public static final String col_mainActors = "mainActors";
    /** individual column names for table */
    public static final String col_moviePosterUrl = "moviePosterUrl";

    /**
     * For the context
     * @param context
     */
    public MySqlData(Context context) {
        super(context, name, null, version);
    }
    /**
     * for creating database
     * @param db pass in database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" Create Table "+ TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_movie_Title + " TEXT ,"
                + col_year + " TEXT ,"
                + col_rating + " TEXT ,"
                + col_runtime + " TEXT ,"
                + col_plot + " TEXT ,"
                + col_mainActors + " TEXT ,"
                + col_moviePosterUrl + " TEXT);");
    }

    /**
     * Used to drop table if version is changed
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_NAME);
    }
}
