package com.example.qq910.coolweather.gson;

/**
 * Created by qq910 on 2018/5/16.
 * 空气质量
 */

public class AQI {



    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
