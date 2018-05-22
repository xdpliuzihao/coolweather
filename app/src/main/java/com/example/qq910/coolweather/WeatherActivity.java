package com.example.qq910.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qq910.coolweather.gson.Forecast;
import com.example.qq910.coolweather.gson.Weather;
import com.example.qq910.coolweather.util.HttpUtil;
import com.example.qq910.coolweather.util.Utillty;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气界面
 */

public class WeatherActivity extends AppCompatActivity {


    private ScrollView mWeatherLayout;  //整个天气布局
    private TextView mTitleCity;    //标题处的城市明珠
    private TextView mTitleUpdateTime;  //标题处的更新时间
    private TextView mDegreeText;   //当前温度
    private TextView mWeatherInfoText;  //当前天气状况
    private LinearLayout mForecastLayout;   //天气预报布局
    private TextView mAqiText;  //空气质量
    private TextView mPm25Text; //空气pm2.5
    private TextView mComfortText;  //当前舒适度
    private TextView mCarWashText;  //当前是否适合洗车
    private TextView mSportText;    //当前是否适合运动

    private TextView mDateText;
    private TextView mInfoText;
    private TextView mMaxText;
    private TextView mMinText;
    private ImageView mBingPicImg;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private String mWeatherId;
    private Button mBtnBack;
    public DrawerLayout mDrawerLayoutS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 21) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        initData();
    }

    private void initView() {
        mWeatherLayout = findViewById(R.id.weather_layout);
        mTitleCity = findViewById(R.id.title_city);
        mTitleUpdateTime = findViewById(R.id.title_update_time);
        mDegreeText = findViewById(R.id.degree_text);
        mWeatherInfoText = findViewById(R.id.weather_info_text);
        mForecastLayout = findViewById(R.id.forecast_layout);
        mAqiText = findViewById(R.id.aqi_text);
        mPm25Text = findViewById(R.id.pm25_text);
        mComfortText = findViewById(R.id.comfort_text);
        mCarWashText = findViewById(R.id.car_wash_text);
        mSportText = findViewById(R.id.sport_text);
        mBingPicImg = findViewById(R.id.iv_bing_pic_img);
        mSwipeRefreshLayout = findViewById(R.id.swip_refresh);

        mBtnBack = findViewById(R.id.btn_back);
        mDrawerLayoutS = findViewById(R.id.drawer_layout);
    }


    private void initData() {
        //设置进度条颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);    //获取天气信息
        //如果不等于空表示有缓存数据
        if (weatherString != null) {
            //有缓存时直接解析天气数据,直接展示天气信息
            Weather weather = Utillty.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;///////////////////////////////////////////////没看懂要这段有什么用/////////////删掉试试
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气   第一次进入都是没有缓冲数据的
            mWeatherId = getIntent().getStringExtra("weather_id");
            mWeatherLayout.setVisibility(View.INVISIBLE);//让他看不见    //这里不知道为什么要隐藏  等写完运行下再考虑   ///////////////////////////////////////////////
            requestWeather(mWeatherId);//通过id从服务器获取天气信息
        }

        //刷新的数据是新数据,SharedPreferences将没有缓存,所以需要重新通过id来从服务器读取数据,下次进入就有缓存了
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        String bingPic = sharedPreferences.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPicImg);
        } else {
            loadBingPic();
        }

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayoutS.openDrawer(GravityCompat.START);
            }
        });


    }

    private void loadBingPic() {
        String address = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responsePic = response.body().string();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("bing_pic", responsePic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(responsePic).into(mBingPicImg);
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });


    }


    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=f44246d7b5c24e8dbcec889e49eed3b8";
        HttpUtil.sendOkhttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                //解析网络的时候耗时操作,okhttp都是在子线程执行,执行完后需要回到主线程对ui进行操作
                final Weather weather = Utillty.handleWeatherResponse(responseText);    //json数据转换成Weather对象
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果解析后的内容有数据并且是ok的,就通过SharedPreferences的edit方法把数据保存进去
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //向键为weather的edit存放responseText响应内容
                            edit.putString("weather", responseText);//putString:键值对; weather实体类,对应weatherUrl返回的数据
                            edit.apply();//根据上面内容将数据缓存到sharedpreferences中,然后进行提交
                            mWeatherId = weather.basic.weatherId;//记录城市id/////////////////////////////////////////////////
                            showWeatherInfo(weather);   //展示数据
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);


                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

        loadBingPic();
    }

    /**
     * 处理并展示weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;  //解析城市名
        String updateTime = weather.basic.update.updateTime.split(" ")[1];//解析更新时间  splite拆分字符串
        String degree = weather.now.temperature + "℃";   //当前度数
        String weatherInfo = weather.now.more.info;//当前天气状态
        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);
        mForecastLayout.removeAllViews();//清空视图
        for (Forecast forecast : weather.mForecastsList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            mDateText = view.findViewById(R.id.date_text);  //当前日期
            mInfoText = view.findViewById(R.id.info_text);  //当前天气状况
            mMaxText = view.findViewById(R.id.max_text);    //最高温度
            mMinText = view.findViewById(R.id.min_text);    //最低温度
            mDateText.setText(forecast.date);
            mInfoText.setText(forecast.more.info);
            mMaxText.setText(forecast.mTemperature.max);
            mMinText.setText(forecast.mTemperature.min);
            mForecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度: " + weather.suggestion.mComfort.info;
        String carWash = "洗车指数: " + weather.suggestion.mCarWash.info;
        String sport = "运动建议: " + weather.suggestion.sport.info;
        mComfortText.setText(comfort);
        mCarWashText.setText(carWash);
        mSportText.setText(sport);
        mWeatherLayout.setVisibility(View.VISIBLE); //设置完数据  让他重新显示


    }


}
