package com.ferper.practica2.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ferper.practica2.R;
import com.ferper.practica2.activities.FormularioRopa;
import com.ferper.practica2.adapters.RopaAdapter;
import com.ferper.practica2.modelo.Ropa;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvPrendas;
    RopaAdapter adapter;
    Button btAbrirMapa;
    boolean modoNoche,modoGrande;
    private ArrayList<Ropa> prendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prendas =new ArrayList<>();
        lvPrendas = findViewById(R.id.lvPrendas);
        adapter = new RopaAdapter(this, R.layout.item_prenda, prendas);
        lvPrendas.setAdapter(adapter);

        btAbrirMapa = findViewById(R.id.btAbrirMapa);
        btAbrirMapa.setOnClickListener(this);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        modoNoche = preferencias.getBoolean("opcion_modo_noche",false);
        UiModeManager modeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        View view = this.getWindow().getDecorView();
        if (modoNoche)
            view.setBackgroundColor(Color.BLACK);
        else
            view.setBackgroundColor(Color.WHITE);


        DescargaDatos descargaDatos = new DescargaDatos();
        descargaDatos.execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        DescargaDatos descargaDatos = new DescargaDatos();
        descargaDatos.execute();
        UiModeManager modeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        View view = this.getWindow().getDecorView();
        if (modoNoche)
            view.setBackgroundColor(Color.BLACK);
        else
            view.setBackgroundColor(Color.WHITE);    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nueva_prenda:
                Intent intent = new Intent(this, FormularioRopa.class);
                startActivity(intent);

                return true;
            case R.id.menu_preferencias:
                Intent intent1 = new Intent(this,Preferencias.class);
                startActivity(intent1);
                return true;
            case R.id.menu_about:
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btAbrirMapa:
                Intent intent = new Intent(this,MapaActivity.class);
                startActivity(intent);
                break;

        }
    }

    class DescargaDatos extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prendas.clear();
        }

        @Override
        protected Void doInBackground(String... strings) {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Ropa[] prendasServer = restTemplate.getForObject("http://10.0.2.2:8082"
                    + "/armario",Ropa[].class);
            prendas.addAll(Arrays.asList(prendasServer));
            return null;
        }


    }
}
