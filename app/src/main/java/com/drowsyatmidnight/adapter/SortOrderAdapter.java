package com.drowsyatmidnight.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.drowsyatmidnight.nytarticle.R;

import java.util.List;

/**
 * Created by haint on 25/06/2017.
 */

public class SortOrderAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> objects;

    public SortOrderAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.sort_order, parent, false);
        TextView txtOrder = (TextView) row.findViewById(R.id.txtOrder);
        txtOrder.setText(objects.get(position));
        return row;
    }


}
