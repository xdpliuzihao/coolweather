package com.example.qq910.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by qq910 on 2018/5/8.
 * 市
 */

public class City extends DataSupport {
    private int id; //id是必须的
    private String cityName;//市名
    private int cityCode;//市的代号
    private int provinceId;//记录当前市所属的省id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
