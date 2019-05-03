package cl.ubiobio.weatherubb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.TubeSpeedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class Promedios extends AppCompatActivity {

    private EditText fecha;
    String fechas;
    long moveDuration = 3000;
    ArrayList<String> te= new ArrayList<String>();
    ArrayList<String> hu=new ArrayList<String>();
    ArrayList<String> ra=new ArrayList<String>();


    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promedios);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final TubeSpeedometer temperatura = findViewById(R.id.temp);
        final ProgressiveGauge humedad = findViewById(R.id.humed);
        final TubeSpeedometer radiacion = findViewById(R.id.radia);
        temperatura.setUnit("°C");
        humedad.setUnit("%RH");
        radiacion.setUnit("nm");


        temperatura.speedTo(0,0);
        humedad.speedTo(0,0);
        radiacion.speedTo(0,0);

        temperatura.setLowSpeedColor(0xFF82CA2C);
        radiacion.setLowSpeedColor(0xFF82CA2C);
        humedad.setSpeedometerColor(0xFF007BA7);
        humedad.setSpeedometerBackColor(0xFF757575);

        temperatura.setWithEffects3D(false);
        radiacion.setWithEffects3D(false);

        //ObtenerHumedadPromedio();
        //ObtenerRadiacionPromedio();
        //ObtenerTemperaturaPromedio();

        Button backBut = findViewById(R.id.button2);
        Button selBut = findViewById(R.id.button3);

        fecha = findViewById(R.id.texto);

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volver = new Intent(Promedios.this, MainActivity.class);
                startActivity(volver);
                finish();
            }
        });

        selBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG",fecha.toString());
                String allFecha = "" + fecha.getText();
                Toast error1 = Toast.makeText(getApplicationContext(), "Fecha mal ingresada. Ejemplo: 02-03-2019", Toast.LENGTH_SHORT);
                Toast error2 = Toast.makeText(getApplicationContext(), "Día, mes o año fuera de los límites", Toast.LENGTH_SHORT);

                if (allFecha.length() == 10) {
                    allFecha = allFecha.replace("-", "");
                    if (esNumero(allFecha)) {
                        if (validaRangos(allFecha)) {
                            Toast.makeText(getApplicationContext(), "Su consulta se ha ingresado", Toast.LENGTH_SHORT).show();
                            //El string allFecha ya está listo para ser ingresado a la URL de la API.
                            //Aquí van las acciones necesarias para consumir el servicio
                            fechas = allFecha;
                            System.out.println(fechas);
                            ObtenerTemperaturaPromedio();
                            ObtenerHumedadPromedio();
                            ObtenerRadiacionPromedio();
                            temperatura.speedTo(promedio(te),moveDuration);
                            humedad.speedTo(promedio(hu),moveDuration);
                            radiacion.speedTo(promedio(ra),moveDuration);
                        } else {
                            error2.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), allFecha, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error1.show();
                }


            }
        });

    }

    public static boolean esNumero(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    public static boolean validaRangos(String cadena) {
        boolean resultado;
        int dia = Integer.parseInt(cadena.substring(0,2));
        int mes = Integer.parseInt(cadena.substring(2,4));
        int anio = Integer.parseInt(cadena.substring(4));

        java.util.Date fecha = new Date();

        if((dia>0&&dia<=31)&&(mes>0&&mes<=12)&&(anio>=2018&&anio<=2019)){
             resultado =true;
        }else{
            resultado = false;
        }
        return resultado;
    }

    private void ObtenerRadiacionPromedio() {
        String T_url = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/8IvrZCP3qa/"+fechas;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, T_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                ra.add(valor);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(request);
    }

    private void ObtenerHumedadPromedio() {
        String T_url = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/VIbSnGKyLW/" + fechas;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, T_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                float value=Float.parseFloat(valor);
                                hu.add(valor);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(request);
    }

    private void ObtenerTemperaturaPromedio() {
        String T_url = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/E1yGxKAcrg/" + fechas;
        Log.d("LOG", T_url);
        StringRequest request = new StringRequest(Request.Method.GET, T_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("LOG", response);
                            JSONObject res = new JSONObject(response);

                            for (int i = 0; i < res.getJSONArray("data").length(); i++) {
                                JSONObject jsonObject = res.getJSONArray("data").getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                te.add(valor);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(request);

    }
    public float promedio(ArrayList<String>lista){
        Log.d("LOG", lista.toString());
        float arreglo[]=new float[lista.size()] ;
        float contador=0;
        float promedio=0;
        if(lista.size()>0){
            for (int i=0;i<lista.size();i++){
                String value=lista.get(i);
                arreglo[i]=Float.parseFloat(value);
            }
            for (int i = 0; i<arreglo.length ; i++) {
                contador+=arreglo[i];
            }
            promedio=contador/arreglo.length;
        }
        return promedio;
    }
}
