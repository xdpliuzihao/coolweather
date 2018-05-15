package com.example.qq910.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by qq910 on 2018/5/14.
 */

public class Province extends DataSupport{
    private String provinceName;
    private int id;
    private int provinceCode;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
