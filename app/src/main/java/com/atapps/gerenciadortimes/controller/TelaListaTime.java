package com.atapps.gerenciadortimes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.adapter.ListTimeAdapter;
import com.atapps.gerenciadortimes.dao.DaoTime;
import com.atapps.gerenciadortimes.domain.Time;

import java.util.List;

public class TelaListaTime extends AppCompatActivity {

    private ListTimeAdapter timeAdapter;
    private ListView lstTime;
    private List<Time> times;
    private DaoTime db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Meus Times");
        //actionBar.setSubtitle("Lista de Times");
        //actionBar.setIcon(R.);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TelaListaTime.this, TelaCadastroTime.class);
                startActivityForResult(i, 1);
            }
        });

        this.lstTime = (ListView) findViewById(R.id.lstTimes);
        this.db = new DaoTime(this);


        listaTimes();
        registerForContextMenu(lstTime);

        lstTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TelaListaTime.this,TelaListaJogadores.class);
                i.putExtra("TIME",timeAdapter.getItem(position).getId());
                startActivity(i);

            }
        });
    }

    public void listaTimes(){
        this.times = db.buscar();
        for (int i = 0; i<times.size(); i++){
            Log.d("BANCO",times.get(i).getNome());
        }
        this.timeAdapter = new ListTimeAdapter(this,times);
        this.lstTime.setAdapter(timeAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listaTimes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(timeAdapter.getItem(info.position).getNome());
        menu.add(0, 0, 0, R.string.editar);
        menu.add(0, 1, 0, R.string.excluir);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId()==0){
            if (item.getItemId()==0){
                Intent i = new Intent(TelaListaTime.this, TelaCadastroTime.class);
                i.putExtra("ID",timeAdapter.getItem(info.position).getId());
                startActivityForResult(i,1);

            }else if (item.getItemId()==1){
                this.db.deletar(timeAdapter.getItem(info.position));
                this.listaTimes();
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUMIR", "onResume");
        this.listaTimes();
    }


}
