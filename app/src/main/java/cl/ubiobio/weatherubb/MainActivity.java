package cl.ubiobio.weatherubb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    long moveDuration=1000;
    Date date =new Date();
    String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date()).replace("/","").trim();
    ArrayList<String> t= new ArrayList<String>();
    ArrayList<String> h=new ArrayList<String>();
    ArrayList<String> r=new ArrayList<String>();
    private RequestQueue requestQueue;

    int c1=0;
    int c2=0;
    int c3=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final TubeSpeedometer temperatura = findViewById(R.id.temp);
        final ProgressiveGauge humedad=findViewById(R.id.humed);
        final TubeSpeedometer radiacion  = findViewById(R.id.radia);
        temperatura.setUnit("°C");
        humedad.setUnit("%RH");
        radiacion.setUnit("nm");



        temperatura.setLowSpeedColor(0xFF82CA2C);
        radiacion.setLowSpeedColor(0xFF82CA2C);
        humedad.setSpeedometerColor(0xFF007BA7);
        humedad.setSpeedometerBackColor(0xFF757575);

        temperatura.setWithEffects3D(false);
        radiacion.setWithEffects3D(false);

        Button actualizar=findViewById(R.id.button1);
        Button promBut= findViewById(R.id.button2);

        temperatura.speedTo(0,0);
        humedad.speedTo(0,0);
        radiacion.speedTo(0,0);

        ObtenerTemperatura();
        ObtenerHumedad();
        ObtenerRadiacion();

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    temperatura.speedTo(0,0);
                    humedad.speedTo(0,0);
                    radiacion.speedTo(0,0);
                    temperatura.speedTo(valor_actual(t), moveDuration);
                    humedad.speedTo(valor_actual(h), moveDuration);
                    radiacion.speedTo(valor_actual(r), moveDuration);

            }
        });

        promBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iniciarPromedios= new Intent(MainActivity.this, Promedios.class);
                startActivity(iniciarPromedios);
                finish();
            }
        });

    }

    //Métodos

    private void ObtenerRadiacion() {
        String T_url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/8IvrZCP3qa/"+fecha;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, T_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                r.add(valor);

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
        ); requestQueue.add(request);
        c1++;
    }

    private void ObtenerHumedad() {
        String T_url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/VIbSnGKyLW/"+fecha;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, T_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                h.add(valor);

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
        ); requestQueue.add(request);
        c2++;
    }

    private void ObtenerTemperatura() {
        //Toast.makeText(getApplicationContext(), "estoy en el método T°", Toast.LENGTH_SHORT).show();
        String T_url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/3lg0M7rq2E/E1yGxKAcrg/"+fecha;
        Log.d("LOG", T_url);
        StringRequest request= new StringRequest(Request.Method.GET, T_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("LOG", response);
                            JSONObject res = new JSONObject(response);

                            for (int i = 0; i < res.getJSONArray("data").length(); i++) {
                                JSONObject jsonObject = res.getJSONArray("data").getJSONObject(i);
                                String valor = jsonObject.getString("valor");
                                t.add(valor);
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
        c3++;
    }


    public float valor_actual(ArrayList<String> lista){
        //Toast.makeText(getApplicationContext(), "estoy en el método valor actual", Toast.LENGTH_SHORT).show();
        Log.d("LOG", lista.toString());
        float actual=0;
        if(lista.size() > 0){
            float array2[]=new float[lista.size()];

            for (int i = 0; i <lista.size() ; i++) {
                array2[i]=Float.parseFloat(lista.get(i));
                System.out.println(array2[i]);
            }

            actual=array2[array2.length - 1];
        }



        return actual;
    }
}



