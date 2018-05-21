package com.example.qq910.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qq910 on 2018/5/16.
 * 天气
 */

public class Weather {
    public String status;
    public AQI aqi;
    public Basic basic;
    @SerializedName("daily_forecast")
    public List<Forecast> mForecastsList;
    public Now now;
    public Suggestion suggestion;



//    public String status;
//    public AQI aqi;
//    public Basic basic;
//    @SerializedName("daily_forecast")
//    public List<Forecast> mForecastsList;
//    public Now now;
//    public Suggestion suggestion;
}
