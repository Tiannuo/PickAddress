package com.tikou.library_pickaddress.AddressPickView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tikou.library_pickaddress.DensityUtils;
import com.tikou.library_pickaddress.R;
import com.tikou.library_pickaddress.adapters.AbstractWheelTextAdapter;
import com.tikou.library_pickaddress.views.OnWheelChangedListener;
import com.tikou.library_pickaddress.views.OnWheelScrollListener;
import com.tikou.library_pickaddress.views.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/31.
 */
public class AddressPickView extends PopupWindow implements View.OnClickListener {


    private Button cancelBtn;
    private Button confirmBtn;
    private WheelView wvProvince, wvCitys, wvAreas;
    private TextView tv_dosage_time_middle;
    private View pickerContainerV;
    private View contentView;
    private LinearLayout rl_whell;
    private Context context;
    private JSONObject mJsonObj;
    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    private Map<String, String[]> mAreasDatasMap = new HashMap<String, String[]>();
    private ArrayList<String> arrProvinces = new ArrayList<String>();
    private ArrayList<String> arrCitys = new ArrayList<String>();
    private ArrayList<String> arrAreas = new ArrayList<String>();
    private AddressTextAdapter provinceAdapter;
    private AddressTextAdapter cityAdapter;
    private AddressTextAdapter areaAdapter;

    private String strProvince = "安徽";
    private String strCity = "合肥";
    private String strArea = "瑶海区";
    private OnAddressCListener onAddressCListener;

    private int maxsize = 24;
    private int minsize = 14;
    private int wheel_height=200;

    public AddressPickView(Context context) {
        this.context = context;
        init();
    }

    public AddressPickView(Context context,int dp) {
       this(context);
        this.wheel_height=dp;
        init();
    }


    private void init() {
        closeKey();
        contentView = LayoutInflater.from(context).inflate(R.layout.dosage_time_poup, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        tv_dosage_time_middle = (TextView) contentView.findViewById(R.id.tv_dosage_time_middle);
        rl_whell= (LinearLayout) contentView.findViewById(R.id.ll_whell);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context,wheel_height));
        LinearLayout.LayoutParams params1= (LinearLayout.LayoutParams) rl_whell.getLayoutParams();
        params1.height=DensityUtils.dp2px(context,wheel_height);
        rl_whell.setLayoutParams(params1);
        wvProvince = (WheelView) contentView.findViewById(R.id.wv_type);
        wvCitys = (WheelView) contentView.findViewById(R.id.wv_hour);
        wvAreas = (WheelView) contentView.findViewById(R.id.wv_minute);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        tv_dosage_time_middle.setText("请选择地址");

        initJsonData();
        initDatas();
        initProvinces();
        initWhellViews();
        initListener();

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);


        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initListener() {

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pickerContainerV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        wvProvince.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);

                String[] citys = mCitisDatasMap.get(currentText);
                initCitys(citys);
                cityAdapter = new AddressTextAdapter(context, arrCitys, 0, maxsize, minsize);
                wvCitys.setVisibleItems(5);
                wvCitys.setViewAdapter(cityAdapter);
                wvCitys.setCurrentItem(0);

                initAreas(mAreasDatasMap.get(strCity));
                areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
                wvAreas.setVisibleItems(5);
                wvAreas.setViewAdapter(areaAdapter);
                wvAreas.setCurrentItem(0);
            }
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvCitys.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(currentText, cityAdapter);

                String[] areas = mAreasDatasMap.get(currentText);
                initAreas(areas);
                areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
                wvAreas.setVisibleItems(5);
                wvAreas.setViewAdapter(areaAdapter);
                wvAreas.setCurrentItem(0);
            }
        });

        wvCitys.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });

        wvAreas.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                strArea = currentText;
                setTextviewSize(currentText, areaAdapter);
            }
        });

        wvAreas.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                System.out.println("currentText" + currentText);
                System.out.println("currentItem" + wheel.getCurrentItem());
                setTextviewSize(currentText, areaAdapter);
            }
        });

    }

    /**
     * 初始化选择器试图
     */
    private void initWhellViews() {
        provinceAdapter = new AddressTextAdapter(context, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(getProvinceItem(strProvince));

        initCitys(mCitisDatasMap.get(strProvince));
        cityAdapter = new AddressTextAdapter(context, arrCitys, getCityItem(strCity), maxsize, minsize);
        wvCitys.setVisibleItems(5);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(getCityItem(strCity));


        initAreas(mAreasDatasMap.get(strCity));
        areaAdapter = new AddressTextAdapter(context, arrAreas, getAreaItem(strArea), maxsize, minsize);
        wvAreas.setVisibleItems(5);
        wvAreas.setViewAdapter(areaAdapter);
        wvAreas.setCurrentItem(getAreaItem(strArea));
    }


//    public void setCurrentItem(RemindTime remindTime) {
//        if (remindTime==null){
//            return;
//        }
//        wv_type.setCurrentItem(Integer.parseInt(remindTime.getType_num()));
//        wv_hour.setCurrentItem(Integer.parseInt(remindTime.getHour_num()));
//        wv_minute.setCurrentItem(Integer.parseInt(remindTime.getMinute_num()));
//
//
//        setTextviewSize((String) type_adapter.getItemText(wv_type.getCurrentItem()), type_adapter);
//        setTextviewSize((String) hour_adapter.getItemText(wv_hour.getCurrentItem()), hour_adapter);
//        setTextviewSize((String) minute_adapter.getItemText(wv_minute.getCurrentItem()), minute_adapter);
//
//    }
//
//    public void setDefaultItem() {
//        wv_type.setCurrentItem(0);
//        wv_hour.setCurrentItem(0);
//        wv_minute.setCurrentItem(0);
//    }

    /**
     * 显示地址选择器弹层
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * 关闭地址选择器弹层
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pickerContainerV.startAnimation(trans);
    }


    @Override
    public void onClick(View v) {
        if (v == confirmBtn) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(strProvince, strCity, strArea);
            }
        } else if (v == cancelBtn) {

        } else {
            dismiss();
        }
        dismiss();

    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }

    /**
     * 从文件中读取地址数据
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据
     */
    private void initDatas() {


        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String province = jsonP.getString("p");
                mProvinceDatas[i] = province;
                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];

                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");
                        mAreasDatas[k] = area;
                    }
                    mAreasDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 初始化省会
     */
    public void initProvinces() {
        int length = mProvinceDatas.length;
        for (int i = 0; i < length; i++) {
            arrProvinces.add(mProvinceDatas[i]);
        }
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.whell_text_item, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {

            try {
                return list.get(index) + "";
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    /**
     * 根据省会，生成该省会的所有城市
     *
     * @param citys
     */
    public void initCitys(String[] citys) {
        if (citys != null) {
            arrCitys.clear();
            int length = citys.length;
            for (int i = 0; i < length; i++) {
                arrCitys.add(citys[i]);
            }
        } else {
            String[] city = mCitisDatasMap.get("安徽");
            arrCitys.clear();
            int length = city.length;
            for (int i = 0; i < length; i++) {
                arrCitys.add(city[i]);
            }
        }
        if (arrCitys != null && arrCitys.size() > 0
                && !arrCitys.contains(strCity)) {
            strCity = arrCitys.get(0);
        }
    }


    private void initAreas(String[] areas) {
        arrAreas.clear();
        if (areas != null) {
            int length = areas.length;
            for (int i = 0; i < length; i++) {
                arrAreas.add(areas[i]);
            }
        } else {
//			String[] area = mAreasDatasMap.get("合肥");
//			arrAreas.clear();
//			int length = area.length;
//			for (int i = 0; i < length; i++) {
//				arrAreas.add(area[i]);
//			}
        }
        if (arrAreas != null && arrAreas.size() > 0
                && !arrAreas.contains(strArea)) {
            strArea = arrAreas.get(0);
        } else {
            strArea = "";
        }

    }


    /**
     * 初始化地点
     *
     * @param
     * @param
     */
    public void setAddress(String[] address) {
        if (address == null || address.length == 0) {
            return;
        }
        this.strProvince = address[0];
        this.strCity = address[1];
        if (address.length == 3) {
            this.strArea = address[2];
        }
        provinceAdapter = null;
        cityAdapter = null;
        areaAdapter = null;
        initWhellViews();
    }

    /**
     * 返回省会索引，没有就返回默认“四川”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        if (province == null || province.equals("")) {
            return 0;
        }
        int size = arrProvinces.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrProvinces.get(i))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strProvince = "";
            return 0;
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“成都”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        if (city == null || city.equals("")) {
            return 0;
        }
        int size = arrCitys.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrCitys.get(i));
            if (city.equals(arrCitys.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strCity = "";
            return 0;
        }
        return cityIndex;
    }


    private int getAreaItem(String strArea) {
        if (strArea == null || strArea.equals("")) {
            return 0;
        }
        int size = arrAreas.size();
        int areaIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrAreas.get(i));
            if (strArea.equals(arrAreas.get(i))) {
                nocity = false;
                return areaIndex;
            } else {
                areaIndex++;
            }
        }
        if (nocity) {
            strArea = "";
            return 0;
        }
        return areaIndex;
    }


    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnAddressCListener {
        void onClick(String province, String city, String area);
    }

    /**
     * 关闭键盘
     */
    public void closeKey() {

        if (((Activity) context).getCurrentFocus() != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
