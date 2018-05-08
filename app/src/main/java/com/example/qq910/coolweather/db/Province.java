package com.example.qq910.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by qq910 on 2018/5/8.
 * 省
 */

public class Province extends DataSupport {
    private int id; //id是必须的
    private String provinceName;//省名
    private int provinceCode;//省的代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
