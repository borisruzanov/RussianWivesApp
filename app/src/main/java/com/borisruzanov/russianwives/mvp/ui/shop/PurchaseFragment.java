package com.borisruzanov.russianwives.mvp.ui.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;

public class PurchaseFragment extends Fragment {

    TextView mMiniText;
    TextView mMiniPrice;
    Button mMiniBtn;

    TextView mMediumText;
    TextView mMediumPrice;

    TextView mMaxText;
    TextView mMaxPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchases, null);
        mMiniText = view.findViewById(R.id.purchase_min_tv);
        mMiniText.setText("MINI \nGet first boorst");
        mMiniPrice = view.findViewById(R.id.purchase_min_price);
        mMiniPrice.setText(getString(R.string.mini_price));

        mMediumText = view.findViewById(R.id.purchase_medium_tv);
        mMediumText.setText("MEDIUM \nVisits + Likes");
        mMediumPrice = view.findViewById(R.id.purchase_medium_price);
        mMediumPrice.setText(getString(R.string.medium_price));

        mMaxText = view.findViewById(R.id.purchase_max_tv);
        mMaxText.setText("MAXIMUM \n VIP Status");
        mMaxPrice = view.findViewById(R.id.purchase_max_price);
        mMaxPrice.setText(getString(R.string.maximum_price));
        return view;
    }
}