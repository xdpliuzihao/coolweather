package com.example.qq910.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qq910 on 2018/5/16.
 * 城市的基本信息
 */

public class Basic {




    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update {
        @SerializedName("loc")
        public String updateTime;

    }
}
