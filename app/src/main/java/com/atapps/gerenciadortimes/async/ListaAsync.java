package com.atapps.gerenciadortimes.async;

import android.os.AsyncTask;

/**
 * Created by ANDRECANDIDO on 10/03/2016.
 */
public class ListaAsync extends AsyncTask {

    interface RetornaLista {
        void retornoInicio();
        void retornoProgresso(int prog);
        void retornoConcluido(int resultado);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
