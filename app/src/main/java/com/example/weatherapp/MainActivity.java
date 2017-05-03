package com.example.weatherapp;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, updatedField;
    ImageView imgIcon;
    private float mLatitude = 35.02f, mLongitude = 139.01f;
    Typeface weatherFont;
    private static final String[] FINE_LOCATION_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String[] INTERNET_PERMISSION = {Manifest.permission.INTERNET};
    private static final int FINE_LOATION_REQUEST = 1337;
    private static final int INTERNET_REQUEST = 1338;
    private RequestQueue mRequestQueue;
    private static final String OPEN_WEATHER_MAP_URL ="http://api.openweathermap.org/data/2.5/weather?q=Moscow&appid=c1adf1f6a33b521bd299497be91e18bb";
            //"http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=c1adf1f6a33b521bd299497be91e18bb";
    private static final String OPEN_WEATHER_MAP_API = "c1adf1f6a33b521bd299497be91e18bb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the toolbar as the app bar for the activity
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        requestPermissions(FINE_LOCATION_PERMISSION, FINE_LOATION_REQUEST);
        requestPermissions(INTERNET_PERMISSION, INTERNET_REQUEST);



        //weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        initComponents();

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, OPEN_WEATHER_MAP_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject details = response.getJSONArray("weather").getJSONObject(0);
                            JSONObject main = response.getJSONObject("main");
                            DateFormat df = DateFormat.getDateTimeInstance();


                            String city = response.getString("name").toUpperCase(Locale.getDefault()) + ", " + response.getJSONObject("sys").getString("country");
                            Toast.makeText(MainActivity.this, city, Toast.LENGTH_LONG).show();
                            String description = details.getString("description").toUpperCase(Locale.getDefault());
                            String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                            String humidity = main.getString("humidity") + "%";
                            String pressure = main.getString("pressure") + " hPa";
                            String updatedOn = df.format(new Date(response.getLong("dt") * 1000));
                            String iconText = setWeatherIcon(details.getInt("id"),
                                    response.getJSONObject("sys").getLong("sunrise") * 1000,
                                    response.getJSONObject("sys").getLong("sunset") * 1000);


                            setData(city, description, temperature, humidity, pressure, updatedOn, setWeatherIcon(description));
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vParams = new HashMap<>();
                vParams.put("city", "Johannesburg");

                return super.getParams();
            }
        };

        mRequestQueue.add(jsonObjectRequest);

    }

    public void initComponents() {
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        imgIcon = (ImageView)findViewById(R.id.img_icon);
    }

    public static String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch (id) {
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xf01c;";
                    break;
                case 7:
                    icon = "&#xf014;";
                    break;
                case 8:
                    icon = "&#xf013;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    public void setData(String city, String description, String temperature, String humidity, String pressure, String updatedOn, int resourceId) {
        cityField.setText(city);
        updatedField.setText(updatedOn);
        detailsField.setText(description);
        currentTemperatureField.setText(temperature);
        humidity_field.setText(humidity);
        pressure_field.setText(pressure);
        imgIcon.getLayoutParams().height = 250;
        imgIcon.getLayoutParams().width = 250;
        imgIcon.setImageResource(resourceId);

    }

    public int setWeatherIcon(String weather) {
        switch (weather) {
            case "LIGHT RAIN" :
                return R.drawable.rainy_day;

            case "CLEAR SKY" :
                return R.drawable.clear_day;

            case "THUNDERSTORM WITH LIGHT RAIN":

               return R.drawable.storm_weather;

            case "THUNDERSTORM WITH RAIN":

                return R.drawable.storm_weather;

            case "SHOWER RAIN AND DRIZZLE":

                return R.drawable.storm_weather;

            case "lIGHT RAIN":

                return R.drawable.storm_weather;

            case "SNOW":

                return R.drawable.snow_weather;

            case "OVERCAST CLOUDS":

                return R.drawable.partly_cloudy;

            case "LIGHT RAIN AND SNOW":

                return R.drawable.rain_snow;

            case "HAZE":

                return R.drawable.haze_weather;

            case "FEW CLOUDS":

                return R.drawable.cloudy_weather;


            case "WINDY":

                return R.drawable.windy_weather;


            default:
                return R.drawable.unknown;
        }
    }
}


