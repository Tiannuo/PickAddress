package com.tikou.library_pickaddress.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tikou.library_pickaddress.R;

import java.util.ArrayList;


public class WhellTextAdapter extends AbstractWheelTextAdapter {
    ArrayList<String> list;

    public WhellTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index) + "";
    }

}