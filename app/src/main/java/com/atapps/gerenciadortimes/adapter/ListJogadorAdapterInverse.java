package com.atapps.gerenciadortimes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.dao.DaoJogador;
import com.atapps.gerenciadortimes.domain.Jogador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 09/03/2016.
 */
public class ListJogadorAdapterInverse extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Jogador> jogadores;
    private DaoJogador db;

    public ListJogadorAdapterInverse(Context context, List<Jogador> jogadores){
        this.inflater = LayoutInflater.from(context);
        this.jogadores = jogadores;
        if (jogadores == null){
            this.jogadores = new ArrayList<Jogador>();
        }
    }

    @Override
    public int getCount() {
        return jogadores.size();
    }

    @Override
    public Jogador getItem(int position) {
        return jogadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Jogador jogador = jogadores.get(position);

        convertView = inflater.inflate(R.layout.item_list_jogador_inverse,null);

        TextView txtNomeJogador = (TextView) convertView.findViewById(R.id.txtNomeJogador);
        TextView txtPosicao = (TextView) convertView.findViewById(R.id.txtPosicao);
        RatingBar forca = (RatingBar) convertView.findViewById(R.id.ratingJogador);
        ImageView imgItemList = (ImageView) convertView.findViewById(R.id.list_image);


        txtNomeJogador.setText(jogador.getNome());
        forca.setRating(jogador.getForca());
        if (jogador.isGoleiro()){
            txtPosicao.setText("G");
        }else{
            txtPosicao.setText("L");
        }


        if (jogador.getCirclePath()!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(jogador.getCirclePath());
            imgItemList.setImageBitmap(bitmap);
        }
        Log.d("VELOCIDADE","ADAPTER2 " + position + "\n" +
                " " + jogadores.get(position).getId() + "\n" +
                " " + jogadores.get(position).getNome());

        return convertView;
    }
}
