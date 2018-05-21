package com.example.qq910.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qq910 on 2018/5/16.
 * 建议(三种建议)
 */

public class Suggestion {




    @SerializedName("comf")
    public Comfortable mComfort;

    @SerializedName("cw")
    public CarWash mCarWash;

    public Sport sport;


    public class Comfortable {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
