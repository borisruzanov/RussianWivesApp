package com.borisruzanov.russianwives.mvp.ui.purchasedialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.R;

public class PurchaseDialogFragment extends MvpAppCompatDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_purchase_fragment, container, false);
        return view;
    }
}
