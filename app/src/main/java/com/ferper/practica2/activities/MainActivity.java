package com.ferper.practica2.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ferper.practica2.R;
import com.ferper.practica2.activities.FormularioRopa;
import com.ferper.practica2.adapters.RopaAdapter;
import com.ferper.practica2.modelo.Ropa;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView lvPrendas;
    RopaAdapter adapter;
    ArrayList<Ropa> prendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prendas =new ArrayList<>();
        lvPrendas = findViewById(R.id.lvPrendas);
        adapter = new RopaAdapter(this, R.layout.item_prenda, prendas);
        lvPrendas.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_nueva_prenda :
                Intent intent = new Intent(this, FormularioRopa.class);
                startActivity(intent);

                return true;
            case R.id.menu_preferencias :
                return true;
            case R.id.menu_about :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Version 1.0 de TuArmario\n" +
                        "Desarrollado por Fernando Perez de la Torre Muñoz\n" +
                        "Febrero de 2019")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
    private class DescargaDatos extends AsyncTask<String,Void,Void>{
        private ProgressDialog dialog;
        private String resultado;

        @Override
        protected Void doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String linea = null;
                while((linea = br.readLine()) != null){
                    sb.append(linea + "\n");
                }
                conexion.disconnect();
                br.close();
                resultado = sb.toString();
                try{
                    JSONObject json = new JSONObject(resultado);
                    for(int i = 0; i  < ){

                    }
                }catch(JSONException jse){
                    jse.printStackTrace();
                }
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}
