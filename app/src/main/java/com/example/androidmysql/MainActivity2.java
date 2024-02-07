package com.example.androidmysql;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtTelefono;
    private EditText txtPass;
    private TextView tvId;
    private String id;
    private String ip = Ip.ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtPass = findViewById(R.id.txtPass);
        tvId = findViewById(R.id.tvId);

        id = getIntent().getStringExtra("id");
        tvId.setText(id);

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+ip+"/android_mysql/registro.php?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        txtNombre.setText(response.getString("nombre"));
                        txtEmail.setText(response.getString("email"));
                        txtTelefono.setText(response.getString("telefono"));
                        txtPass.setText(response.getString("pass"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity2.this, error.toString(), Toast.LENGTH_LONG).show()
        );

        queue.add(jsonObjectRequest);
    }

    public void clickRegresar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickBorrar(View view) {
        String url = "http://"+ip+"/android_mysql/borrar.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest resultadoPost = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(MainActivity2.this, "El usuario se borró de forma exitosa", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(MainActivity2.this, "Error al borrar el usuario " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", id);
                return parametros;
            }
        };

        queue.add(resultadoPost);
    }

    public void clickEditar(View view) {
        String url = "http://"+ip+"/android_mysql/editar.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest resultadoPost = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(MainActivity2.this, "El usuario ha sido editado de forma exitosa", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(MainActivity2.this, "Error al editar el usuario " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", id);
                parametros.put("nombre", txtNombre.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("email", txtEmail.getText().toString());
                parametros.put("pass", txtPass.getText().toString());
                return parametros;
            }
        };

        queue.add(resultadoPost);
    }
}
