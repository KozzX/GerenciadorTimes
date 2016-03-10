package com.atapps.gerenciadortimes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atapps.gerenciadortimes.domain.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 05/03/2016.
 */
public class DaoTime {
    private SQLiteDatabase db;

    public DaoTime(Context context){
        DBCore auxDb = new DBCore(context);
        db = auxDb.getWritableDatabase();
    }

    public void inserir(Time time){
        ContentValues valores = new ContentValues();
        valores.put("NOMTIM", time.getNome());
        valores.put("DIATIM", time.getDia());
        valores.put("HORTIM", time.getHora());
        valores.put("LOCTIM", time.getLocal());
        valores.put("IMGTIM", time.getFotoPath());
        valores.put("CIRTIM", time.getCirclePath());

        db.insert("TIME", null, valores);
        atualizarInfo(ultimoId());
    }

    public void atualizar(Time time){
        ContentValues valores = new ContentValues();
        valores.put("NOMTIM", time.getNome());
        valores.put("DIATIM", time.getDia());
        valores.put("HORTIM", time.getHora());
        valores.put("LOCTIM", time.getLocal());
        valores.put("IMGTIM", time.getFotoPath());
        valores.put("CIRTIM", time.getCirclePath());

        db.update("TIME", valores, "CODTIM = ?", new String[]{"" + time.getId()});
    }

    public void deletar(Time time){
        db.delete("TIME", "CODTIM = " + time.getId(), null);
    }

    public List<Time> buscar(){
        List<Time>list = new ArrayList<Time>();
        String[] colunas = new String[]{"CODTIM", "NOMTIM", "DIATIM", "HORTIM", "LOCTIM", "IMGTIM", "CIRTIM"};
        Cursor cursor = db.query("TIME", colunas, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            Time time = new Time();
            time.setId(cursor.getLong(0));
            time.setNome(cursor.getString(1));
            time.setDia(cursor.getString(2));
            time.setHora(cursor.getString(3));
            time.setLocal(cursor.getString(4));
            time.setFotoPath(cursor.getString(5));
            time.setCirclePath(cursor.getString(6));
            list.add(time);

            cursor.moveToNext();
        }
        return list;

    }
    public Time buscarTimePorId(Long id){
        String[] colunas = new String[]{"CODTIM", "NOMTIM", "DIATIM", "HORTIM", "LOCTIM", "IMGTIM", "CIRTIM"};
        Cursor cursor = db.query("TIME",colunas, "CODTIM = "+ id, null, null, null, null);
        cursor.moveToFirst();
        Time time = new Time();
        time.setId(cursor.getLong(0));
        time.setNome(cursor.getString(1));
        time.setDia(cursor.getString(2));
        time.setHora(cursor.getString(3));
        time.setLocal(cursor.getString(4));
        time.setFotoPath(cursor.getString(5));
        time.setCirclePath(cursor.getString(6));
        return time;
    }

    public Long ultimoId(){
        String[] colunas = new String[]{"CODTIM"};
        Cursor cursor = db.query("TIME", colunas, null, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToLast();
            return cursor.getLong(0);
        }else{
            return Long.valueOf(0);
        }

    }
    public void atualizarInfo(Long ulttim){
        ContentValues valores = new ContentValues();
        valores.put("ULTTIM", ulttim);
        db.update("INFO", valores, "CODINFO = ?", new String[]{"1"});
    }
    public Long ultimoIdTime(){
        String[] colunas = new String[]{"ULTTIM"};
        Cursor cursor = db.query("INFO", colunas, " CODINFO = 1", null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount()>0){
            return cursor.getLong(0);
        }else{
            return Long.valueOf(0);
        }

    }

}
