package com.example.qq910.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //从缓存中判断,看是否存在数据,如果存在数据表示之前请求过天气数据,就比没药再次选择城市
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }


//        //从数据库中取出天气信息,如果有天气,不用重新获取,直接跳转到WeatherActivity
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sharedPreferences.getString("weather", null) != null) {
//            Intent intent = new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }
}
