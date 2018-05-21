package com.example.qq910.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qq910 on 2018/5/16.
 * 天气预报
 */

public class Forecast {



    public String date;

    @SerializedName("cond")
    public More more;
     public class More{
         @SerializedName("txt_d")
         public String info;
     }

     @SerializedName("tmp")
     public Temperature mTemperature;
     public class Temperature{
         public String max;
         public String min;
     }
}
