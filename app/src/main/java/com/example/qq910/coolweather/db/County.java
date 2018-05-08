package com.example.qq910.coolweather.db;

/**
 * Created by qq910 on 2018/5/8.
 * 县
 */

public class County {
    private int id;
    private String countyName;  //县名字
    private String weatherId;   //县的天气id
    private int cityId; //当前市的id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
