package com.borisruzanov.russianwives.mvp.ui.confirm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity;
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity;
import com.borisruzanov.russianwives.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialogFragment extends MvpAppCompatDialogFragment {

    public static String TAG = "Confirm dialog";

    @BindView(R.id.confirm_dialog_text)
    TextView headerTv;

    @BindView(R.id.confirm_button)
    Button confirmButton;

    @BindView(R.id.cancel_button)
    Button cancelButton;

    private ConfirmListener listener;

    public interface ConfirmListener {
        void onConfirm();

        void onUpdateDialog();

        void reviewDialogYes();

        void reviewDialogNo();

        void sendToPlayMarket();

        void sendComplain();
    }

    public static ConfirmDialogFragment newInstance(String module) {
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putString(Consts.MODULE, module);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmListener) {
            listener = (ConfirmListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_confirm_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (getArguments().getString(Consts.MODULE)){
            case Consts.REG_MODULE: case Consts.ACTION_MODULE:
                headerTv.setText(R.string.confirm_dialog_reg_header);
                break;
            case Consts.SLIDER_MODULE:
                headerTv.setText(R.string.confirm_dialog_slider_header);
                break;
            case Consts.FP_MODULE:
                headerTv.setText(R.string.full_profile_header);
                confirmButton.setText(R.string.now);
                cancelButton.setText(R.string.later);
                break;
            case Consts.UPDATE_MODULE:
                headerTv.setText(R.string.please_update_application);
                confirmButton.setText(R.string.now);
                cancelButton.setText(R.string.later);
                break;
            case Consts.RAITING_MODULE:
                headerTv.setText(R.string.do_you_like_app);
                confirmButton.setText(R.string.yes_ru);
                cancelButton.setText(R.string.no_ru);
                break;
            case Consts.VOTE_MODULE:
                headerTv.setText(R.string.leave_review);
                confirmButton.setText(R.string.yes_ru);
                cancelButton.setText(R.string.later);
                break;
            case Consts.NEGATIVE_VOTE_MODULE:
                headerTv.setText(R.string.leave_complain);
                confirmButton.setText(R.string.yes_ru);
                cancelButton.setText(R.string.later);
                break;
        }

    }

    @OnClick(R.id.confirm_button)
    public void onConfirmClicked() {
        switch (getArguments().getString(Consts.MODULE)){
            case Consts.REG_MODULE:
                ((FriendProfileActivity) getActivity()).callAuthWindow();
                break;
            case Consts.ACTION_MODULE:
                ((MainScreenActivity) getActivity()).callAuthWindow();
                break;
            case Consts.SLIDER_MODULE: case Consts.FP_MODULE:
                listener.onConfirm();
                break;
            case Consts.UPDATE_MODULE:
                listener.onUpdateDialog();
                break;
            case Consts.RAITING_MODULE:
                listener.reviewDialogYes();
                break;
            case Consts.VOTE_MODULE:
                listener.sendToPlayMarket();
                break;
            case Consts.NEGATIVE_VOTE_MODULE:
                listener.sendComplain();
                break;
        }
        dismiss();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        switch (getArguments().getString(Consts.MODULE)){
            case Consts.RAITING_MODULE:
                listener.reviewDialogNo();
                break;
        }
        dismiss();
    }

}
