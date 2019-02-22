package com.ferper.practica2.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ferper.practica2.R;
import com.ferper.practica2.modelo.Ropa;

import java.util.List;

public class RopaAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int idLayout;
    private List<Ropa> prendas;

    public RopaAdapter(Context context, int idLayout, List<Ropa> prendas){
        inflater = LayoutInflater.from(context);
        this.idLayout = idLayout;
        this.prendas = prendas;
    }

    static class ViewHolder{
        TextView tvNombre;
        TextView tvMarca;
        TextView tvPrecio;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(idLayout,null);

            holder = new ViewHolder();
            holder.tvNombre = view.findViewById(R.id.tvNombre);
            holder.tvMarca = view.findViewById(R.id.tvMarca);
            holder.tvPrecio = view.findViewById(R.id.tvPrecio);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Ropa prenda = prendas.get(i);
        holder.tvNombre.setText(prenda.getNombre());
        holder.tvMarca.setText(prenda.getMarca());
        holder.tvPrecio.setText(String.valueOf(prenda.getPrecio()));

        return view;
    }

    @Override
    public int getCount() { return prendas.size(); }

    @Override
    public Object getItem(int i) {return prendas.get(i); }

    @Override
    public long getItemId(int i) { return prendas.get(i).getId(); }
}
