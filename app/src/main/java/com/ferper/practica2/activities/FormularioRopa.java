package com.ferper.practica2.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ferper.practica2.R;
import com.ferper.practica2.modelo.Ropa;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class FormularioRopa extends Activity implements View.OnClickListener {

    private static final String URL_SERVIDOR = "http://192.168.34.190:8082";
    EditText etNombre,etMarca,etTalla,etPrecio;
    Button btGuardar,btCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_ropa);

        etNombre = findViewById(R.id.etNombre);
        etMarca = findViewById(R.id.etMarca);
        etTalla = findViewById(R.id.etTalla);
        etPrecio = findViewById(R.id.etPrecio);
        btGuardar = findViewById(R.id.btGuardar);
        btCancelar = findViewById(R.id.btCancelar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btGuardar:
                Ropa prenda =cogerDatos();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getForObject(URL_SERVIDOR + "/nuevaprenda?nombre=" + prenda.getNombre()
                        +"&marca=" + prenda.getMarca() + "&talla=" + prenda.getTalla() + "&precio="
                        + prenda.getPrecio(),Void.class);
                break;
            case R.id.btCancelar:
                onBackPressed();
                break;
        }
    }
    public Ropa cogerDatos(){
        Ropa prenda = new Ropa();
        prenda.setNombre(String.valueOf(etNombre.getText()));
        prenda.setMarca(String.valueOf(etMarca.getText()));
        prenda.setTalla(String.valueOf(etTalla.getText()));
        prenda.setPrecio(Float.parseFloat(String.valueOf(etPrecio.getText())));
        return prenda;
    }
}
