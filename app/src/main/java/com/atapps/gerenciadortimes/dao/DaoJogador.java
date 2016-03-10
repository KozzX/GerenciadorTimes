package com.atapps.gerenciadortimes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atapps.gerenciadortimes.domain.Jogador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRECANDIDO on 07/03/2016.
 */
public class DaoJogador {
    private SQLiteDatabase db;

    public DaoJogador(Context context){
        DBCore auxDb = new DBCore(context);
        db = auxDb.getWritableDatabase();
    }

    public void inserir(Jogador jogador){
        ContentValues valores = new ContentValues();
        valores.put("NOMJOG", jogador.getNome());
        valores.put("CODTIM", jogador.getIdTime());
        valores.put("TELJOG", jogador.getTelefone());
        if (jogador.isGoleiro()){
            valores.put("INDGOL", 1);
        }else{
            valores.put("INDGOL", 0);
        }
        valores.put("FORJOG", jogador.getForca());
        valores.put("IMGJOG", jogador.getFotoPath());
        valores.put("CIRJOG", jogador.getCirclePath());

        db.insert("JOGADOR", null, valores);
        atualizarInfo(ultimoId());
    }

    public void inserirComId(Jogador jogador){
        ContentValues valores = new ContentValues();
        valores.put("CODJOG", jogador.getId());
        valores.put("NOMJOG", jogador.getNome());
        valores.put("CODTIM", jogador.getIdTime());
        valores.put("TELJOG", jogador.getTelefone());
        if (jogador.isGoleiro()){
            valores.put("INDGOL", 1);
        }else{
            valores.put("INDGOL", 0);
        }
        valores.put("FORJOG", jogador.getForca());
        valores.put("IMGJOG", jogador.getFotoPath());
        valores.put("CIRJOG", jogador.getCirclePath());

        db.insert("JOGADOR", null, valores);
        atualizarInfo(ultimoId());
    }

    public void atualizar(Jogador jogador){
        ContentValues valores = new ContentValues();
        valores.put("NOMJOG", jogador.getNome());
        valores.put("CODTIM", jogador.getIdTime());
        valores.put("TELJOG", jogador.getTelefone());
        if (jogador.isGoleiro()){
            valores.put("INDGOL", 1);
        }else{
            valores.put("INDGOL", 0);
        }
        valores.put("FORJOG", jogador.getForca());
        valores.put("IMGJOG", jogador.getFotoPath());
        valores.put("CIRJOG", jogador.getCirclePath());

        db.update("JOGADOR", valores, "CODJOG = ?", new String[]{"" + jogador.getId()});
    }

    public void deletar(Jogador jogador){
        db.delete("JOGADOR", "CODJOG = " + jogador.getId(), null);
    }

    public List<Jogador> buscar(Long idTime){
        List<Jogador>list = new ArrayList<Jogador>();
        String[] colunas = new String[]{"CODJOG", "NOMJOG", "CODTIM", "TELJOG", "INDGOL", "FORJOG", "IMGJOG", "CIRJOG"};
        Cursor cursor = db.query("JOGADOR", colunas, " CODTIM = "+idTime, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            Jogador jogador = new Jogador();
            jogador.setId(cursor.getLong(0));
            jogador.setNome(cursor.getString(1));
            jogador.setIdTime(cursor.getLong(2));
            jogador.setTelefone(cursor.getString(3));
            if (cursor.getInt(4)==1){
                jogador.setGoleiro(true);
            }else if (cursor.getInt(4)==0){
                jogador.setGoleiro(false);
            }
            jogador.setForca(cursor.getFloat(5));
            jogador.setFotoPath(cursor.getString(6));
            jogador.setCirclePath(cursor.getString(7));
            list.add(jogador);

            cursor.moveToNext();
        }
        return list;

    }

    public List<Jogador> buscarTodos(){
        List<Jogador>list = new ArrayList<Jogador>();
        String[] colunas = new String[]{"CODJOG", "NOMJOG", "CODTIM", "TELJOG", "INDGOL", "FORJOG", "IMGJOG", "CIRJOG"};
        Cursor cursor = db.query("JOGADOR", colunas, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            Jogador jogador = new Jogador();
            jogador.setId(cursor.getLong(0));
            jogador.setNome(cursor.getString(1));
            jogador.setIdTime(cursor.getLong(2));
            jogador.setTelefone(cursor.getString(3));
            if (cursor.getInt(4)==1){
                jogador.setGoleiro(true);
            }else if (cursor.getInt(4)==0){
                jogador.setGoleiro(false);
            }
            jogador.setForca(cursor.getFloat(5));
            jogador.setFotoPath(cursor.getString(6));
            jogador.setCirclePath(cursor.getString(7));
            list.add(jogador);

            cursor.moveToNext();
        }
        return list;

    }

    public Jogador buscarTimePorId(Long id){
        String[] colunas = new String[]{"CODJOG", "NOMJOG", "CODTIM", "TELJOG", "INDGOL", "FORJOG", "IMGJOG", "CIRJOG"};
        Cursor cursor = db.query("JOGADOR",colunas, "CODJOG = "+ id, null, null, null, null);
        cursor.moveToFirst();
        Jogador jogador = new Jogador();
        jogador.setId(cursor.getLong(0));
        jogador.setNome(cursor.getString(1));
        jogador.setIdTime(cursor.getLong(2));
        jogador.setTelefone(cursor.getString(3));
        if (cursor.getInt(4)==1){
            jogador.setGoleiro(true);
        }else if (cursor.getInt(4)==0){
            jogador.setGoleiro(false);
        }
        jogador.setForca(cursor.getFloat(5));
        jogador.setFotoPath(cursor.getString(6));
        jogador.setCirclePath(cursor.getString(7));
        return jogador;
    }

    public Long ultimoId(){
        String[] colunas = new String[]{"CODJOG"};
        Cursor cursor = db.query("JOGADOR", colunas, null, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToLast();
            return cursor.getLong(0);
        }else{
            return Long.valueOf(0);
        }

    }
    public void atualizarInfo(Long ultjog){
        ContentValues valores = new ContentValues();
        valores.put("ULTJOG", ultjog);
        db.update("INFO", valores, "CODINFO = ?", new String[]{"1"});
    }
    public Long ultimoIdTime(){
        String[] colunas = new String[]{"ULTJOG"};
        Cursor cursor = db.query("INFO", colunas, " CODINFO = 1", null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount()>0){
            return cursor.getLong(0);
        }else{
            return Long.valueOf(0);
        }

    }
}
