package com.atapps.gerenciadortimes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atapps.gerenciadortimes.R;
import com.atapps.gerenciadortimes.dao.DaoJogador;
import com.atapps.gerenciadortimes.dao.DaoTime;
import com.atapps.gerenciadortimes.domain.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 05/03/2016.
 */
public class ListTimeAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Time> times;
    private DaoJogador db;

    public ListTimeAdapter(Context context, List<Time> times){
        this.inflater = LayoutInflater.from(context);
        this.times = times;
        if (times == null){
            this.times = new ArrayList<Time>();
        }
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Time getItem(int position) {
        return times.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Time time = times.get(position);

        convertView = inflater.inflate(R.layout.item_list_time,null);

        db = new DaoJogador(convertView.getContext());

        TextView txtNomeTime = (TextView) convertView.findViewById(R.id.txtNomeTime);
        TextView txtDiaTime = (TextView) convertView.findViewById(R.id.txtDiaTime);
        TextView txtQtdJogadores = (TextView) convertView.findViewById(R.id.txtQtdJogadores);
        ImageView imgItemList = (ImageView) convertView.findViewById(R.id.list_image);

        txtNomeTime.setText(time.getNome());
        txtDiaTime.setText(time.getDia() + " " + time.getHora());
        txtQtdJogadores.setText("("+db.buscar(time.getId()).size()+")");

        if (time.getCirclePath()!=null){
            //Log.d("IMAGENOME",time.getId() + "\n " + time.getNome() + "\n " + time.getCirclePath() + "\n " + time.getFotoPath());
            Bitmap bitmap = BitmapFactory.decodeFile(time.getCirclePath());
            imgItemList.setImageBitmap(bitmap);
        }

        return convertView;
    }
}
