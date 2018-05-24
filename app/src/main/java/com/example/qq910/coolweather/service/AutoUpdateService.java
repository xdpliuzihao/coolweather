package com.example.qq910.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.example.qq910.coolweather.WeatherActivity;
import com.example.qq910.coolweather.gson.Weather;
import com.example.qq910.coolweather.util.HttpUtil;
import com.example.qq910.coolweather.util.Utillty;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int timeS = 8 * 60 * 60 * 1000;
        long LTime = SystemClock.elapsedRealtime() + timeS;
        //指定服务为当前服务
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,LTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    //更新天气信息
    private void updateWeather() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = sharedPreferences.getString("weather", null);
        if (weatherStr != null) {
            Weather weather = Utillty.handleWeatherResponse(weatherStr);
            final String weatherId = weather.basic.weatherId;
            String address = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=f44246d7b5c24e8dbcec889e49eed3b8";
            HttpUtil.sendOkhttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String stringResponse = response.body().string();
                    //下次试试把这里的weather改下名字看下和上面的weather不同能不能通得过
                    Weather weather = Utillty.handleWeatherResponse(stringResponse);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        edit.putString("weather",stringResponse);
                        edit.apply();
                    }

                }
            });

        }

    }

    //更新图片
    private void updateBingPic() {
        String address = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();
            }
        });

    }


}
