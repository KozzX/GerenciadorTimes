package com.atapps.gerenciadortimes.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.adapter.ListJogadorAdapter;
import com.atapps.gerenciadortimes.dao.DaoJogador;
import com.atapps.gerenciadortimes.dao.DaoTime;
import com.atapps.gerenciadortimes.domain.Jogador;

import java.util.Collections;
import java.util.List;

public class TelaListaJogadores extends AppCompatActivity {

    private ListJogadorAdapter jogadorAdapter;
    private ListView lstJogador;
    private List<Jogador> jogadores;
    private DaoJogador db;
    private DaoTime dbTime;
    private Bundle extras;
    private Long idTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista_jogadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TelaListaJogadores.this, TelaCadastroJogador.class);
                i.putExtra("TIME",idTime);
                startActivityForResult(i, 1);
            }
        });

        this.lstJogador = (ListView) findViewById(R.id.lstJogadores);
        this.db = new DaoJogador(this);


        extras = getIntent().getExtras();
        if (extras!=null){
            this.idTime = extras.getLong("TIME");
            this.dbTime = new DaoTime(this);
            actionBar.setTitle("Jogadores");
            actionBar.setSubtitle(this.dbTime.buscarTimePorId(idTime).getNome());
        }

        listaJogador();
        registerForContextMenu(lstJogador);

        lstJogador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TelaListaJogadores.this, TelaCadastroJogador.class);
                i.putExtra("ID",jogadorAdapter.getItem(position).getId());
                startActivityForResult(i,1);
            }
        });
    }

    public void listaJogador(){
        this.jogadores = db.buscar(idTime);

        for (int i = 0; i<jogadores.size(); i++){
            Log.d("BANCO", jogadores.get(i).getNome());
        }
        this.jogadorAdapter = new ListJogadorAdapter(this,jogadores);
        this.lstJogador.setAdapter(jogadorAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listaJogador();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sorteio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        if (id==R.id.action_sortear){
            Log.d("SORTEIO","Ir para sorteio");
            int qtdGol = 0;
            for (int i = 0; i < jogadores.size();i++){
                if (jogadores.get(i).isGoleiro()){
                    qtdGol++;
                }
            }
            if(qtdGol != 2){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.erro_dois_goleiros)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else {
                Intent i = new Intent(TelaListaJogadores.this, SorteioTime.class);
                i.putExtra("TIME", idTime);
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(jogadorAdapter.getItem(info.position).getNome());
        menu.add(0, 0, 0, R.string.editar);
        menu.add(0, 1, 0, R.string.excluir);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId()==0){
            if (item.getItemId()==0){
                Intent i = new Intent(TelaListaJogadores.this, TelaCadastroJogador.class);
                i.putExtra("ID",jogadorAdapter.getItem(info.position).getId());
                startActivityForResult(i,1);

            }else if (item.getItemId()==1){
                this.db.deletar(jogadorAdapter.getItem(info.position));
                this.listaJogador();
            }
        }
        return true;
    }

}
