package com.borisruzanov.russianwives.mvp.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.ui.main.MainActivity;
import com.borisruzanov.russianwives.utils.Consts;

public class RegisterFragment extends MvpAppCompatFragment {

    public static RegisterFragment newInstance(String tabName){
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(Consts.REG_TAB_NAME, tabName);
        fragment.setArguments(args);
        return fragment;
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView registerTv = view.findViewById(R.id.register_tv);
        Button registerButton = view.findViewById(R.id.register_button);

        if (getArguments().getString(Consts.REG_TAB_NAME).equals(Consts.CHATS_TAB_NAME)){
            registerTv.setText(R.string.need_register_chats);
        } else if (getArguments().getString(Consts.REG_TAB_NAME).equals(Consts.ACTIONS_TAB_NAME)){
            registerTv.setText(R.string.need_register_actions);
        }

        registerButton.setOnClickListener(view -> ((MainActivity)getActivity()).callAuthWindow());
    }

}
