package com.example.androidmysql;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtTelefono;
    private EditText txtPass;
    private TableLayout tbUsuarios;
    private String idGlobal;
    private String ip = Ip.ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtPass = findViewById(R.id.txtPass);
        tbUsuarios = findViewById(R.id.tbUsuarios);

        cargaTabla();
    }

    public void cargaTabla() {
        tbUsuarios.removeAllViews();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+ip+"/android_mysql/registros.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            View registro = LayoutInflater.from(MainActivity.this).inflate(R.layout.table_row, null, false);
                            TextView colNombre = registro.findViewById(R.id.colNombre);
                            TextView colEmail = registro.findViewById(R.id.colEmail);
                            View colEditar = registro.findViewById(R.id.colEditar);
                            View colBorrar = registro.findViewById(R.id.colBorrar);

                            colNombre.setText(jsonObject.getString("nombre"));
                            colEmail.setText(jsonObject.getString("email"));
                            colEditar.setId(jsonObject.getInt("id"));
                            colBorrar.setId(jsonObject.getInt("id"));

                            tbUsuarios.addView(registro);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        queue.add(jsonObjectRequest);
    }

    public void clickTablaEditar(View view) {
        Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
        idGlobal = String.valueOf(view.getId());
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+ip+"/android_mysql/registro.php?id=" + idGlobal;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        txtNombre.setText(response.getString("nombre"));
                        txtEmail.setText(response.getString("email"));
                        txtTelefono.setText(response.getString("telefono"));
                        txtPass.setText(response.getString("pass"));
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()
        );
        queue.add(jsonObjectRequest);
    }

    public void clickGuardaEditar(View view) {
        String url = "http://"+ip+"/android_mysql/editar.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest resultadoPost = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(MainActivity.this, "El usuario ha sido editado de forma exitosa", Toast.LENGTH_LONG).show();
                    cargaTabla();
                },
                error -> Toast.makeText(MainActivity.this, "Error al editar el usuario " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", idGlobal);
                parametros.put("nombre", txtNombre.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("email", txtEmail.getText().toString());
                parametros.put("pass", txtPass.getText().toString());
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    public void clickTablaBorrar(View view) {
        Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
        String url = "http://"+ip+"/android_mysql/borrar.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest resultadoPost = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(MainActivity.this, "El usuario se borró exitosamente", Toast.LENGTH_LONG).show();
                    cargaTabla();
                },
                error -> Toast.makeText(MainActivity.this, "Error al borrar el usuario " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", String.valueOf(view.getId()));
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    public void clickBtnInsertar(View view) {
        String url = "http://"+ip+"/android_mysql/insertar.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest resultadoPost = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(MainActivity.this, "Usuario insertado exitosamente", Toast.LENGTH_LONG).show();
                    cargaTabla();
                },
                error -> Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", txtNombre.getText().toString());
                parametros.put("email", txtEmail.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("pass", txtPass.getText().toString());
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    public void cickVer(View view) {
        EditText txtId = findViewById(R.id.txtId);
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("id", txtId.getText().toString());
        startActivity(intent);
    }

    public void clickReset(View view) {
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtPass.setText("");
    }
}
