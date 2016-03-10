package com.atapps.gerenciadortimes.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.dao.DaoJogador;
import com.atapps.gerenciadortimes.domain.Jogador;
import com.atapps.gerenciadortimes.utils.ImageSaver;
import com.atapps.gerenciadortimes.utils.ScalingUtilities;

public class TelaCadastroJogador extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private EditText edtNomeJogador;
    private EditText edtTelefoneJogador;
    private CheckBox goleiro;
    private RatingBar forcaJogador;
    private ImageView imgProfile;
    private DaoJogador db;
    private Bundle extras;
    private Jogador jogador;
    private String picturePath;
    private String auxPath;
    private String circlePath;
    private Bitmap bitmapTop;
    private Bitmap bitmapCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_jogador);

        this.bitmapCircle = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        this.bitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.player);

        FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        this.edtNomeJogador = (EditText) findViewById(R.id.edtNomeJogador);
        this.edtTelefoneJogador = (EditText) findViewById(R.id.edtTelefoneJogador);
        this.goleiro = (CheckBox) findViewById(R.id.checkGoleiro);
        this.forcaJogador = (RatingBar) findViewById(R.id.ratingBar);
        this.imgProfile = (ImageView) findViewById(R.id.profile_id);

        this.db = new DaoJogador(this);
        this.jogador = new Jogador();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Meu jogador");

        extras = getIntent().getExtras();
        if (extras.getLong("ID")>0){
            this.jogador = db.buscarTimePorId(extras.getLong("ID"));
            this.edtNomeJogador.setText(jogador.getNome());
            this.edtTelefoneJogador.setText(jogador.getTelefone());
            this.goleiro.setChecked(jogador.isGoleiro());
            this.forcaJogador.setRating(jogador.getForca());
            if (jogador.getFotoPath()!=null){
                bitmapTop = BitmapFactory.decodeFile(jogador.getFotoPath());
                bitmapCircle = BitmapFactory.decodeFile(jogador.getCirclePath());
                this.picturePath = jogador.getFotoPath();
                this.circlePath = jogador.getCirclePath();
                this.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.imgProfile.setImageBitmap(bitmapTop);
            }
            collapsingToolbarLayout.setTitle(jogador.getNome());
        }

        this.edtNomeJogador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                collapsingToolbarLayout.setTitle(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dynamicToolbarColor();

        toolbarTextAppernce();

    }

    public void salvarJogador(){
        this.jogador.setNome(edtNomeJogador.getText().toString());
        this.jogador.setTelefone(edtTelefoneJogador.getText().toString());
        this.jogador.setGoleiro(goleiro.isChecked());
        this.jogador.setForca(forcaJogador.getRating());
        if (extras.getLong("TIME")>0){
            this.jogador.setIdTime(extras.getLong("TIME"));
        }

        if (extras.getLong("ID")>0){
            picturePath = new ImageSaver(TelaCadastroJogador.this).setFileName("topJ"+(jogador.getId())+".png").setDirectoryName("timesImg").save(bitmapTop);
            circlePath = new ImageSaver(TelaCadastroJogador.this).setFileName("circleJ"+(jogador.getId())+".png").setDirectoryName("timesImg").save(bitmapCircle);
            this.jogador.setFotoPath(picturePath);
            this.jogador.setCirclePath(circlePath);
            this.db.atualizar(jogador);
        }else {
            picturePath = new ImageSaver(TelaCadastroJogador.this).setFileName("topJ"+(db.ultimoIdTime() + 1)+".png").setDirectoryName("timesImg").save(bitmapTop);
            circlePath = new ImageSaver(TelaCadastroJogador.this).setFileName("circleJ"+(db.ultimoIdTime() + 1)+".png").setDirectoryName("timesImg").save(bitmapCircle);
            this.jogador.setFotoPath(picturePath);
            this.jogador.setCirclePath(circlePath);
            this.db.inserir(jogador);
        }
        setResult(1);
        finish();
    }

    private void dynamicToolbarColor() {

        Log.d("IMAGEM", "entrou");
        Bitmap bitmap;

        if (this.jogador.getFotoPath()!=null){
            Log.d("IMAGEM", jogador.getFotoPath());
            bitmap = BitmapFactory.decodeFile(jogador.getFotoPath());
        }else{

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player);


        }

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
                collapsingToolbarLayout.setBackgroundColor(palette.getMutedColor(R.attr.colorPrimary));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(palette.getMutedColor(R.attr.colorPrimary));
                    getWindow().setNavigationBarColor(palette.getMutedColor(R.attr.colorPrimary));
                }
            }
        });
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_cadastro_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if (id == R.id.action_save){
            if (edtNomeJogador.getText().toString().trim().length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.erro_nome_jogador)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                salvarJogador();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            auxPath = cursor.getString(columnIndex);
            cursor.close();

            if (auxPath!=null){
                this.bitmapTop = ScalingUtilities.reduzirQualidade(auxPath, "TOP");
                this.bitmapCircle = ScalingUtilities.reduzirQualidade(auxPath,"CIRCLE");
                this.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.imgProfile.setImageBitmap(bitmapTop);
                this.jogador.setFotoPath(auxPath);
                dynamicToolbarColor();
            }
        }


    }
}
