package com.example.qq910.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qq910 on 2018/5/16.
 * 当前天气的信息
 */

public class Now {



    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
