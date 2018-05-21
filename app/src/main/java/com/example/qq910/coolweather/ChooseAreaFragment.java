package com.example.qq910.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qq910.coolweather.db.City;
import com.example.qq910.coolweather.db.County;
import com.example.qq910.coolweather.db.Province;
import com.example.qq910.coolweather.util.HttpUtil;
import com.example.qq910.coolweather.util.Utillty;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by qq910 on 2018/5/15.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private int currentLevel;//当前级别
    private TextView mTitleText;
    private Button mBackButton;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provincesList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitleText = view.findViewById(R.id.title_text);
        mBackButton = view.findViewById(R.id.back_button);
        mListView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provincesList.get(position);
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounty();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }




//
//                else if (currentLevel == LEVEL_COUNTY){
//                    String weatherId = countyList.get(position).getWeatherId();
//                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
//                    intent.putExtra("weather_id",weatherId);
//                    startActivity(intent);
//                    getActivity().finish();
//                }

                mBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentLevel == LEVEL_COUNTY) {
                            queryCity();
                        } else if (currentLevel == LEVEL_CITY) {
                            queryProvince();
                        }
                    }
                });
            }
        });
        queryProvince();//////////////如果把它放到最前面 看下还行不行
    }

    //查询省数据,先数据库查询,没找到再去服务器查询
    private void queryProvince() {
        mTitleText.setText("中国");
        mBackButton.setVisibility(View.GONE);
        provincesList = DataSupport.findAll(Province.class);
        if (provincesList.size() > 0) {
            dataList.clear();
            for (Province province : provincesList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;  //当前级别为LEVEL_PROVINCE,就将去调用queryCities()方法查询城市
            Toast.makeText(getActivity(), "queryProvince()数据库中拿数据", Toast.LENGTH_SHORT).show();
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
            Toast.makeText(getActivity(), "queryProvince()服务器中拿数据", Toast.LENGTH_SHORT).show();
        }

    }


    //查询城市数据,先数据库查询,没找到再去服务器查询
    private void queryCity() {
        mTitleText.setText(selectedProvince.getProvinceName());
        mBackButton.setVisibility(View.VISIBLE);
        //provinceid的id大小写都行
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_CITY;
            Toast.makeText(getActivity(), "queryCity()数据库中拿数据", Toast.LENGTH_SHORT).show();
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
            Toast.makeText(getActivity(), "queryCity()服务器中拿数据", Toast.LENGTH_SHORT).show();
        }
    }

    //查询县数据,先数据库查询,没找到再去服务器查询
    private void queryCounty() {
        mTitleText.setText(selectedCity.getCityName());
        mBackButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
            Toast.makeText(getActivity(), "queryCounty()数据库中拿数据", Toast.LENGTH_SHORT).show();
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address,"county");
            Toast.makeText(getActivity(), "queryCounty()服务器中拿数据", Toast.LENGTH_SHORT).show();
        }
    }

    //通过服务器来查找
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {

            private boolean mResult = false;///////////////////////

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseTest = response.body().string(); //////////// 可能是toString();
                //boolean mResult = false;///////////////////////
                if ("province".equals(type)) {
                    mResult = Utillty.handleProvinceResponse(responseTest);
                } else if ("city".equals(type)) {
                    mResult = Utillty.handleCityResponse(responseTest, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    mResult = Utillty.handleCountyResponse(responseTest, selectedCity.getId());
                }

                if (mResult) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败...", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    //显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
