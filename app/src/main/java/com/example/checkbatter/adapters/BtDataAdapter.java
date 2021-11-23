package com.example.checkbatter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.checkbatter.R;
import com.example.checkbatter.model.PropertiesBt;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class BtDataAdapter extends ArrayAdapter<PropertiesBt> {
    public BtDataAdapter(@NonNull Context context, @NonNull List<PropertiesBt> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_bt_info, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        PropertiesBt currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
//        ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
//        assert currentNumberPosition != null;
//        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView2 = currentItemView.findViewById(R.id.valueName);
        textView2.setText(currentNumberPosition.getValuePr());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView1 = currentItemView.findViewById(R.id.valueData);
        textView1.setText(currentNumberPosition.getLabelPr());

        // then return the recyclable view
        return currentItemView;
    }
}
