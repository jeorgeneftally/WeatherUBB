package cl.ubiobio.weatherubb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.TubeSpeedometer;

import java.util.Date;

public class Promedios extends AppCompatActivity {

    private EditText fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promedios);

        TubeSpeedometer temperatura = findViewById(R.id.temp);
        ProgressiveGauge humedad = findViewById(R.id.humed);
        TubeSpeedometer radiacion = findViewById(R.id.radia);
        temperatura.setUnit("°C");
        humedad.setUnit("%RH");
        radiacion.setUnit("nm");


        temperatura.speedTo(25);
        humedad.speedTo(95);
        radiacion.speedTo(300);

        temperatura.setLowSpeedColor(0xFF82CA2C);
        radiacion.setLowSpeedColor(0xFF82CA2C);
        humedad.setSpeedometerColor(0xFF007BA7);
        humedad.setSpeedometerBackColor(0xFF757575);

        temperatura.setWithEffects3D(false);
        radiacion.setWithEffects3D(false);


        Button backBut = findViewById(R.id.button2);
        Button selBut = findViewById(R.id.button3);

        fecha = findViewById(R.id.texto);

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                Intent volver = new Intent(Promedios.this, MainActivity.class);
                startActivity(volver);
                finish();
            }
        });

        selBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
