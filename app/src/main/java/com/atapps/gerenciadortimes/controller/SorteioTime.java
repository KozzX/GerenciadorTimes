package com.atapps.gerenciadortimes.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.adapter.ListJogadorAdapter;
import com.atapps.gerenciadortimes.adapter.ListJogadorAdapterInverse;
import com.atapps.gerenciadortimes.adapter.ListTimeAdapter;
import com.atapps.gerenciadortimes.dao.DaoJogador;
import com.atapps.gerenciadortimes.dao.DaoTime;
import com.atapps.gerenciadortimes.domain.Jogador;
import com.atapps.gerenciadortimes.domain.Time;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SorteioTime extends AppCompatActivity {

    private Handler mUiHandler = new Handler();

    private ListJogadorAdapter jogadorAdapter1;
    private ListJogadorAdapterInverse jogadorAdapter2;
    private List<Jogador> listJog;
    private List<Jogador> jogadores1;
    private List<Jogador> jogadores2;
    private DaoJogador db;
    private ListView lstJogador1;
    private ListView lstJogador2;
    private Bundle extras;
    private Long idTime;
    private int tentativas = 0;
    private RatingBar rtTime1;
    private RatingBar rtTime2;
    private TextView txtPlacar1;
    private TextView txtPlacar2;
    private int placar1 = 0;
    private int placar2 = 0;
    private final float DIFERENCATIME = 1f;
    private boolean running = false;
    private Chronometer chrono;
    private long lastPause;
    private int contPlay = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lstJogador1 = (ListView) findViewById(R.id.lstJogador1);
        lstJogador2 = (ListView) findViewById(R.id.lstJogador2);
        rtTime1 = (RatingBar) findViewById(R.id.rtTim1);
        rtTime2 = (RatingBar) findViewById(R.id.rtTim2);
        txtPlacar1 = (TextView) findViewById(R.id.txtPlacar1);
        txtPlacar2 = (TextView) findViewById(R.id.txtPlacar2);
        chrono = (Chronometer) findViewById(R.id.chronometer);

        extras = getIntent().getExtras();
        if (extras!=null) {
            this.idTime = extras.getLong("TIME");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lstJogador1.getCount() > 0){
                    if ((contPlay==0) && (running == false)){
                        contPlay = 1;
                        running = true;
                        chrono.setBase(SystemClock.elapsedRealtime());
                        chrono.start();
                    }else if ((running == false)){
                        running = true;
                        chrono.setBase(chrono.getBase() + SystemClock.elapsedRealtime() - lastPause);
                        chrono.start();
                    }else{
                        running = false;
                        lastPause = SystemClock.elapsedRealtime();
                        chrono.stop();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SorteioTime.this);
                    builder.setTitle(R.string.mensagem);
                    builder.setMessage(R.string.erro_inicio)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        this.db = new DaoJogador(this);

        lstJogador1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (running == true){
                    view.showContextMenu();
                }
            }
        });
        lstJogador2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (running == true){
                    view.showContextMenu();
                }

            }
        });

        final Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sortearJogadores();
                    }
                });
            }
        });
        myThread.start();


    }

    public void sortearJogadores(){

        this.listJog = db.buscar(idTime);
        this.jogadores1 = new ArrayList<Jogador>();
        this.jogadores2 = new ArrayList<Jogador>();
        this.jogadores1.clear();
        this.jogadores2.clear();

        List<Jogador>goleiro = new ArrayList<Jogador>();
        int index = 0;
        float forca1 = 0;
        float forca2 = 0;
        int tam = listJog.size();
        for (int i = 0; i < tam; i++){
            Log.d("GOLEIROS",goleiro.size()+" " + listJog.get(i).isGoleiro() + " " + listJog.get(i).getNome());
            if (listJog.get(i).isGoleiro()){
                goleiro.add(listJog.get(i));
            }
        }
        Collections.shuffle(goleiro);
        jogadores1.add(goleiro.get(0));
        forca1 = forca1 + goleiro.get(0).getForca();
        jogadores2.add(goleiro.get(1));
        forca2 = forca2 + goleiro.get(1).getForca();
        goleiro.clear();
        while (listJog.size()>0){
            Collections.shuffle(listJog);
            if (listJog.get(0).isGoleiro()){
                listJog.remove(0);
            }else {
                if (index < ((tam-2) / 2)) {
                    Log.d("NOSSOTIME2", index + " - " + tam + " - " + listJog.get(0).getNome());
                    jogadores2.add(listJog.get(0));
                    forca2 = forca2 + listJog.get(0).getForca();
                    listJog.remove(0);
                } else {
                    Log.d("NOSSOTIME1", index + " - " + tam + " - " + listJog.get(0).getNome());
                    jogadores1.add(listJog.get(0));
                    forca1 = forca1 + listJog.get(0).getForca();
                    listJog.remove(0);
                }
                index++;
            }
        }
        float media1 = forca1 / jogadores1.size();
        float media2 = forca2 / jogadores2.size();

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        rtTime1.setStepSize((float) 0.1);
        rtTime2.setStepSize((float) 0.1);
        decimalFormat.format(media1);
        decimalFormat.format(media2);

        rtTime1.setRating(media1);
        rtTime2.setRating(media2);

        Log.d("MEDIA", rtTime1.getRating() + " " + rtTime2.getRating());


        Log.d("MEDIAS", media1 + " - " + media2 + " - " + (media1 - media2));
        if (((media1-media2)>DIFERENCATIME) || ((media1-media2) < (-DIFERENCATIME))){
            tentativas++;
            if (tentativas > 20){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.mensagem);
                builder.setMessage(R.string.erro_tentativas_equilibrio)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else {
                Log.d("MEDIAS ACIMA", media1 + " - " + media2 + " - " + (media1 - media2) + "tent" + tentativas);
                sortearJogadores();
            }
        }

        Log.d("VELOCIDADE", "ANTESADAPTER");
        this.jogadorAdapter1 = new ListJogadorAdapter(this,jogadores1);
        this.lstJogador1.setAdapter(jogadorAdapter1);
        this.jogadorAdapter2 = new ListJogadorAdapterInverse(this,jogadores2);
        this.lstJogador2.setAdapter(jogadorAdapter2);

        registerForContextMenu(lstJogador1);
        registerForContextMenu(lstJogador2);
        Log.d("VELOCIDADE", "DEPOISADAPTER");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_sorteio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if(v.getId()== R.id.lstJogador1){
            menu.setHeaderTitle(jogadorAdapter1.getItem(info.position).getNome());
            menu.add(1, 0, 0, R.string.marcar_gol);
            menu.add(1, 1, 0, R.string.retirar_gol);
            menu.add(1, 2, 0, R.string.trocar_time);

        }else if (v.getId()==R.id.lstJogador2){
            menu.setHeaderTitle(jogadorAdapter2.getItem(info.position).getNome());
            menu.add(2, 0, 0, R.string.marcar_gol);
            menu.add(2, 1, 0, R.string.retirar_gol);
            menu.add(2, 2, 0, R.string.trocar_time);

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId()==1){
            if (item.getItemId()==0){
                //jogadorAdapter1.getItem(info.position).getId());
                placar1 = placar1 + 1;
                txtPlacar1.setText(placar1+"");

            }else if (item.getItemId()==1){
                //jogadorAdapter2.getItem(info.position).getId());
                placar1 = placar1 - 1;
                txtPlacar1.setText(placar1+"");

            }else if (item.getItemId()==2){
                this.jogadores2.add(jogadorAdapter1.getItem(info.position));
                this.jogadores1.remove(info.position);
                this.jogadorAdapter1 = new ListJogadorAdapter(this,jogadores1);
                this.lstJogador1.setAdapter(jogadorAdapter1);
                this.jogadorAdapter2 = new ListJogadorAdapterInverse(this,jogadores2);
                this.lstJogador2.setAdapter(jogadorAdapter2);
            }
        }else if(item.getGroupId()==2){
            if (item.getItemId()==0){
                //jogadorAdapter1.getItem(info.position).getId());
                placar2 = placar2 + 1;
                txtPlacar2.setText(placar2+"");

            }else if (item.getItemId()==1){
                //jogadorAdapter2.getItem(info.position).getId());
                placar2 = placar2 - 1;
                txtPlacar2.setText(placar2+"");

            }else if (item.getItemId()==2){
                this.jogadores1.add(jogadorAdapter2.getItem(info.position));
                this.jogadores2.remove(info.position);
                this.jogadorAdapter1 = new ListJogadorAdapter(this,jogadores1);
                this.lstJogador1.setAdapter(jogadorAdapter1);
                this.jogadorAdapter2 = new ListJogadorAdapterInverse(this,jogadores2);
                this.lstJogador2.setAdapter(jogadorAdapter2);
            }

        }
        return true;
    }

}
