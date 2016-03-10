package com.atapps.gerenciadortimes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.atapps.gerenciadortimes.domain.Jogador;
import com.atapps.gerenciadortimes.domain.Time;

import java.util.List;

/**
 * Created by ANDRE on 05/03/2016.
 */
public class DBCore extends SQLiteOpenHelper {


    private static final String NOME_DB = "times";
    private static final int  VERSAO_DB = 5;

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

        db.execSQL("create table JOGADOR (" +
                " CODJOG integer primary key autoincrement," +
                " NOMJOG text not null," +
                " CODTIM integer not null," +
                " TELJOG text," +
                " INDGOL integer," +
                " FORJOG integer," +
                " IMGJOG text," +
                " CIRJOG text)");

        /*db.execSQL(" create table PARTIDA (" +
                " CODPAR integer primary key autoincrement, " +
                " TIMPAR integer not null," +
                " DATPAR )");*/

        ContentValues valores = new ContentValues();
        valores.put("ULTTIM", 0);
        valores.put("ULTJOG", 0);
        db.insert("INFO", null, valores);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TEMPORARY TABLE TIME_BACKUP (" +
                " CODTIM integer primary key autoincrement, " +
                " NOMTIM text not null, " +
                " DIATIM text," +
                " HORTIM text," +
                " LOCTIM text," +
                " IMGTIM text," +
                " CIRTIM text)");

        db.execSQL("CREATE TEMPORARY TABLE INFO_BACKUP (" +
                " CODINFO integer primary key autoincrement, " +
                " ULTTIM integer," +
                " ULTJOG integer)");

        db.execSQL( "CREATE TEMPORARY TABLE JOGADOR_BACKUP (" +
                " CODJOG integer primary key autoincrement," +
                " NOMJOG text not null," +
                " CODTIM integer not null," +
                " TELJOG text," +
                " INDGOL integer," +
                " FORJOG integer," +
                " IMGJOG text," +
                " CIRJOG text)");

        //db.execSQL("INSERT INTO TIME_BACKUP SELECT CODTIM, NOMTIM, DIATIM, HORTIM, LOCTIM, IMGTIM, CIRTIM FROM TIME ");
        //db.execSQL("INSERT INTO INFO_BACKUP SELECT CODINFO, ULTTIM, ULTJOG FROM INFO ");
        //db.execSQL("INSERT INTO JOGADOR_BACKUP SELECT CODJOG, NOMJOG, CODTIM, TELJOG, INDGOL, FORJOG, IMGJOG, CIRJOG FROM JOGADOR ");
        db.execSQL("INSERT INTO TIME_BACKUP SELECT * FROM TIME ");
        db.execSQL("INSERT INTO INFO_BACKUP SELECT * FROM INFO ");
        db.execSQL("INSERT INTO JOGADOR_BACKUP SELECT * FROM JOGADOR ");

        db.execSQL("drop table TIME");
        db.execSQL("drop table INFO");
        db.execSQL("drop table JOGADOR");

        onCreate(db);

        //db.execSQL("INSERT INTO TIME SELECT CODTIM, NOMTIM, DIATIM, HORTIM, LOCTIM, IMGTIM, CIRTIM FROM TIME_BACKUP ");
        //db.execSQL("INSERT INTO JOGADOR SELECT CODJOG, NOMJOG, CODTIM, TELJOG, INDGOL, FORJOG, IMGJOG, CIRJOG FROM JOGADOR_BACKUP ");

        db.execSQL("INSERT INTO TIME SELECT * FROM TIME_BACKUP ");
        db.execSQL("UPDATE INFO SET ULTTIM=(SELECT ULTTIM FROM INFO_BACKUP), ULTJOG=(SELECT ULTJOG FROM INFO_BACKUP) WHERE CODINFO = 1 ");
        db.execSQL("INSERT INTO JOGADOR SELECT * FROM JOGADOR_BACKUP ");

        db.execSQL("drop table TIME_BACKUP");
        db.execSQL("drop table INFO_BACKUP");
        db.execSQL("drop table JOGADOR_BACKUP");

        Log.d("ATUALIZADB","ATUALIZOU" + oldVersion + " - " + newVersion);

    }

}