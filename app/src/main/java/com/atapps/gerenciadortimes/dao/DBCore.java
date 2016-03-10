package com.atapps.gerenciadortimes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ANDRE on 05/03/2016.
 */
public class DBCore extends SQLiteOpenHelper {

    private static final String NOME_DB = "times";
    private static final int  VERSAO_DB = 2;

    public DBCore(Context context){
        super(context, NOME_DB, null, VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TIME (" +
                " CODTIM integer primary key autoincrement, " +
                " NOMTIM text not null, " +
                " DIATIM text," +
                " HORTIM text," +
                " LOCTIM text," +
                " IMGTIM text," +
                " CIRTIM text)");

        db.execSQL("create table INFO (" +
                " CODINFO integer primary key autoincrement, " +
                " ULTTIM integer," +
                " ULTJOG integer)");

        db.execSQL( "create table JOGADOR (" +
                " CODJOG integer primary key autoincrement," +
                " NOMJOG text not null," +
                " CODTIM integer not null," +
                " TELJOG text," +
                " INDGOL integer," +
                " FORJOG integer," +
                " IMGJOG text," +
                " CIRJOG text)");

        ContentValues valores = new ContentValues();
        valores.put("ULTTIM", 0);
        valores.put("ULTJOG", 0);
        db.insert("INFO", null, valores);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table TIME");
        db.execSQL("drop table INFO");
        db.execSQL("drop table JOGADOR");
        onCreate(db);
    }

}