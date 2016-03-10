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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.dao.DaoTime;
import com.atapps.gerenciadortimes.domain.Time;
import com.atapps.gerenciadortimes.utils.ImageSaver;
import com.atapps.gerenciadortimes.utils.ScalingUtilities;
import com.atapps.gerenciadortimes.utils.SetTime;

public class TelaCadastroTime extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private EditText edtNomeTime;
    private EditText edtDiaTime;
    private EditText edtHoraTime;
    private EditText edtLocalTime;
    private ImageView imgProfile;
    private DaoTime db;
    private Bundle extras;
    private Time time;
    private String picturePath;
    private String auxPath;
    private String circlePath;
    private Bitmap bitmapTop;
    private Bitmap bitmapCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_time);

        this.bitmapCircle = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        this.bitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.campo);

        FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        this.edtNomeTime = (EditText) findViewById(R.id.edtNomeTime);
        this.edtDiaTime = (EditText) findViewById(R.id.edtDiaTime);
        this.edtHoraTime = (EditText) findViewById(R.id.edtHoraTime);
        this.edtLocalTime = (EditText) findViewById(R.id.edtLocalTime);
        this.imgProfile = (ImageView) findViewById(R.id.profile_id);


        SetTime fromTime = new SetTime(edtHoraTime, this);

        this.db = new DaoTime(this);
        this.time = new Time();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Meu time");

        extras = getIntent().getExtras();
        if (extras!=null){
            this.time = db.buscarTimePorId(extras.getLong("ID"));
            this.edtNomeTime.setText(time.getNome());
            this.edtDiaTime.setText(time.getDia());
            this.edtHoraTime.setText(time.getHora());
            this.edtLocalTime.setText(time.getLocal());
            if (time.getFotoPath()!=null){
                bitmapTop = BitmapFactory.decodeFile(time.getFotoPath());
                bitmapCircle = BitmapFactory.decodeFile(time.getCirclePath());
                this.picturePath = time.getFotoPath();
                this.circlePath = time.getCirclePath();
                this.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.imgProfile.setImageBitmap(bitmapTop);
            }
            collapsingToolbarLayout.setTitle(time.getNome());
        }

        this.edtNomeTime.addTextChangedListener(new TextWatcher() {
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
    public void salvarTime(){
        this.time.setNome(edtNomeTime.getText().toString());
        this.time.setDia(edtDiaTime.getText().toString());
        this.time.setHora(edtHoraTime.getText().toString());
        this.time.setLocal(edtLocalTime.getText().toString());

        if (extras!=null){
            picturePath = new ImageSaver(TelaCadastroTime.this).setFileName("top"+(time.getId())+".png").setDirectoryName("timesImg").save(bitmapTop);
            circlePath = new ImageSaver(TelaCadastroTime.this).setFileName("circle"+(time.getId())+".png").setDirectoryName("timesImg").save(bitmapCircle);
            this.time.setFotoPath(picturePath);
            this.time.setCirclePath(circlePath);
            this.db.atualizar(time);
        }else {
            picturePath = new ImageSaver(TelaCadastroTime.this).setFileName("top"+(db.ultimoIdTime() + 1)+".png").setDirectoryName("timesImg").save(bitmapTop);
            circlePath = new ImageSaver(TelaCadastroTime.this).setFileName("circle"+(db.ultimoIdTime() + 1)+".png").setDirectoryName("timesImg").save(bitmapCircle);
            this.time.setFotoPath(picturePath);
            this.time.setCirclePath(circlePath);
            this.db.inserir(time);
        }
        setResult(1);
        finish();
    }

    private void dynamicToolbarColor() {

        Log.d("IMAGEM", "entrou");
        Bitmap bitmap;

        if (this.time.getFotoPath()!=null){
            Log.d("IMAGEM",time.getFotoPath());
            bitmap = BitmapFactory.decodeFile(time.getFotoPath());
        }else{

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);


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
            if (edtNomeTime.getText().toString().trim().length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.erro_nome_time)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                salvarTime();
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
                this.bitmapTop = ScalingUtilities.reduzirQualidade(auxPath,"TOP");
                this.bitmapCircle = ScalingUtilities.reduzirQualidade(auxPath,"CIRCLE");
                this.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.imgProfile.setImageBitmap(bitmapTop);
                this.time.setFotoPath(auxPath);
                dynamicToolbarColor();
            }
        }


    }

}


