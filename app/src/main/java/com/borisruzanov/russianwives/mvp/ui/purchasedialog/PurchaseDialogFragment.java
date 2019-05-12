package com.borisruzanov.russianwives.mvp.ui.purchasedialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.utils.ConfirmListener;
import com.borisruzanov.russianwives.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseDialogFragment extends MvpAppCompatDialogFragment {

    public static String TAG = "Purchase Dialog";

    @BindView(R.id.purchase_coins_text)
    TextView coinsAmountTv;

    private ConfirmPurchaseListener listener;

    public interface ConfirmPurchaseListener {
        void onConfirmPurchase();
    }

    public static PurchaseDialogFragment newInstance(int coinsAmount) {
        Bundle args = new Bundle();
        args.putInt(Consts.COINS_AMOUNT, coinsAmount);
        PurchaseDialogFragment fragment = new PurchaseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmListener) {
            listener = (ConfirmPurchaseListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_purchase_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null) {
            coinsAmountTv.setText(String.valueOf(getArguments().getInt(Consts.COINS_AMOUNT)));
        }
    }

    @OnClick(R.id.purchase_confirm_btn)
    public void onConfirmClicked() {
        listener.onConfirmPurchase();
        dismiss();
    }

    @OnClick(R.id.purchase_cancel_btn)
    public void onCancelClicked() {
        dismiss();
    }

}
