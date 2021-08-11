package com.cst2335.soccerapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SoccerOpenHelper extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "SoccerFavDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "SOCCERNEWS";
    public final static String COL_SOCCERTITLE = "SOCCER_TITLE";
    public final static String COL_SOCCERDATE = "SOCCER_DATE";
    public final static String COL_SOCCERIMG = "SOCCER_IMG";
    public final static String COL_SOCCERDESC = "SOCCER_DESC";
    public final static String COL_SOCCERLINK = "SOCCER_LINK";
    public final static String COL_ID = "ID";


    public SoccerOpenHelper(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SOCCERTITLE + " text,"
                + COL_SOCCERDATE + " text,"
                + COL_SOCCERIMG + " text,"
                + COL_SOCCERDESC + " text,"
                + COL_SOCCERLINK  + " text);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}